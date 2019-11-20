package com.jelly.thor.customview.wxbook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jelly.thor.customview.R
import com.jelly.thor.customview.wxbook.test.TestAdapter
import com.jelly.thor.customview.wxbook.test.TestBean
import kotlinx.android.synthetic.main.activity_wx_book.*
import java.util.*

/**
 * 类描述：仿微信通信录 <br/>
 * 学习自https://juejin.im/post/583c133eac502e006c23cc81
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/11/19 13:24 <br/>
 */
class WXBookActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wx_book)
        val list = mutableListOf<TestBean>()

        val b10 = TestBean()
        b10.name = "安啊"
        list.add(b10)

        val b9 = TestBean()
        b9.name = ".123"
        list.add(b9)

        val b8 = TestBean()
        b8.name = "123"
        list.add(b8)

        val b88 = TestBean()
        b88.name = "1223"
        list.add(b88)

        val b7 = TestBean()
        b7.name = "我是谁1"
        list.add(b7)

        val b6 = TestBean()
        b6.name = "我是谁空"
        list.add(b6)

        val b5 = TestBean()
        b5.name = "啊"
        list.add(b5)

        val b4 = TestBean()
        b4.name = "哎"
        list.add(b4)

        val b3 = TestBean()
        b3.name = "奥"
        list.add(b3)

        val b2 = TestBean()
        b2.name = "aa"
        list.add(b2)

        val bean1 = TestBean()
        bean1.name = "1啊"
        list.add(bean1)

        val bean0 = TestBean()
        bean0.name = "啊1"
        list.add(bean0)

        for (i in 0..10) {
            val vvvv = TestBean()
            vvvv.name = "我是谁bvvvv"
            list.add(vvvv)
        }

//        list.sort()
//        println("list")

        wx_view.setAdapter(TestAdapter(list))
    }
}