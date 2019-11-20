package com.jelly.thor.customview.parallaxanimator

import android.content.Context
import android.util.AttributeSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager

/**
 * 类描述：视差1 <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/11/14 14:37 <br/>
 */
class Parallax01ViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ViewPager(context, attrs) {
    private val mFragments = mutableListOf<Parallax01Fragment>()


    fun setLayout(fm: FragmentManager, intArrayOf: IntArray) {
        mFragments.clear()
        for (i in intArrayOf) {
            val fragment = Parallax01Fragment.newInstance(i)
            mFragments.add(fragment)
        }

        adapter = ParallaxPagerAdapter(fm)
    }

    private inner class ParallaxPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return mFragments[position]
        }

        override fun getCount(): Int {
            return mFragments.size
        }

    }
}