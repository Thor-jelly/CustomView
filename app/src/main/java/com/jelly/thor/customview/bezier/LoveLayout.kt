package com.jelly.thor.customview.bezier

import android.animation.*
import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import android.view.animation.*
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.jelly.thor.customview.R
import java.util.*
import kotlin.math.pow

/**
 * 类描述：底部花束效果<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/11/13 14:12 <br/>
 */
class LoveLayout @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    RelativeLayout(context, attrs, defStyleAttr) {
    private val mImageRes by lazy {
        intArrayOf(R.drawable.bezier_pl_blue, R.drawable.bezier_pl_red, R.drawable.bezier_pl_yellow)
    }

    private val mInterpolator by lazy {
        arrayOf<TimeInterpolator>(
            AccelerateDecelerateInterpolator()
            , AccelerateInterpolator()
            , DecelerateInterpolator()
            , LinearInterpolator()
            , OvershootInterpolator()
        )
    }

    private val mRandom by lazy {
        Random()
    }

    //当前LoveLayout的宽高
    private var mWidth = 0F
    private var mHeight = 0F
    //图片的宽高
    private var mImageWidth = 0F
    private var mImageHeight = 0F

    fun addLove() {
        //添加一个ImageView在底部中心
        val loveIv = ImageView(context)

        //设置第一个图片随机
        val imageResId = mImageRes[mRandom.nextInt(mImageRes.size)]
        val imageDrawable = ContextCompat.getDrawable(context, imageResId)!!
        mImageWidth = imageDrawable.intrinsicWidth.toFloat()
        mImageHeight = imageDrawable.intrinsicHeight.toFloat()

        loveIv.setImageResource(imageResId)
        val params = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.addRule(ALIGN_PARENT_BOTTOM)
        params.addRule(CENTER_HORIZONTAL)
        loveIv.layoutParams = params

        addView(loveIv)

        //添加效果 透明度放大 再从底部贝塞尔往上 最后消失
        val animatorSet = getAnimator(loveIv)
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                //动画执行完毕后移除view
                removeView(loveIv)
            }
        })
    }

    /**
     * 获取动画
     */
    private fun getAnimator(loveIv: ImageView): AnimatorSet {
        val allAnimatorSet = AnimatorSet()

        val animatorSet = AnimatorSet()
        val alphaAnimator = ObjectAnimator.ofFloat(loveIv, "alpha", 0.3F, 1F)
        val scaleXAnimator = ObjectAnimator.ofFloat(loveIv, "scaleX", 0.3F, 1F)
        val scaleYAnimator = ObjectAnimator.ofFloat(loveIv, "scaleY", 0.3F, 1F)

        animatorSet.duration = 350
        //一起执行 透明度 和放大动画
        animatorSet.playTogether(alphaAnimator, scaleXAnimator, scaleYAnimator)

        //运行路径动画
        allAnimatorSet.playSequentially(animatorSet, getBezierAnimator(loveIv))

        allAnimatorSet.start()

        return allAnimatorSet
    }

    /**
     * 获取贝塞尔动画
     */
    private fun getBezierAnimator(loveIv: ImageView): Animator {
        //贝塞尔曲线 公式地址 https://baike.baidu.com/item/%E8%B4%9D%E5%A1%9E%E5%B0%94%E6%9B%B2%E7%BA%BF/1091769?fr=aladdin

        //p0 p1 p2 p3 从底部到顶部点
        //p0起始点， p3终止点
        val p0 = PointF(mWidth / 2 - mImageWidth / 2, mHeight - mImageHeight)
        val p3 = PointF(mRandom.nextInt(mWidth.toInt()).toFloat(), 0F)
        //p2 的 y 一定比 p1大
        val p1 = PointF(
            mRandom.nextInt(mWidth.toInt()).toFloat(),
            mRandom.nextInt((mHeight / 2).toInt()).toFloat()
        )
        val p2 = PointF(
            mRandom.nextInt(mWidth.toInt()).toFloat(),
            mRandom.nextInt((mHeight / 2).toInt()).toFloat() + mHeight / 2
        )
        Log.d("123===", "p0=${p0}  ,p3=${p3} ,,,,,,  p1=${p1}  ,p2=${p2}")

        val bezierTypeEvaluator =
            TypeEvaluator<PointF> { t, p00, p33 ->
                //t 0-1;
                //p0起始点， p3终止点
                //p1 p2 控制点
                val newPointF = PointF()
                newPointF.x = p00.x * (1 - t).pow(3) +
                        3 * p1.x * t * (1 - t).pow(2) +
                        3 * p2.x * t.pow(2) * (1 - t) +
                        p33.x * t.pow(3)
                newPointF.y = p00.y * (1 - t).pow(3) +
                        3 * p1.y * t * (1 - t).pow(2) +
                        3 * p2.y * t.pow(2) * (1 - t) +
                        p33.y * t.pow(3)

                //Log.d("123===", "t=${t}  ,love新点位置=$newPointF")
                return@TypeEvaluator newPointF
            }

        val bezierTypeAnimator = ObjectAnimator.ofObject(bezierTypeEvaluator, p0, p3)
        //添加差值器，让效果更好点
        bezierTypeAnimator.interpolator = mInterpolator[mRandom.nextInt(mInterpolator.size)]
        bezierTypeAnimator.addUpdateListener {
            val newPointF = it.animatedValue as PointF
            //Log.d("123===", "设置点的位置=$newPointF")
            //设置的是左上角点，所以需要吧起始点换为左上角
            loveIv.x = newPointF.x
            loveIv.y = newPointF.y
            //到上面越来越浅
            //获取动画变化，也就是t
            val t = it.animatedFraction
            loveIv.alpha = (1 - t * 0.2).toFloat()

        }
        bezierTypeAnimator.duration = 1500
        return bezierTypeAnimator
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        mHeight = MeasureSpec.getSize(heightMeasureSpec).toFloat()
        //Log.d("123===", "当前love 宽=${mWidth} 高=${mHeight}")
    }
}