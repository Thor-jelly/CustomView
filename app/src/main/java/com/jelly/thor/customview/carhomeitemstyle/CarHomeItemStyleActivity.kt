package com.jelly.thor.customview.carhomeitemstyle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jelly.thor.customview.R
import kotlinx.android.synthetic.main.activity_car_home_item_style.*

/**
 * 类描述：//汽车之家折叠列表 <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/7/15 13:46 <br/>
 */
class CarHomeItemStyleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_home_item_style)

        initRv()
    }

    private fun initRv() {
        mRv.adapter = TestAdapter()
    }
}