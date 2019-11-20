package com.jelly.thor.customview.parallaxanimator

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.os.Bundle
import android.os.Environment
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.LayoutInflaterCompat
import androidx.core.view.LayoutInflaterFactory
import androidx.fragment.app.Fragment
import java.io.File

/**
 * 类描述：视差1   <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/11/14 14:39 <br/>
 */
class Parallax01Fragment() : Fragment(), LayoutInflater.Factory2 {

    override fun onCreateView(name: String?, context: Context?, attrs: AttributeSet?): View? {
        return this.onCreateView(null, name, context, attrs)
    }

    override fun onCreateView(
        parent: View?,
        name: String?,
        context: Context?,
        attrs: AttributeSet?
    ): View? {
        //view都会来这里，创建view

        //创建AssetManager,构造方法被隐藏了，因此通过反射调取
        val assetManager = AssetManager::class.java.newInstance()
        //添加本地下载好的资源皮肤路径 assetManager.addAssetPath 调用不了 也只能反射
        val method = AssetManager::class.java.getDeclaredMethod("addAssetPath", String::class.java)
        //method.isAccessible = true //私有的需要设置
        val skinPathStr = Environment.getExternalStorageDirectory().absolutePath +
                File.separator +
                "xxx.skin"
        method.invoke(assetManager, skinPathStr)
        val resource = Resources(assetManager, resources.displayMetrics, resources.configuration)
        //获取资源
        val drawableId = resource.getIdentifier("image_src", "drawable","com.jelly.thor.xxx")
        val drawable = resource.getDrawable(drawableId)


        return null
    }

    companion object {
        fun newInstance(layoutId: Int): Parallax01Fragment {
            val fragment = Parallax01Fragment()
            val arguments = Bundle()
            arguments.putInt("layoutId", layoutId)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutId = arguments!!.getInt("layoutId")
        //view创建的时候，解析属性
        //inflater全局是单例，会导致以后所有view创建都会来这里
        LayoutInflaterCompat.setFactory2(inflater, this)

        return inflater.inflate(layoutId, container, false)
    }
}