package com.jelly.thor.customview.yelopack

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jelly.thor.customview.R

/**
 * 创建人：吴冬冬<br/>
 * 创建时间：2020/4/23 17:53 <br/>
 */
class YeloPackActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yele_pack)

        val dfMemberFirstTips = DFMemberFirstTips.newInstance(object : DFMemberFirstTips.Callback {
            override fun callback() {
                val dfMemberAnimation = DFMemberAnimation.newInstance(object : DFMemberAnimation.Callback {
                    override fun callback() {
                        Toast.makeText(this@YeloPackActivity, "动画结束", Toast.LENGTH_SHORT).show()
                    }
                })
                dfMemberAnimation.show(supportFragmentManager, "123===")
            }
        })
        dfMemberFirstTips.show(supportFragmentManager, "123")
    }
}