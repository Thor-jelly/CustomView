package com.jelly.thor.customview.loadinganimation00

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import com.jelly.thor.customview.R

/**
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/10/16 10:05 <br/>
 */
class LoadingView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    LinearLayout(context, attrs, defStyleAttr) {

    init {
        gravity = Gravity.CENTER
        startAnimator()
    }

    /**
     * 动画执行时间
     */
    var mAnimatorDuration = 350L

    /**
     * 动态变化的view
     */
    private var mShapeView: ShapeView? = null

    /**
     * 阴影
     */
    private var mShadowView: View? = null

    /**
     * 文本
     */
    private var mTextStrView: TextView? = null

    private fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context!!.resources.displayMetrics
        ).toInt()
    }

    /**
     * 开始动画
     */
    fun startAnimator() {
        post {
            if (mShapeView == null) {
                inflate(context, R.layout.activity_loading_animation00_ui_view, this)

                mShapeView = findViewById(R.id.mShapeView)
                mShadowView = findViewById<ShapeView>(R.id.mShadowV)
                mTextStrView = findViewById(R.id.mTv)
            }

            if (mShapeView?.animation == null) {
                starDownAnimator()
            }
        }
    }

    /**
     * 停止动画
     */
    fun stopAnimator() {
        //清除动画
        mShapeView?.clearAnimation()
        mShadowView?.clearAnimation()
        //把loading view从父布局移除
        val viewGroup = parent as ViewGroup?
        if (viewGroup != null) {
            viewGroup.removeView(this)
            removeAllViews()
        }
        mShapeView = null
        mShadowView = null
        mTextStrView = null
    }

    private fun starDownAnimator() {
        if (mShapeView == null) {
            return
        }
        //开始下落先
        val translationObjectAnimator =
            ObjectAnimator.ofFloat(
                mShapeView!!,
                "translationY",
                0F,
                dp2px(100).toFloat()
            )

        //中间缩影
        val scaleObjectAnimator =
            ObjectAnimator.ofFloat(
                mShadowView!!,
                "scaleX",
                1f,
                0.3f
            )

        val animator = AnimatorSet()
        animator.playTogether(translationObjectAnimator, scaleObjectAnimator)
        animator.duration = mAnimatorDuration
        animator.interpolator = AccelerateInterpolator(1.8F)
        animator.start()

        //下落完成上抛
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                //下落完成,上抛
                startUpAnimator()
            }
        })
    }

    private fun startUpAnimator() {
        if (mShapeView == null) {
            return
        }
        //开始下落先
        val translationObjectAnimator =
            ObjectAnimator.ofFloat(
                mShapeView!!,
                "translationY",
                dp2px(100).toFloat(),
                0F

            )

        //中间缩影
        val scaleObjectAnimator =
            ObjectAnimator.ofFloat(
                mShadowView!!,
                "scaleX",
                0.3f,
                1f
            )

        val animator = AnimatorSet()
        animator.playTogether(translationObjectAnimator, scaleObjectAnimator)
        animator.duration = mAnimatorDuration
        animator.interpolator = DecelerateInterpolator(1.8F)

        //下落完成上抛
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?, isReverse: Boolean) {
                val rotationI = when (mShapeView!!.mShapeEnum) {
                    ShapeView.ShapeEnum.TRIANGLE -> 120F
                    else -> 180F
                }
                //选装当前shape view
                val rotationAnimator = ObjectAnimator.ofFloat(
                    mShapeView!!,
                    "rotation",
                    0F,
                    rotationI
                )
                rotationAnimator.duration = mAnimatorDuration
                rotationAnimator.interpolator = DecelerateInterpolator(1.8F)
                rotationAnimator.start()
            }

            override fun onAnimationEnd(animation: Animator?) {
                //下落完成
                mShapeView!!.exchange()
                starDownAnimator()
            }
        })

        animator.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.AT_MOST,MeasureSpec.UNSPECIFIED -> {
                dp2px(150)
            }
            MeasureSpec.EXACTLY -> {
                MeasureSpec.getSize(widthMeasureSpec)
            }
            else -> {
                dp2px(150)
            }
        }

        val height = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.AT_MOST,MeasureSpec.UNSPECIFIED -> {
                dp2px(300)
            }
            MeasureSpec.EXACTLY -> {
                MeasureSpec.getSize(heightMeasureSpec)
            }
            else -> {
                dp2px(300)
            }
        }
        setMeasuredDimension(width, height)
    }
}