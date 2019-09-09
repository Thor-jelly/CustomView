package com.jelly.thor.customview.carnumberkeyboard.lib

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import com.jelly.thor.customview.R
import com.jelly.thor.customview.carnumberkeyboard.lib.view.KeyboardView

/**
 * 类描述：键盘帮助类<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/9/2 14:15 <br/>
 */
object KeyboardHelper {
    /**
     * 显示键盘
     */
    @JvmStatic
    fun showKeyboard(
        window: Window?,
        keyboardView: KeyboardView,
        isDialog: Boolean
    ) {
        //获取当前内容布局
        if (window == null) {
            return
        }
        val rootView = window.decorView.findViewById<View>(android.R.id.content)
        //获取键盘包装布局
        var keyboardWrapper = rootView.findViewById<FrameLayout>(R.id.keyboard_wrapper_id)
        if (keyboardWrapper == null) {
            //移除当前键盘布局的父布局
            val keyboardViewParent = keyboardView.parent
            if (keyboardViewParent != null) {
                if ((keyboardViewParent as View).id == R.id.keyboard_wrapper_id) {
                    keyboardWrapper = keyboardViewParent as FrameLayout
                    //移除父布局
                    (keyboardWrapper.parent as ViewGroup).removeView(keyboardWrapper)
                }
            }

            if (keyboardWrapper == null) {
                keyboardWrapper = createKeyboardWrapperView(keyboardView)
            }
            if (rootView is FrameLayout) {
                val params = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    Gravity.BOTTOM
                )
                if (isDialog) {
                    params.topMargin = (rootView as ViewGroup).getChildAt(0).height
                }
                rootView.addView(keyboardWrapper, params)
            }
        } else {
            keyboardView.visibility = View.VISIBLE
            //把键盘布局移动最顶层
            keyboardView.bringToFront()
        }
    }

    private fun createKeyboardWrapperView(keyboardView: KeyboardView): FrameLayout {
        val keyboardWrapperView = FrameLayout(keyboardView.context)
        keyboardWrapperView.id = R.id.keyboard_wrapper_id
        keyboardWrapperView.clipChildren = false

        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            Gravity.BOTTOM
        )
        keyboardWrapperView.addView(keyboardView, params)
        return keyboardWrapperView
    }

    /**
     * 隐藏键盘
     */
    @JvmStatic
    fun hideKeyboard(
        window: Window?
    ) {
        //获取当前内容布局
        if (window == null) {
            return
        }
        val rootView = window.decorView.findViewById<View>(android.R.id.content)
        //获取键盘包装布局
        val keyboardWrapper = rootView.findViewById<FrameLayout>(R.id.keyboard_wrapper_id)
        if (keyboardWrapper != null) {
            (keyboardWrapper.parent as ViewGroup).removeView(keyboardWrapper)
        }
    }
}