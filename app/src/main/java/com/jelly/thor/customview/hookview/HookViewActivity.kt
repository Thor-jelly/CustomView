package com.jelly.thor.customview.hookview

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat
import com.jelly.thor.customview.R

/**
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/11/22 16:56 <br/>
 */
class HookViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val layoutInflater = LayoutInflater.from(this)
        LayoutInflaterCompat.setFactory2(layoutInflater, object : LayoutInflater.Factory2 {
            override fun onCreateView(
                parent: View?,
                name: String?,
                context: Context?,
                attrs: AttributeSet?
            ): View? {
                Log.d("123===", "拦截到View创建")
                if (name == "Button") {
                    val textView = TextView(this@HookViewActivity)
                    textView.text = "我把Button 转换成 TextView了"
                    return textView
                }
                return null
            }

            override fun onCreateView(
                name: String?,
                context: Context?,
                attrs: AttributeSet?
            ): View? {
                return onCreateView(null, name, context, attrs)
            }

        })
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hook_view)
    }
}