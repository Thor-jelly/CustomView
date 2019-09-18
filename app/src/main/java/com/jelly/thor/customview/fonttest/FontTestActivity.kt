package com.jelly.thor.customview.fonttest

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jelly.thor.customview.R
import com.jelly.thor.customview.utils.FontStyleEnum
import com.jelly.thor.customview.utils.setFontStyle
import kotlinx.android.synthetic.main.activity_font_test.*

/**
 * 类描述：字体测试activity <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/9/18 15:41 <br/>
 */
class FontTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_font_test)

        m1Tv.setFontStyle(FontStyleEnum.DIN_ALTERNATE_BOLD)
        m2Tv.setFontStyle(FontStyleEnum.DIN_ALTERNATE_BOLD, true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val resFontTypeface = resources.getFont(R.font.din_alternate_bold)
            m5Tv.typeface = resFontTypeface
        }
    }
}