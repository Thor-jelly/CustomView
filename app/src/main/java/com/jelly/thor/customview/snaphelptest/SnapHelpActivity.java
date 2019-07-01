package com.jelly.thor.customview.snaphelptest;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.jelly.thor.customview.R;

/**
 * 类描述：//TODO:(这里用一句话描述这个方法的作用)    <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/6/27 10:04 <br/>
 */
public class SnapHelpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_snap_help_test);

        RecyclerView rv1 = findViewById(R.id.rv1);
        LinearLayoutManager ll1 = new LinearLayoutManager(this);
        ll1.setOrientation(RecyclerView.HORIZONTAL);
        rv1.setLayoutManager(ll1);
        SnapHelpAdapter adapter = new SnapHelpAdapter();
        rv1.setAdapter(adapter);
        LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
        linearSnapHelper.attachToRecyclerView(rv1);


        RecyclerView rv2 = findViewById(R.id.rv2);
        LinearLayoutManager ll2 = new LinearLayoutManager(this);
        ll2.setOrientation(RecyclerView.HORIZONTAL);
        rv2.setLayoutManager(ll2);
        SnapHelpAdapter adapter2 = new SnapHelpAdapter();
        rv2.setAdapter(adapter2);
        PagerSnapHelper linearSnapHelper2 = new PagerSnapHelper();
        linearSnapHelper2.attachToRecyclerView(rv2);
    }
}
