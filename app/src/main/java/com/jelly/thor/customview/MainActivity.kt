package com.jelly.thor.customview

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jelly.thor.customview.kugou.SlidingKuGouMenuActivity
import com.jelly.thor.customview.snaphelptest.SnapHelpActivity
import com.jelly.thor.customview.utils.FontStyleEnum
import com.jelly.thor.customview.utils.setFontStyle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //设置新字体
        clickFontStyle.setFontStyle(FontStyleEnum.DIN_ALTERNATE_BOLD)
        //设置新字体加粗
        //clickFontStyle.paint.isFakeBoldText = true

        clickSnapHelpTest.setOnClickListener {
            startActivity(Intent(this, SnapHelpActivity::class.java))
        }

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
