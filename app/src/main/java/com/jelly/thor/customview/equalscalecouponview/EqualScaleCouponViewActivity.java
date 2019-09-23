package com.jelly.thor.customview.equalscalecouponview;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jelly.thor.customview.R;

/**
 * 类描述：优惠券view 按比例缩放，形成在任意手机上效果一致<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/9/6 10:38 <br/>
 */
public class EqualScaleCouponViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equal_scale_coupon_view);

        final LinearLayout testClParentLl = findViewById(R.id.test_cl_parent_ll);
        final ConstraintLayout testCl = findViewById(R.id.test_cl);
        final TextView priceStrTv = findViewById(R.id.price_str_tv);
        initRv();

        priceStrTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EqualScaleCouponViewActivity.this, "点击了", Toast.LENGTH_SHORT).show();
            }
        });

        testCl.post(new Runnable() {
            @Override
            public void run() {
                ViewEKt.scaleView(testCl, false);
            }
        });
    }

    private void initRv() {
        RecyclerView rv = findViewById(R.id.rv);
        rv.setAdapter(new EqualScaleCouponViewAdapter());
    }


    private int dp2px(int dpI) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpI,
                getApplication().getApplicationContext().getResources().getDisplayMetrics());
    }
}
