package com.jelly.thor.customview.wxbook

import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jelly.thor.customview.R

/**
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/11/19 17:28 <br/>
 */
class WXBookView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    RelativeLayout(context, attrs, defStyleAttr) {
    private val mRv by lazy {
        val rv = findViewById<RecyclerView>(R.id.wx_rv)
        rv
    }

    private val mIndexBar by lazy {
        findViewById<IndexBar>(R.id.index_bar)
    }

    private val mTv by lazy {
        findViewById<TextView>(R.id.wx_preview)
    }

    init {
        View.inflate(context, R.layout.wx_book_view, this)
    }

    fun <T : WXBean> setAdapter(adapter: WXBaseAdapter<T>) {
        mRv.addItemDecoration(WXTitleItemDecoration(adapter.list))
        mRv.adapter = adapter
        mRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val manager = recyclerView.layoutManager as LinearLayoutManager
                val position = manager.findFirstVisibleItemPosition()
                val pinyin = adapter.list[position].pinyin
                val sub = if (pinyin.isEmpty()) {
                    "#"
                } else {
                    pinyin.substring(0, 1)
                }
                mIndexBar.setCurrentChar(sub.toUpperCase())
            }
        })

        mIndexBar.setCurrentChar("A")
        mIndexBar.setListener(object : IndexBar.IndexBarListener {
            override fun touchListener(
                str: String,
                isShowPreView: Boolean,
                pointF: PointF?
            ) {
                Log.d("123===", "位置信息=$pointF")
                if (isShowPreView) {
                    mTv.visibility = View.VISIBLE
                    mTv.text = str
                    mTv.translationY = pointF!!.y - mTv.height / 2
                    for ((index, value) in adapter.list.withIndex()) {
                        if (value.pinyin.toUpperCase().startsWith(str)) {
                            //滑动到指定位置
                            val manager = mRv.layoutManager as LinearLayoutManager
                            manager.scrollToPositionWithOffset(index, 0)
                            break
                        }
                    }
                } else {
                    mTv.visibility = View.INVISIBLE
                }
            }
        })
    }
}