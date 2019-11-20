package com.jelly.thor.customview.wxbook.test;

import com.jelly.thor.customview.wxbook.WXBean;

/**
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/11/19 19:51 <br/>
 */
public class TestBean extends WXBean {
    public String name;

    @Override
    public String getZh2pinyin() {
        return name;
    }
}
