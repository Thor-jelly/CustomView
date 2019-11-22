package com.jelly.thor.customview.wxbook

import android.content.Context
import android.graphics.Color
import android.graphics.PointF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jelly.thor.customview.R
import com.jelly.thor.customview.wxbook.test.TestBean

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
        findViewById<RecyclerView>(R.id.wx_rv)
    }

    private val mIndexBar by lazy {
        findViewById<IndexBar>(R.id.index_bar)
    }

    private val mTv by lazy {
        findViewById<TextView>(R.id.wx_preview)
    }

    private var mTitleColor = Color.RED
    private var mTopTitleTextColor = Color.GREEN
    private var mTitleTextColor = Color.WHITE
    private var mTitleHeight = 0F
    private var mTitleTextMarginStar = 0F
    private var mTitleTextSize = 0F

    private var mBarTextSize = dp2px(11)
    private var mBarTextNoSelectColor = Color.BLACK
    private var mBarTextSelectColor = Color.GRAY
    private var mBarTextCircleSelectColor = Color.RED
    private var mBarIsVibrator = true

    private var mPreviewTextSize = dp2px(30)

    init {
        View.inflate(context, R.layout.wx_book_view, this)

        val array = context?.obtainStyledAttributes(attrs, R.styleable.WXBookView)
        array?.let {
            mTitleColor = array.getColor(
                R.styleable.WXBookView_wxbookview_title_color,
                Color.RED
            )
            mTopTitleTextColor = array.getColor(
                R.styleable.WXBookView_wxbookview_top_title_text_color,
                Color.GREEN
            )
            mTitleTextColor = array.getColor(
                R.styleable.WXBookView_wxbookview_title_text_color,
                Color.WHITE
            )
            mTitleHeight = array.getDimension(
                R.styleable.WXBookView_wxbookview_title_height,
                dp2px(30).toFloat()
            )
            mTitleTextMarginStar = array.getDimension(
                R.styleable.WXBookView_wxbookview_title_text_marginstar,
                dp2px(20).toFloat()
            )
            mTitleTextSize = array.getDimension(
                R.styleable.WXBookView_wxbookview_title_text_size,
                dp2px(15).toFloat()
            )

            mBarTextSize = array.getDimension(
                R.styleable.WXBookView_wxbookview_bar_text_size,
                dp2px(11).toFloat()
            ).toInt()
            mBarTextNoSelectColor = array.getColor(
                R.styleable.WXBookView_wxbookview_bar_text_no_select_color,
                Color.BLACK
            )
            mBarTextSelectColor = array.getColor(
                R.styleable.WXBookView_wxbookview_bar_text_select_color,
                Color.GRAY
            )
            mBarTextCircleSelectColor = array.getColor(
                R.styleable.WXBookView_wxbookview_bar_text_circle_select_color,
                Color.RED
            )
            mBarIsVibrator = array.getBoolean(
                R.styleable.WXBookView_wxbookview_bar_text_vibrator,
                true
            )

            //预览效果属性暂不添加
            array.recycle()
        }

    }

    fun <T : WXBean> setAdapter(adapter: WXBaseAdapter<T>) {
        val rvBuilder =
            WXTitleItemDecoration.Builder().apply {
                setHeight(mTitleHeight.toInt())
                setColor(mTitleColor)
                setTextMarginStar(mTitleTextMarginStar.toInt())
                setTextColor(mTitleTextColor)
                setTextSize(mTitleTextSize.toInt())
                setTopTitleTextColor(mTopTitleTextColor)
            }
        mRv.addItemDecoration(WXTitleItemDecoration(adapter.list, rvBuilder))
        mRv.adapter = adapter
        mRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val sub = WXUtils.getRvFistVisiblePinyinFirstChar(mRv, adapter)
                mIndexBar.setCurrentChar(sub)
            }
        })


        val indexBarBuilder =
            IndexBar.Builder().apply {
                setCurrentChar(WXUtils.getRvFistVisiblePinyinFirstChar(mRv, adapter))
                setTextSize(mBarTextSize)
                setTextSelectColor(mBarTextSelectColor)
                setTextNoSelectColor(mBarTextNoSelectColor)
                setCircleSelectColor(mBarTextCircleSelectColor)
                setIsVibrator(mBarIsVibrator)
            }
        mIndexBar.setBuilder(indexBarBuilder)
        mIndexBar.setListener(object : IndexBar.IndexBarListener {
            override fun touchListener(
                str: String,
                isShowPreView: Boolean,
                pointF: PointF?
            ) {
                //Log.d("123===", "位置信息=$pointF")
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
                    val sub = WXUtils.getRvFistVisiblePinyinFirstChar(mRv, adapter)
                    if (str != sub) {
                        mIndexBar.setCurrentChar(sub)
                    }

                }
            }
        })
    }

    private fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        )
            .toInt()
    }

    fun <T : WXBean> notifyDataSetChanged(adapter: WXBaseAdapter<T>) {
        WXUtils.sort(adapter.list)
        adapter.notifyDataSetChanged()
    }
}