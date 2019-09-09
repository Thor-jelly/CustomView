package com.jelly.thor.customview

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jelly.thor.customview.carhomeitemstyle.CarHomeItemStyleActivity
import com.jelly.thor.customview.carnumberkeyboard.CarNumberKeyboardActivity
import com.jelly.thor.customview.custombehavior.CustomBehaviorActivity
import com.jelly.thor.customview.equalscalecouponview.EqualScaleCouponViewActivity
import com.jelly.thor.customview.kugou.SlidingKuGouMenuActivity
import com.jelly.thor.customview.ninelockpalaces.NineLockPalacesActivity
import com.jelly.thor.customview.notchline.NotchLineActivity
import com.jelly.thor.customview.qq6.SlidingQq6MenuActivity
import com.jelly.thor.customview.snaphelptest.SnapHelpActivity
import com.jelly.thor.customview.statusbar.StatusBarActivity
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

        clickNotchLine.setOnClickListener {
            //自定义view缺口线
            startNewActivity(NotchLineActivity::class.java)
        }

        clickStatusBar.setOnClickListener {
            //状态栏测试界面
            startNewActivity(StatusBarActivity::class.java)
        }

        clickCustomBehavior.setOnClickListener {
            //自定义behavior
            startNewActivity(CustomBehaviorActivity::class.java)
        }

        clickCarNumberKeyboard.setOnClickListener {
            //自定义车牌号键盘
            startNewActivity(CarNumberKeyboardActivity::class.java)
        }

        clickEqualScaleCouponViewActivity.setOnClickListener {
            //优惠券view 生成图片再按比例缩放，形成在任意手机上效果一致
            startNewActivity(EqualScaleCouponViewActivity::class.java)
        }
    }

    private fun startNewActivity(cls: Class<*>) {
        startActivity(Intent(this, cls))
    }
}
