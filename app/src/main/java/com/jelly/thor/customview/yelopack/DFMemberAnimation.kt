package com.jelly.thor.customview.yelopack

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import com.jelly.thor.customview.DFBase
import com.jelly.thor.customview.R
import kotlinx.android.synthetic.main.df_member_animation.*

/**
 * 类描述：会员第一次提醒 <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2020/4/24 16:05 <br/>
 */
class DFMemberAnimation : DFBase() {
    companion object {
        @JvmStatic
        fun newInstance(callback: Callback): DFMemberAnimation {
            val dfMemberFirstTips = DFMemberAnimation()
            dfMemberFirstTips.setCallback(callback)
            return dfMemberFirstTips
        }
    }

    interface Callback {
        fun callback()
    }

    private var mCallback: Callback? = null
    fun setCallback(callback: Callback) {
        mCallback = callback
    }

    override fun initView(view: View?) {
        mView.setCallback(object : MemberAnimationView.Callback {
            override fun callback() {
                dismiss()
                mCallback?.callback()
            }
        })
    }

    /**
     * 设置dialogFragment参数
     */
    override fun setDialogParams(window: Window) {
        //设置宽度百分比
        //#第一种
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun setOutClick() {
        isCancelable = false
    }

    override fun setOutBackgroundDrawableTransparent(window: Window) {
        window.setDimAmount(0F)
    }

    override fun createView(): Int {
        return R.layout.df_member_animation
    }
}