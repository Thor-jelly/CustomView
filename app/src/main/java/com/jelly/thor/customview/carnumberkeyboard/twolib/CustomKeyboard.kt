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
class CustomKeyboard(
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
     * 提供set方法
     * 可以设置键盘类型，默认车牌号键盘
     */
    var mKeyboardTypeEnum = KeyboardTypeEnum.CAR_NUMBER_PROVINCE

    /**
     * 提供set方法
     * 始值是否显示预览
     */
    var mKeyboardIsPreviewEnabled = false

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
            override fun onKey(primaryCode: Int, keyCodes: IntArray) {
                if (!::mCurrentKeyboardEt.isInitialized) {
                    throw RuntimeException("请先调用bind方法！")
                }

                val editable = mCurrentKeyboardEt.text ?: return
                val start = mCurrentKeyboardEt.selectionStart
                val end = mCurrentKeyboardEt.selectionEnd
                when (primaryCode) {
                    Keyboard.KEYCODE_CANCEL ->
                        //隐藏键盘
                        hintKeyboardView()
                    Keyboard.KEYCODE_DELETE ->
                        //回退键，删除字符
                        if (editable.isNotEmpty()) {
                            if (start == end) { //光标开始和结束位置相同, 即没有选中内容
                                editable.delete(start - 1, start)
                            } else { //光标开始和结束位置不同, 即选中EditText中的内容
                                editable.delete(start, end)
                            }
                        }
                    Keyboard.KEYCODE_MODE_CHANGE ->
                        //键盘切换
                        switchKeyboard(mKeyboardTypeEnum)
                    else ->
                        // 输入键盘值
                        // editable.insert(start, Character.toString((char) primaryCode));
                        editable.replace(start, end, Character.toString(primaryCode.toChar()))
                }
            }

            override fun swipeRight() {
            }

            override fun onPress(primaryCode: Int) {
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
    private fun switchKeyboard(keyboardTypeEnum: KeyboardTypeEnum) {
        when (keyboardTypeEnum) {
            KeyboardTypeEnum.NUMBER_AND_LETTER -> {
                //原数字字母键盘 切换为 车牌键盘
                mKeyboardTypeEnum = KeyboardTypeEnum.CAR_NUMBER_PROVINCE
                mKeyboardView.keyboard = mCarNumberProvinceKeyboard
            }
            KeyboardTypeEnum.CAR_NUMBER_PROVINCE -> {
                //原车牌键盘 切换为 数字字母键盘
                mKeyboardTypeEnum = KeyboardTypeEnum.NUMBER_AND_LETTER
                mKeyboardView.keyboard = mNumberAndLettersKeyboard
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

        mCurrentKeyboardEt.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                mKeyboardEt2Bottom = v.bottom

                //123
                hideSystemKeyBoard(mContext as AppCompatActivity, mCurrentKeyboardEt)
                mKeyboardInflaterParentView.visibility = View.VISIBLE
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
            } else {
                hintKeyboardView()
            }
        }
    }

    /**
     * 对外提供隐藏自定义键盘方法
     * 隐藏自定义键盘方法
     */
    fun hintKeyboardView() {
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