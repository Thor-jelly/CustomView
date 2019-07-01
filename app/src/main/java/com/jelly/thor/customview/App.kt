package com.jelly.thor.customview

import android.app.Application

/**
 * 类描述：Application<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/6/24 17:42 <br/>
 */
class App : Application() {
    companion object {
        @JvmStatic
        lateinit var mApplication: Application
            private set
    }

    init {
        mApplication = this
    }
}