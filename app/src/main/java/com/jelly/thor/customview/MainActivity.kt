package com.jelly.thor.customview

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jelly.thor.customview.carhomeitemstyle.CarHomeItemStyleActivity
import com.jelly.thor.customview.kugou.SlidingKuGouMenuActivity
import com.jelly.thor.customview.ninelockpalaces.NineLockPalacesActivity
import com.jelly.thor.customview.qq6.SlidingQq6MenuActivity
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
        clickFontStyleBold.setFontStyle(FontStyleEnum.DIN_ALTERNATE_BOLD)
        //设置新字体加粗
        clickFontStyleBold.paint.isFakeBoldText = true

        clickSnapHelpTest.setOnClickListener {
            startActivity(Intent(this, SnapHelpActivity::class.java))
        }

        clickKuGouSlidingMenu.setOnClickListener {
            //酷狗侧边栏样式
            startNewActivity(SlidingKuGouMenuActivity::class.java)
        }

        clickQq6SlidingMenu.setOnClickListener {
            //qq6.0侧边栏效果
            startNewActivity(SlidingQq6MenuActivity::class.java)
        }

        clickCarHomeItem.setOnClickListener {
            //汽车之家折叠列表
            startNewActivity(CarHomeItemStyleActivity::class.java)
        }

        click9LockPalaces.setOnClickListener {
            //9宫格解锁样式
            startNewActivity(NineLockPalacesActivity::class.java)
        }
    }

    private fun startNewActivity(cls: Class<*>) {
        startActivity(Intent(this, cls))
    }
}
