package com.jelly.thor.customview.ratingbar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jelly.thor.customview.R
import kotlinx.android.synthetic.main.activity_rating_bar.*

/**
 * 类描述：自定义评价星星 <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/10/16 14:36 <br/>
 */
class RatingBarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating_bar)
        mRb.setSelectStar(4)
    }
}