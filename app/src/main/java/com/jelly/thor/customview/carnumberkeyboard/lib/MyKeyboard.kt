package com.jelly.thor.customview.carnumberkeyboard.lib

import android.app.Dialog
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import com.jelly.thor.customview.carnumberkeyboard.lib.view.KeyboardView

/**
 * 类描述：我的键盘<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/9/2 13:13 <br/>
 */
class MyKeyboard(val keyboardView: KeyboardView) {
    /**
     * 是否是dialog
     */
    private var isDialog = false

    /**
     * 绑定方法
     */
    fun bindEt(et: AppCompatEditText, activity: AppCompatActivity) {
        bindEt(et, activity.window)
    }

    /**
     * 绑定方法
     */
    fun bindEt(et: AppCompatEditText, dialog: Dialog) {
        isDialog = true
        bindEt(et, dialog.window)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun bindEt(et: AppCompatEditText, window: Window?) {
        //
    }

    /**
     * 隐藏键盘
     */
    fun hideKeyboard(window: Window?) {
        KeyboardHelper.hideKeyboard(window)
    }

    /**
     * 显示键盘
     */
    fun showKeyboard(window: Window?) {
        KeyboardHelper.showKeyboard(window, keyboardView, isDialog)
    }
}