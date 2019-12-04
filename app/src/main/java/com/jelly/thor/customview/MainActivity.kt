package com.jelly.thor.customview

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jelly.thor.customview.bezier.BezierActivity
import com.jelly.thor.customview.carhomeitemstyle.CarHomeItemStyleActivity
import com.jelly.thor.customview.carnumberkeyboard.CarNumberKeyboardActivity
import com.jelly.thor.customview.custombehavior.CustomBehaviorActivity
import com.jelly.thor.customview.equalscalecouponview.EqualScaleCouponViewActivity
import com.jelly.thor.customview.fonttest.FontTestActivity
import com.jelly.thor.customview.hookview.HookViewActivity
import com.jelly.thor.customview.kugou.SlidingKuGouMenuActivity
import com.jelly.thor.customview.loadinganimation00.LoadingAnimation00Activity
import com.jelly.thor.customview.ninelockpalaces.NineLockPalacesActivity
import com.jelly.thor.customview.notchline.NotchLineActivity
import com.jelly.thor.customview.parallaxanimator.ParallaxAnimatorActivity
import com.jelly.thor.customview.qq6.SlidingQq6MenuActivity
import com.jelly.thor.customview.ratingbar.RatingBarActivity
import com.jelly.thor.customview.snaphelptest.SnapHelpActivity
import com.jelly.thor.customview.statusbar.StatusBarActivity
import com.jelly.thor.customview.tablayoutfloattop.TabLayoutFloatTopActivity
import com.jelly.thor.customview.wxbook.WXBookActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clickFontStyle.setOnClickListener {
            //设置新字体
            startNewActivity(FontTestActivity::class.java)
        }

        clickTabLayoutFloatTop.setOnClickListener {
            //tab悬浮在最上方
            startNewActivity(TabLayoutFloatTopActivity::class.java)
        }

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

        clickLoadingAnimationActivity.setOnClickListener {
            //加载view00
            startNewActivity(LoadingAnimation00Activity::class.java)
        }

        clickRatingBarActivity.setOnClickListener {
            //自定义评价星星
            startNewActivity(RatingBarActivity::class.java)
        }

        clickBezierActivity.setOnClickListener {
            //贝塞尔 曲线学习
            startNewActivity(BezierActivity::class.java)
        }

        clickParallaxAnimatorActivity.setOnClickListener {
            //视差动画学习
            startNewActivity(ParallaxAnimatorActivity::class.java)
        }

        clickWXBookActivity.setOnClickListener {
            //仿微信通讯录
            startNewActivity(WXBookActivity::class.java)
        }
        clickHookViewActivity.setOnClickListener {
            //HookView测试
            startNewActivity(HookViewActivity::class.java)
        }
    }

    private fun startNewActivity(cls: Class<*>) {
        startActivity(Intent(this, cls))
    }
}
