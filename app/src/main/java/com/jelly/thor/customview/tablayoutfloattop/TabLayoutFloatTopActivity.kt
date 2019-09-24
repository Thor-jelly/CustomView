package com.jelly.thor.customview.tablayoutfloattop

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jelly.thor.customview.R
import kotlinx.android.synthetic.main.activity_tab_layout_float_top.*

/**
 * 类描述：tab悬浮在最上方 , <br/>
 * 创建人：吴冬冬<br/>
 * https://www.jianshu.com/p/9eea61538c67
 * https://www.jianshu.com/p/bbc703a0015e
 * https://www.cnblogs.com/anni-qianqian/p/6814061.html
 * 创建时间：2019/9/24 14:52 <br/>
 */
class TabLayoutFloatTopActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_layout_float_top)

        mTest1Tv.setOnClickListener {
            startNewActivity(TabLayoutFloatTopTest1Activity::class.java)
        }
    }

    private fun startNewActivity(cls: Class<*>) {
        startActivity(Intent(this, cls))
    }
}