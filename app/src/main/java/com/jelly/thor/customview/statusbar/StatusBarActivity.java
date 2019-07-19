package com.jelly.thor.customview.statusbar;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.jelly.thor.customview.R;

/**
 * 类描述：状态栏<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/7/19 09:40 <br/>
 */
public class StatusBarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_bar);
//        StatusBarUtils.setStatusBarColor(this, Color.RED);
        StatusBarUtils.setFullActivity(this);
    }
}
