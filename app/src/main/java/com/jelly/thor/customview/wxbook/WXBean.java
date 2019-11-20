package com.jelly.thor.customview.wxbook;

import java.util.Comparator;

/**
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/11/19 19:10 <br/>
 */
public abstract class WXBean implements Comparable<WXBean> {
    /**
     * 第一个字的汉字转拼音
     */
    private String pinyin = "";

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public abstract String getZh2pinyin();

    @Override
    public int compareTo(WXBean o) {
        int nowLength = this.pinyin.length();
        int oldLength = o.pinyin.length();
        if (nowLength == 0) {
            return 1;
        }
        if (oldLength == 0) {
            return -1;
        }
        if (this.pinyin.equals("#")) {
            return 1;
        }
        if (o.pinyin.equals("#")) {
            return -1;
        }
        return this.pinyin.compareTo(o.pinyin);
    }
}
