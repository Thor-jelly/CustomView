package com.jelly.thor.customview.yelopack

import android.animation.*
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.jelly.thor.customview.R

/**
 * 类描述：会员动画<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2020/4/23 18:46 <br/>
 */
class MemberAnimationView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    RelativeLayout(context, attrs, defStyleAttr) {

    interface Callback {
        fun callback()
    }

    private var mCallback: Callback? = null
    fun setCallback(callback: Callback) {
        mCallback = callback
    }


    private var memberAll: ConstraintLayout
    private var member1: ImageView
    private var member2: ImageView

    init {
        val view = View.inflate(context, R.layout.member_animation_view, this)
        memberAll = view.findViewById(R.id.member_all)
        member1 = view.findViewById(R.id.member_01)
        member2 = view.findViewById(R.id.member_02)
        post {
            startAnimation()
        }
    }

    private fun startAnimation() {
        //1.绘制缩放旋转动画
        val member1ScaleX = ObjectAnimator.ofFloat(member1, "scaleX", 0F, 1F)
            .setDuration(1500)
        member1ScaleX.startDelay = 200L
        val member1ScaleY = ObjectAnimator.ofFloat(member1, "scaleY", 0F, 1F)
            .setDuration(1500)
        member1ScaleY.startDelay = 200L

        val member2Rotation =
            ObjectAnimator.ofFloat(member2, "rotation", 0F, 360F).setDuration(3500)
        member2Rotation.startDelay = 600L

        val animator = AnimatorSet()
        animator.playTogether(
            member1ScaleX,
            member1ScaleY,
            member2Rotation
        )
        animator.start()

        val valueAnimator = ValueAnimator.ofFloat(1F, 0F)
            .setDuration(1200)
        valueAnimator.startDelay = 2500L
        valueAnimator.start()
        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                memberAll.background = ColorDrawable(Color.TRANSPARENT)
            }

            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                mCallback?.callback()
            }
        })
        valueAnimator.addUpdateListener {
            val scaleF = it.animatedValue as Float
            member1.scaleX = scaleF
            member1.scaleY = scaleF
            member2.scaleX = scaleF
            member2.scaleY = scaleF

            val member1W = -((width - member1.width * scaleF) / 2) * (scaleF - 1)
            val member1H = ((height - member1.height * scaleF) / 2) * (scaleF - 1)
            member1.translationX = member1W
            member1.translationY = member1H

            //val member2W = ((width - member2.width * scaleF) / 2 - w) * (1 - scaleF)
            val member2W = -((width - member2.width * scaleF) / 2) * (scaleF - 1)
            val member2H = ((height - member2.height * scaleF) / 2) * (scaleF - 1)
            member2.translationX = member2W
            member2.translationY = member2H
        }
    }
}