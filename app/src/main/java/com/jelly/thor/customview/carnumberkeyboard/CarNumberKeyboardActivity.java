package com.jelly.thor.customview.carnumberkeyboard;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.jelly.thor.customview.R;

/**
 * 类描述：车牌键盘 <br/>
 * 参考：
 * https://android.googlesource.com/platform/development/+/master/samples/SoftKeyboard/
 * <p>
 * https://blog.csdn.net/dgs960825/article/details/50344743
 * https://blog.csdn.net/flueky/article/details/80088255
 * https://github.com/parkingwang/vehicle-keyboard-android
 * https://github.com/StomHong/CustomizeKeyboard
 * https://github.com/SValence/SafeKeyboard
 * <p>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/8/29 17:28 <br/>
 */
public class CarNumberKeyboardActivity extends AppCompatActivity {
    private AppCompatEditText mKeyboardEt;
    private KeyboardView mKeyboardView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_number_keyboard);

        mKeyboardEt = (AppCompatEditText) findViewById(R.id.keyboard_et);

        mKeyboardView = (KeyboardView) findViewById(R.id.keyboard_view);
        /////////////////////////////////////////////////
        Keyboard keyboard = new Keyboard(this, R.xml.number_and_letters);
        mKeyboardView.setKeyboard(keyboard);
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
                mKeyboardEt.setFocusableInTouchMode(true);

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

    private void towLib() {
//        KeyboardView keyboardView = new KeyboardView(KeyboardView.getENG(), null, this);
//        MyKeyboard myKeyboard = new MyKeyboard(keyboardView);
//        myKeyboard.bindEt(mKeyboardEt, this);
    }


}
