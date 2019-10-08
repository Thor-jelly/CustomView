package com.jelly.thor.customview.carnumberkeyboard;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.widget.NestedScrollView;

import com.jelly.thor.customview.R;
import com.jelly.thor.customview.carnumberkeyboard.twolib.KeyboardTypeEnum;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 类描述：车牌键盘 <br/>
 * 参考：
 * https://android.googlesource.com/platform/development/+/master/samples/SoftKeyboard/
 * <p>
 * Android自定义安全键盘 https://www.cnblogs.com/sixrain/p/7793610.html
 * https://blog.csdn.net/dgs960825/article/details/50344743
 * 自定义Android键盘 https://blog.csdn.net/flueky/article/details/80088255
 * https://github.com/24Kshign/NumberKeyboard
 * 停车王车牌键盘-Android https://github.com/parkingwang/vehicle-keyboard-android
 * 完全自定义的Android安全键盘 https://github.com/StomHong/CustomizeKeyboard
 * Android自定义安全软键盘，完全自定义，方便、安全、可靠 https://github.com/SValence/SafeKeyboard
 * <p>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/8/29 17:28 <br/>
 */
public class CarNumberKeyboardActivity extends AppCompatActivity {
    private AppCompatEditText mKeyboardEt;
    private KeyboardView mKeyboardView;
    private NestedScrollView mNsv;

    /**
     * 当前键盘选中类型
     */
    private KeyboardTypeEnum mKeyboardTypeEnum = KeyboardTypeEnum.NUMBER_AND_LETTER;

    private int mScrollY;
    private int mNsvHeight;
    private int mKeyboardEt2Bottom;
    private int mKeyboardViewHeight;

    /**
     * 数字字母键盘
     */
    private Keyboard mNumberAndLettersKeyboard;
    /**
     * 车牌号键盘
     */
    private Keyboard mCarNumberProvince;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_number_keyboard);

        mKeyboardEt = (AppCompatEditText) findViewById(R.id.keyboard_et);

        mNsv = (NestedScrollView) findViewById(R.id.nsv);



        mKeyboardView = (KeyboardView) findViewById(R.id.keyboard_view);

        mKeyboardEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hideSystemKeyBoard(CarNumberKeyboardActivity.this, mKeyboardEt);
                    mKeyboardView.setVisibility(View.VISIBLE);
                    mKeyboardView.post(new Runnable() {
                        @Override
                        public void run() {
                            mKeyboardViewHeight = mKeyboardView.getHeight();

                            mNsv.setPadding(mNsv.getPaddingStart(), mNsv.getPaddingTop(), mNsv.getPaddingEnd(), mNsv.getPaddingBottom() + mKeyboardViewHeight);
                            if (mScrollY + mNsvHeight - mKeyboardEt2Bottom - mKeyboardViewHeight < 0) {
                                mNsv.smoothScrollBy(0, mKeyboardViewHeight);
                            }
                        }
                    });
                } else {
                    hintKeyboardView();
                }
            }
        });

        mNsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                mScrollY = scrollY;
                mNsvHeight = mNsv.getHeight();
                mKeyboardEt2Bottom = mKeyboardEt.getBottom();
                mKeyboardView.post(new Runnable() {
                    @Override
                    public void run() {
                        mKeyboardViewHeight = mKeyboardView.getHeight();
                    }
                });

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
        });

        /////////////////////////////////////////////////
        mNumberAndLettersKeyboard = new Keyboard(this, R.xml.number_and_letters);
        mCarNumberProvince = new Keyboard(this, R.xml.car_number_province);


        mKeyboardView.setKeyboard(mNumberAndLettersKeyboard);
        mKeyboardView.setEnabled(true);
        mKeyboardView.setPreviewEnabled(true);
        mKeyboardView.setOnKeyboardActionListener(new KeyboardView.OnKeyboardActionListener() {
            @Override
            public void onPress(int primaryCode) {

            }

            @Override
            public void onRelease(int primaryCode) {

            }

            @Override
            public void onKey(int primaryCode, int[] keyCodes) {
                Editable editable = mKeyboardEt.getText();
                if (editable == null) {
                    return;
                }
                int start = mKeyboardEt.getSelectionStart();
                int end = mKeyboardEt.getSelectionEnd();
                switch (primaryCode) {
                    case Keyboard.KEYCODE_CANCEL:
                        //隐藏键盘
                        hintKeyboardView();
                        break;
                    case Keyboard.KEYCODE_DELETE:
                        //回退键，删除字符
                        if (editable.length() > 0) {
                            if (start == end) { //光标开始和结束位置相同, 即没有选中内容
                                editable.delete(start - 1, start);
                            } else { //光标开始和结束位置不同, 即选中EditText中的内容
                                editable.delete(start, end);
                            }
                        }
                        break;
                    case Keyboard.KEYCODE_MODE_CHANGE:
                        //键盘切换
                        switch (mKeyboardTypeEnum) {
                            case NUMBER_AND_LETTER:
                                //原数字字母键盘 切换为 车牌键盘
                                mKeyboardTypeEnum = KeyboardTypeEnum.CAR_NUMBER_PROVINCE;
                                mKeyboardView.setKeyboard(mCarNumberProvince);
                                break;
                            case CAR_NUMBER_PROVINCE:
                                //原车牌键盘 切换为 数字字母键盘
                                mKeyboardTypeEnum = KeyboardTypeEnum.NUMBER_AND_LETTER;
                                mKeyboardView.setKeyboard(mNumberAndLettersKeyboard);
                                break;
                        }
                        break;
                    default:
                        // 输入键盘值
                        // editable.insert(start, Character.toString((char) primaryCode));
                        editable.replace(start, end, Character.toString((char) primaryCode));
                        break;
                }
            }

            @Override
            public void onText(CharSequence charSequence) {

            }

            @Override
            public void swipeLeft() {

            }

            @Override
            public void swipeRight() {

            }

            @Override
            public void swipeDown() {

            }

            @Override
            public void swipeUp() {

            }
        });


        //towLib();
    }

    private void hintKeyboardView() {
        mKeyboardView.setVisibility(View.GONE);
        mKeyboardView.post(new Runnable() {
            @Override
            public void run() {
                mNsv.setPadding(mNsv.getPaddingStart(), mNsv.getPaddingTop(), mNsv.getPaddingEnd(), mNsv.getPaddingBottom() - mKeyboardViewHeight);
            }
        });
    }

    /**
     * 关闭软键盘
     */
    private static void hideSystemKeyBoard(AppCompatActivity activity, EditText... editTexts) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        for (EditText ed : editTexts) {
            if (imm.isActive(ed)) {  //i(imm.isActive())  //一直是true
                //imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);//显示或隐藏软键盘
                imm.hideSoftInputFromWindow(ed.getWindowToken(), 0);//隐藏软键盘
                //imm.showSoftInput(myEditText, 0);//显示软键盘
                /* mOrderTimeLl.requestFocus();
                mOrderTimeLl.setFocusableInTouchMode(true);*/
//                ed.clearFocus();
//                ed.setSelected(false);

                int currentVersion = Build.VERSION.SDK_INT;
                String methodName = null;
                if (currentVersion >= 16) {
                    methodName = "setShowSoftInputOnFocus";
                } else if (currentVersion >= 14) {
                    methodName = "setSoftInputShownOnFocus";
                }

                if (methodName == null) {
                    ed.setInputType(0);
                } else {
                    try {
                        Method setShowSoftInputOnFocus = EditText.class.getMethod(methodName, Boolean.TYPE);
                        setShowSoftInputOnFocus.setAccessible(true);
                        setShowSoftInputOnFocus.invoke(ed, Boolean.FALSE);
                    } catch (NoSuchMethodException e) {
                        ed.setInputType(0);
                        e.printStackTrace();
                    } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    private void towLib() {
//        KeyboardView keyboardView = new KeyboardView(KeyboardView.getENG(), null, this);
//        MyKeyboard myKeyboard = new MyKeyboard(keyboardView);
//        myKeyboard.bindEt(mKeyboardEt, this);
    }


}
