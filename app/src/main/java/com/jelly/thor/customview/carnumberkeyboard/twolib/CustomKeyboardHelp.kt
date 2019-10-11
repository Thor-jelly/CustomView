package com.jelly.thor.customview.carnumberkeyboard.twolib

import android.content.Context
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.NestedScrollView
import com.jelly.thor.customview.R
import java.lang.reflect.InvocationTargetException

/**
 * 类描述：自定义键盘<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/10/8 18:57 <br/>
 */
class CustomKeyboardHelp(
    /**
     * 上线文
     */
    private val mContext: Context,
    /**
     * 键盘布局
     */
    @LayoutRes private val mKeyboardLayoutId: Int,
    /**
     * keyboardViewId
     */
    private val mKeyboardViewId: Int,
    /**
     * 键盘父布局，键盘必须要一个父布局包裹
     */
    private val mKeyboardParentView: ViewGroup,
    /**
     * 除了键盘布局的view
     */
    private val mOtherView: ViewGroup
) {
    /**
     * 可以设置键盘类型，默认车牌号键盘
     */
    var mKeyboardTypeEnum = KeyboardTypeEnum.CAR_NUMBER_PROVINCE
        set(value) {
            field = value
            when (value) {
                KeyboardTypeEnum.CAR_NUMBER_PROVINCE -> {
                    //切换为 车牌键盘
                    mKeyboardView.keyboard = mCarNumberProvinceKeyboard
                }
                KeyboardTypeEnum.NUMBER_AND_LETTER -> {
                    //切换为 数字字母键盘
                    mKeyboardView.keyboard = mNumberAndLettersKeyboard
                }
                KeyboardTypeEnum.SYSTEM -> {
                    //切换为系统个键盘,不需要操作
                }
            }
        }

    /**
     * 提供set方法
     * 始值是否显示预览
     */
    var mKeyboardIsPreviewEnabled = false
        set(value) {
            mKeyboardView.isPreviewEnabled = value
            field = value
        }

    /**
     * 如果是nsv需要记录scrollY距离
     */
    private var mScrollY: Int = 0
    /**
     * nsv高度
     */
    private var mNsvHeight: Int = 0
    /**
     * 当前et居nsv底部距离
     */
    private var mKeyboardEt2Bottom: Int = 0
    /**
     * 自定义键盘高度
     */
    private var mKeyboardViewHeight: Int = 0

    /**
     * 其他布局是否设置了底部padding
     */
    private var mCurrentOtherViewIsSetBottom = false

    /**
     * 当前et
     */
    private lateinit var mCurrentKeyboardEt: AppCompatEditText

    /**
     * 数字字母键盘布局
     */
    private val mNumberAndLettersKeyboard by lazy {
        Keyboard(mContext, R.xml.number_and_letters)
    }
    /**
     * 车牌号键盘布局
     */
    private val mCarNumberProvinceKeyboard by lazy {
        Keyboard(mContext, R.xml.car_number_province)
    }

    /**
     * 添加进 键盘父布局中 自定义布局的父布局
     */
    private val mKeyboardInflaterParentView by lazy {
        LayoutInflater.from(mContext).inflate(
            mKeyboardLayoutId,
            mKeyboardParentView,
            true
        )
    }

    private val mKeyboardView by lazy {
        //第一次初始化隐藏
        mKeyboardInflaterParentView.visibility = View.GONE
        val keyboardView = mKeyboardInflaterParentView.findViewById<KeyboardView>(mKeyboardViewId)
        //添加键盘
        keyboardView.keyboard = mCarNumberProvinceKeyboard
        //设置是否预览
        keyboardView.isPreviewEnabled = mKeyboardIsPreviewEnabled
        //添加监听
        keyboardView.setOnKeyboardActionListener(object : KeyboardView.OnKeyboardActionListener {
            @Suppress("SpellCheckingInspection")
            override fun onKey(primaryCode: Int, keyCodes: IntArray) {
                if (!::mCurrentKeyboardEt.isInitialized) {
                    throw RuntimeException("请先调用bind方法！")
                }

                val editable = mCurrentKeyboardEt.text ?: return
                val start = mCurrentKeyboardEt.selectionStart
                val end = mCurrentKeyboardEt.selectionEnd
                when (primaryCode) {
                    Keyboard.KEYCODE_DONE ->
                        //完成
                        hintKeyboardView()
                    Keyboard.KEYCODE_CANCEL ->
                        //隐藏键盘
                        hintKeyboardView()
                    Keyboard.KEYCODE_DELETE -> {
                        //回退键，删除字符
                        if (editable.isNotEmpty()) {
                            if (start == end) { //光标开始和结束位置相同, 即没有选中内容
                                if (start <= 0) {
                                    return
                                }
                                editable.delete(start - 1, start)
                            } else { //光标开始和结束位置不同, 即选中EditText中的内容
                                editable.delete(start, end)
                            }
                        }
                        if (keyboardView is CarNumberKeyboardView) {
                            //如果按键可以重复触发，这里有问题
                            //车牌号键盘，如果被清空或者删除第一个字符即中文 切换为中文键盘
                            if (editable.isEmpty() || start == 1) {
                                switchChangeKeyboard(KeyboardTypeEnum.NUMBER_AND_LETTER)
                            }
                        }
                    }
                    Keyboard.KEYCODE_MODE_CHANGE -> {
                        //键盘切换
                        if (keyboardView is CarNumberKeyboardView) {
                            if (editable.isEmpty() && KeyboardTypeEnum.CAR_NUMBER_PROVINCE == mKeyboardTypeEnum) {
                                return
                            }
                        }
                        switchChangeKeyboard(mKeyboardTypeEnum)
                    }
                    else -> {
                        // 输入键盘值
                        // editable.insert(start, Character.toString((char) primaryCode));
                        //当前按键值
                        val pressKeyStr = primaryCode.toChar().toString()
                        if (keyboardView is CarNumberKeyboardView) {
                            //当前输入框值 + 按键值
                            val nowInputStr = with(editable) {
                                val oldEtStr = toString()
                                val oldEtStrLength = oldEtStr.length
                                when {
                                    start == 0 -> {
                                        //输入在最前
                                        pressKeyStr + oldEtStr
                                    }
                                    oldEtStrLength == start -> {
                                        //输入在最后
                                        oldEtStr + pressKeyStr
                                    }
                                    else -> {
                                        //输入在中间
                                        oldEtStr.substring(
                                            0,
                                            start
                                        ) + pressKeyStr + oldEtStr.subSequence(
                                            start,
                                            editable.length
                                        )
                                    }
                                }
                            }
                            val provinceRegx =
                                "[京|津|渝|沪|冀|晋|辽|吉|黑|苏|浙|皖|闽|赣|鲁|豫|鄂|湘|粤|琼|川|贵|云|陕|甘|青|蒙|桂|宁|新|藏|港|澳]"
                            val lettersRegx = "Q|W|E|R|T|Y|U|I|O|P|A|S|D|F|G|H|J|K|L|Z|X|C|V|B|N|M"
                            val numberAndLettersRegx =
                                "[$lettersRegx][1|2|3|4|5|6|7|8|9|0|$lettersRegx|挂]"
                            val allRegx = "^${provinceRegx}(${numberAndLettersRegx}*)*$"
                            //判断如果是输入开始为中文，并且是第一位 切换为数字 字母键盘
                            if (editable.isEmpty()) {
                                if (nowInputStr.contains(provinceRegx.toRegex())) {
                                    switchChangeKeyboard(KeyboardTypeEnum.CAR_NUMBER_PROVINCE)
                                } else {
                                    //如果输入数字字母不允许填入
                                    return
                                }
                            } else if (editable.length == 1) {
                                //第二位只能输入英文
                                if (!nowInputStr.contains("[$lettersRegx]".toRegex())) {
                                    return
                                }
                                if (mKeyboardTypeEnum == KeyboardTypeEnum.CAR_NUMBER_PROVINCE) {
                                    //切换为 数字 字母键盘
                                    switchChangeKeyboard(KeyboardTypeEnum.CAR_NUMBER_PROVINCE)
                                }
                            } else {
                                if (!nowInputStr.contains(allRegx.toRegex())) {
                                    return
                                }
                                if (mKeyboardTypeEnum == KeyboardTypeEnum.CAR_NUMBER_PROVINCE) {
                                    //切换为 数字 字母键盘
                                    switchChangeKeyboard(KeyboardTypeEnum.CAR_NUMBER_PROVINCE)
                                }
                            }
                        }
                        editable.replace(start, end, pressKeyStr)
                    }
                }
            }

            override fun onPress(primaryCode: Int) {
                if (mKeyboardIsPreviewEnabled) {
                    when (primaryCode) {
                        Keyboard.KEYCODE_DONE,
                        Keyboard.KEYCODE_CANCEL,
                        Keyboard.KEYCODE_DELETE,
                        Keyboard.KEYCODE_MODE_CHANGE -> {
                            keyboardView.isPreviewEnabled = false
                        }
                        else -> {
                            keyboardView.isPreviewEnabled = true
                        }
                    }
                }
            }

            override fun swipeRight() {
            }

            override fun onRelease(primaryCode: Int) {
            }

            override fun swipeLeft() {
            }

            override fun swipeUp() {
            }

            override fun swipeDown() {
            }

            override fun onText(text: CharSequence?) {
            }

        })
        keyboardView
    }

    /**
     * 切换接盘
     */
    private fun switchChangeKeyboard(oldKeyboardTypeEnum: KeyboardTypeEnum) {
        mKeyboardTypeEnum = when (oldKeyboardTypeEnum) {
            KeyboardTypeEnum.NUMBER_AND_LETTER -> {
                //原数字字母键盘 切换为 车牌键盘
                KeyboardTypeEnum.CAR_NUMBER_PROVINCE
            }
            KeyboardTypeEnum.CAR_NUMBER_PROVINCE -> {
                //原车牌键盘 切换为 数字字母键盘
                KeyboardTypeEnum.NUMBER_AND_LETTER
            }
            KeyboardTypeEnum.SYSTEM -> {
                //系统键盘
                KeyboardTypeEnum.SYSTEM
            }
        }
    }


    init {
        //默认初始化值为 数字 车牌号键盘
        mKeyboardTypeEnum = KeyboardTypeEnum.CAR_NUMBER_PROVINCE
        //初始化键盘
        mKeyboardView.isEnabled = true

        //如果otherView是nsv 需要记录其滚动
        if (mOtherView is NestedScrollView) {
            mOtherView.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
                mScrollY = scrollY
                mNsvHeight = mOtherView.height
//                mKeyboardEt2Bottom = mCurrentKeyboardEt.bottom
//                mKeyboardView.post {
//                     mKeyboardViewHeight = mKeyboardView.height
//                }

                /*Log.e("123===", "scrollY = " + scrollY);
                Log.e("123===", "mNsv.getHeight() = " + mNsv.getHeight());
                Log.e("123===", "mKeyboardEt.getBottom() = " + mKeyboardEt.getBottom());
                mKeyboardView.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("123===", "mKeyboardView.getHeight() = " + mKeyboardView.getHeight());
                    }
                });*/
            }
        } else {
            mScrollY = 0
            mNsvHeight = mOtherView.height
        }
    }

    fun bind(et: AppCompatEditText) {
        mCurrentKeyboardEt = et

        //输入框焦点监听
        mCurrentKeyboardEt.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                showKeyboardView(v)
            } else {
                hintKeyboardView()
            }
        }
    }

    /**
     * 显示键盘
     */
    private fun showKeyboardView(
        v: View
    ) {

        when (mKeyboardTypeEnum) {
            KeyboardTypeEnum.SYSTEM -> {
                //显示系统键盘
                showSystemKeyBoard(mContext as AppCompatActivity)
            }
            else -> {
                //显示自定义键盘
                mKeyboardEt2Bottom = v.bottom

                hideSystemKeyBoard(mContext as AppCompatActivity, mCurrentKeyboardEt)
                if (mKeyboardInflaterParentView.visibility != View.VISIBLE) {
                    mKeyboardInflaterParentView.visibility = View.VISIBLE
                }
                mKeyboardInflaterParentView.post {
                    mKeyboardViewHeight = mKeyboardInflaterParentView.height

                    if (mOtherView is NestedScrollView) {
                        if (!mCurrentOtherViewIsSetBottom) {
                            mCurrentOtherViewIsSetBottom = true
                        }
                        mOtherView.setPadding(
                            mOtherView.paddingStart,
                            mOtherView.paddingTop,
                            mOtherView.paddingEnd,
                            mOtherView.paddingBottom + mKeyboardViewHeight
                        )
                        if (mScrollY + mNsvHeight - mKeyboardEt2Bottom - mKeyboardViewHeight < 0) {
                            mOtherView.smoothScrollBy(0, mKeyboardViewHeight)
                        }
                    } else {
                        if (mScrollY + mNsvHeight - mKeyboardEt2Bottom - mKeyboardViewHeight < 0) {
                            val p = mOtherView.layoutParams as ViewGroup.MarginLayoutParams
                            p.topMargin = p.topMargin - mKeyboardViewHeight
                            mOtherView.layoutParams = p

                            if (!mCurrentOtherViewIsSetBottom) {
                                mCurrentOtherViewIsSetBottom = true
                            }
                        } else {
                            if (mCurrentOtherViewIsSetBottom) {
                                mCurrentOtherViewIsSetBottom = false
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 对外提供隐藏自定义键盘方法
     * 隐藏自定义键盘方法
     */
    fun hintKeyboardView() {
        when (mKeyboardTypeEnum) {
            KeyboardTypeEnum.SYSTEM -> {
                hideSystemKeyBoard(mContext as AppCompatActivity, mCurrentKeyboardEt)
            }
            else -> {
                if (mKeyboardInflaterParentView.visibility == View.GONE) {
                    return
                }

                if (!::mCurrentKeyboardEt.isInitialized) {
                    throw RuntimeException("请先调用bind方法！")
                }

                mKeyboardInflaterParentView.visibility = View.GONE
                if (mCurrentOtherViewIsSetBottom) {
                    mCurrentOtherViewIsSetBottom = false
                    mKeyboardInflaterParentView.post {
                        if (mOtherView is NestedScrollView) {
                            mOtherView.setPadding(
                                mOtherView.paddingStart,
                                mOtherView.paddingTop,
                                mOtherView.paddingEnd,
                                mOtherView.paddingBottom - mKeyboardViewHeight
                            )
                        } else {
                            val p = mOtherView.layoutParams as ViewGroup.MarginLayoutParams
                            p.topMargin = p.topMargin + mKeyboardViewHeight
                            mOtherView.layoutParams = p
                        }
                    }
                }

                mCurrentKeyboardEt.clearFocus()
                mCurrentKeyboardEt.isSelected = false
            }
        }
    }

    private fun showSystemKeyBoard(activity: AppCompatActivity) {
        val imm =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(mCurrentKeyboardEt, 0);//显示软键盘
    }

    /**
     * 关闭软键盘
     */
    private fun hideSystemKeyBoard(activity: AppCompatActivity, vararg editTexts: EditText) {
        val imm =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        for (ed in editTexts) {
            if (imm.isActive(ed)) {  //i(imm.isActive())  //一直是true
                //imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);//显示或隐藏软键盘
                imm.hideSoftInputFromWindow(ed.windowToken, 0)//隐藏软键盘
                //imm.showSoftInput(myEditText, 0);//显示软键盘
                /* mOrderTimeLl.requestFocus();
                mOrderTimeLl.setFocusableInTouchMode(true);*/
                //                ed.clearFocus();
                //                ed.setSelected(false);

                val currentVersion = Build.VERSION.SDK_INT
                var methodName: String? = null
                if (currentVersion >= 16) {
                    methodName = "setShowSoftInputOnFocus"
                } else if (currentVersion >= 14) {
                    methodName = "setSoftInputShownOnFocus"
                }

                if (methodName == null) {
                    ed.inputType = 0
                } else {
                    try {
                        val setShowSoftInputOnFocus =
                            EditText::class.java.getMethod(methodName, java.lang.Boolean.TYPE)
                        setShowSoftInputOnFocus.isAccessible = true
                        setShowSoftInputOnFocus.invoke(ed, java.lang.Boolean.FALSE)
                    } catch (e: NoSuchMethodException) {
                        ed.inputType = 0
                        e.printStackTrace()
                    } catch (e: IllegalAccessException) {
                        e.printStackTrace()
                    } catch (e: InvocationTargetException) {
                        e.printStackTrace()
                    } catch (e: IllegalArgumentException) {
                        e.printStackTrace()
                    }

                }
            }
        }
    }
}