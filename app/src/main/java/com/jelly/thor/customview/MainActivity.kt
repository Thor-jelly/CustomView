package com.jelly.thor.customview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jelly.thor.customview.kugou.SlidingKuGouMenuActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clickKuGouSlidingMenu.setOnClickListener {
            //酷狗侧边栏样式
            clickKuGouSlidingMethod()
        }
    }

    /**
     * 酷狗侧边栏样式
     */
    private fun clickKuGouSlidingMethod() {
        startActivity(Intent(this, SlidingKuGouMenuActivity::class.java))
    }
}
