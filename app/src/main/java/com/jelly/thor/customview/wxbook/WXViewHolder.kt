package com.jelly.thor.customview.wxbook

import android.util.SparseArray
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/11/19 18:52 <br/>
 */
open class WXViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val sa = SparseArray<View>()

    @Suppress("UNCHECKED_CAST")
    fun <T : View> getView(id: Int): T {
        return if (sa.indexOfKey(id) < 0) {
            val findView = itemView.findViewById<T>(id)
            sa.append(id, findView)
            findView
        } else {
            sa.get(id) as T
        }
    }
}