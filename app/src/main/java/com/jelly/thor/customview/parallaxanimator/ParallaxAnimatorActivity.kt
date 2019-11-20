package com.jelly.thor.customview.parallaxanimator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jelly.thor.customview.R
import kotlinx.android.synthetic.main.activity_parallax_animator.*

/**
 * 类描述：视差动画 <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/11/14 13:16 <br/>
 */
class ParallaxAnimatorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parallax_animator)

        mVp01.setLayout(
            supportFragmentManager,
            intArrayOf(R.layout.f_s_01_first, R.layout.f_s_01_second, R.layout.f_s_01_third)
        )
    }
}