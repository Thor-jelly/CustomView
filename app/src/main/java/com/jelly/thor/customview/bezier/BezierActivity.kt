package com.jelly.thor.customview.bezier

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Consumer
import com.jelly.thor.customview.R
import kotlinx.android.synthetic.main.activity_bezier.*

/**
 * 贝塞尔曲线
 */
class BezierActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bezier)
        Bezier01Update.bind(mTv, Consumer {
            Log.d("123===", "---------->$it")
        })
    }
}