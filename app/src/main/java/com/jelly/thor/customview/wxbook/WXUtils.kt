package com.jelly.thor.customview.wxbook

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.promeg.pinyinhelper.Pinyin

/**
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/11/21 15:44 <br/>
 */
object WXUtils {
    @JvmStatic
    fun <T : WXBean> sort(list: MutableList<T>) {
        if (list.isEmpty()) {
            return
        }
        for (value in list) {
            val zh2pinyin = value.zh2pinyin
            if (zh2pinyin.isEmpty()) {
                value.pinyin = "#"
            } else {
                val toUpperCase = Pinyin.toPinyin(zh2pinyin, "").toUpperCase()
                if (toUpperCase.matches("[A-Za-z].*".toRegex())) {
                    value.pinyin = toUpperCase
                } else {
                    value.pinyin = "#"
                }
            }
        }
        list.sort()
    }

    /**
     * 获取rv可见数据中第一个的首字母
     */
    @JvmStatic
    fun <T : WXBean> getRvFistVisiblePinyinFirstChar(
        rv: RecyclerView,
        adapter: WXBaseAdapter<T>
    ): String {
        return adapter.list.let {
            //判断当前显示第一个是否为侧边索引字母，如果不是更改所有选中字母
            return if (adapter.list.isEmpty()) {
                ""
            } else {
                val manager = rv.layoutManager as LinearLayoutManager
                val position = manager.findFirstVisibleItemPosition()
                getPinyinFirstChar(adapter.list[position])
            }
        }
    }

    /**
     * 获取拼音首字母
     */
    @JvmStatic
    fun getPinyinFirstChar(
        bean: WXBean?
    ): String {
        return bean?.let {
            //判断当前显示第一个是否为侧边索引字母，如果不是更改所有选中字母
            val pinyin = bean.pinyin
            if (pinyin.isEmpty()) {
                "#"
            } else {
                pinyin.substring(0, 1).toUpperCase()
            }
        } ?: "#"
    }
}