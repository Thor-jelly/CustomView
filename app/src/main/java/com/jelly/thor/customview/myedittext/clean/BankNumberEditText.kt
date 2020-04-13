package com.jelly.thor.customview.myedittext.clean

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet


/**
 * 类描述：银行卡号输入框 四位一格<br/>
 * 参考：https://blog.csdn.net/lintcgirl/article/details/50358421
 * 创建人：吴冬冬<br/>
 * 创建时间：2020/4/13 10:49 <br/>
 */
class BankNumberEditText : CleanEditText {
    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    private val inputRule = " "

    init {
        initEt()
    }

    private fun initEt() {
        val textWatch = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                Log.d(
//                    "123===",
//                    "text=${text}--start=${start}--before=${before}--count=${count}"
//                )
                if (s == null) return

                //是否在中间输入
                val isMiddleInput = (start + count) <= s.length
//                Log.d(
//                    "123===",
//                    "是否在中间输入=${isMiddleInput}"
//                )
                //是否在末尾加入空客
                var isNeedEndAddSpace = false
                if (!isMiddleInput && s.isNotEmpty() && s.length % 5 == 4) {
                    isNeedEndAddSpace = true
                }
//                Log.d(
//                    "123===",
//                    "是否末尾需要添加空格=${isNeedEndAddSpace}"
//                )

                if (isMiddleInput || isNeedEndAddSpace) {
                    val newStr = s.toString().replace(inputRule, "")
                    val sb = StringBuilder()
                    for (i in newStr.indices step 4) {
                        if (i > 0) {
                            sb.append(inputRule)
                        }
                        if (i + 4 <= newStr.length) {
                            sb.append(newStr.substring(i, i + 4))
                        } else {
                            sb.append(newStr.substring(i, newStr.length))
                        }
                    }
                    removeTextChangedListener(this)
                    setText(sb.toString())
                    if (!isMiddleInput || count > 1) {
                        //如果在末尾的话，或者加入字符数大于0的情况
                        //设置指针在末尾
                        setSelection(sb.length)
                    } else if (isMiddleInput) {
                        if (count == 0) {
                            //删除情况
                            val index = start /*- before + before*///剪切特殊情况
                            val newIndex =
                                if (index < 0) 0 else if (index > sb.length) sb.length else index
                            setSelection(newIndex)
                        } else {
                            //增加时，如果光标在空格前，光标移动到添加数字后
                            if ((start - before + count) % 5 == 0) {
                                setSelection(if ((start + count - before + 1) < sb.length) (start + count - before + 1) else sb.length)
                            } else {
                                setSelection(start + count - before)
                            }
                        }
                    }
                    addTextChangedListener(this)
                }
            }
        }
        addTextChangedListener(textWatch)
    }
}