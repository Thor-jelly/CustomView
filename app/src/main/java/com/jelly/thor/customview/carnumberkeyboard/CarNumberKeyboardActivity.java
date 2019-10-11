package com.jelly.thor.customview.carnumberkeyboard;

import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.jelly.thor.customview.R;
import com.jelly.thor.customview.carnumberkeyboard.twolib.CustomKeyboardHelp;
import com.jelly.thor.customview.carnumberkeyboard.twolib.KeyboardTypeEnum;

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
    private ViewGroup mNsv;

    /**
     * 当前键盘选中类型
     */
    private KeyboardTypeEnum mKeyboardTypeEnum = KeyboardTypeEnum.NUMBER_AND_LETTER;


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

        mNsv = findViewById(R.id.nsv);

        LinearLayout keyboardParentView = (LinearLayout) findViewById(R.id.keyboard_parent_view);

        CustomKeyboardHelp customKeyboard = new CustomKeyboardHelp(this,
                R.layout.activity_car_number_keyboardview,
                R.id.keyboard_view,
                keyboardParentView,
                mNsv
        );
        customKeyboard.setMKeyboardIsPreviewEnabled(true);
        customKeyboard.bind(mKeyboardEt);
    }

}
