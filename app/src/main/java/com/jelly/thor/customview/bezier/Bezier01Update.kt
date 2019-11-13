package com.jelly.thor.customview.bezier

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.graphics.PointF
import android.graphics.drawable.AnimationDrawable
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.util.Consumer
import com.jelly.thor.customview.R


/**
 * 仿qq消息拖拽效果升级版
 *
 * 实现方法：把原来view隐藏，并生成一张图片view 在windowManger上view 实现对所有view都有效果
 */
class Bezier01Update private constructor(private val view: View, listener: Consumer<String>) {
    companion object {
        /**
         * 绑定
         */
        fun bind(view: View, listener: Consumer<String>): Bezier01Update {
            return Bezier01Update(view, listener)
        }

        /**
         * 获取状态栏高度
         */
        private fun getStatusBarHeight(context: Context): Int {
            //获取status_bar_height资源的ID
            val resourceId =
                context.resources.getIdentifier("status_bar_height", "dimen", "android")
            return if (resourceId > 0) {
                //根据资源ID获取响应的尺寸值
                context.resources.getDimensionPixelSize(resourceId)
            } else {
                dp2Px(25, context)
            }
        }

        private fun dp2Px(dp: Int, context: Context): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                context.resources.displayMetrics
            ).toInt()
        }
    }


    /**
     * windowManger管理类
     */
    private val mWindowManager by lazy {
        view.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    private val mWindowManagerParams by lazy {
        val params = WindowManager.LayoutParams()
        params.format = PixelFormat.TRANSPARENT
        params
    }

    private val mBombFrame by lazy {
        FrameLayout(view.context)
    }

    private val mBombImage by lazy {
        val img = ImageView(view.context)
        img.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        mBombFrame.addView(img)
        img
    }

    /**
     * 具有贝塞尔效果的view
     */
    private val mBezier01View by lazy {
        val view = Bezier01(view.context)
        view.setListener(object : Bezier01.Companion.Listener {
            override fun reset() {
                //移除windowManager中添加的view，显示原view
                mWindowManager.removeView(view)
                this@Bezier01Update.view.visibility = View.VISIBLE
            }

            override fun dismiss(pointF: PointF) {
                //取消 实现爆炸效果

                // 要去执行爆炸动画 （帧动画）
                // 原来的View的View肯定要移除
                mWindowManager.removeView(view)
                // 要在 mWindowManager 添加一个爆炸动画
                mWindowManager.addView(mBombFrame, mWindowManagerParams)
                mBombImage.setBackgroundResource(R.drawable.anim_bezier_bubble_pop)

                val drawable = mBombImage.background as AnimationDrawable
                mBombImage.x = pointF.x - drawable.intrinsicWidth / 2
                mBombImage.y = pointF.y - drawable.intrinsicHeight / 2

                drawable.start()
                // 等它执行完之后我要移除掉这个 爆炸动画也就是 mBombFrame
                mBombImage.postDelayed({
                    mWindowManager.removeView(mBombFrame)
                    // 通知一下外面该消失
                    listener.accept("view被取消了")
                }, getAnimationDrawableTime(drawable))
            }
        })
        view
    }

    private fun getAnimationDrawableTime(drawable: AnimationDrawable): Long {
        val numberOfFrames = drawable.numberOfFrames
        var time: Long = 0
        for (i in 0 until numberOfFrames) {
            time += drawable.getDuration(i).toLong()
        }
        return time
    }

    /**
     * 状态栏高度
     */
    private val mStatusBarHeightSize by lazy {
        getStatusBarHeight(view.context)
    }

    init {
        view.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    //在windowManager上搞一个view，也就是基本的贝塞尔view
                    mWindowManager.addView(mBezier01View, mWindowManagerParams)

                    //初始化贝塞尔view的点
                    val location = intArrayOf(0, 0)
                    view.getLocationOnScreen(location)
                    mBezier01View.initPoint(
                        location[0].toFloat() + view.width / 2,
                        location[1].toFloat() + view.height / 2 - mStatusBarHeightSize
                    )
                    //mBezier01View.initPoint(event.rawX, event.rawY - mStatusBarHeightSize)

                    //给拖拽view设置bitmap
                    getBitmapByView(view)
                    mBezier01View.setBitmap(getBitmapByView(view))

                    //按下先把自己隐藏
                    view.visibility = View.INVISIBLE
                }
                MotionEvent.ACTION_MOVE -> {
                    mBezier01View.updateMovePoint(event.rawX, event.rawY - mStatusBarHeightSize)
                }
                MotionEvent.ACTION_UP -> {
                    mBezier01View.handlerActionUp()
                }
            }
            return@setOnTouchListener true
        }
    }

    /**
     * 从view中获取bitmap
     */
    private fun getBitmapByView(view: View): Bitmap {
        view.buildDrawingCache()
        return view.drawingCache
    }
}