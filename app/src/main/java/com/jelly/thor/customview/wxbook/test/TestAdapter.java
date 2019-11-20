package com.jelly.thor.customview.wxbook.test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jelly.thor.customview.R;
import com.jelly.thor.customview.wxbook.WXBaseAdapter;
import com.jelly.thor.customview.wxbook.WXViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/11/19 19:54 <br/>
 */
public class TestAdapter extends WXBaseAdapter<TestBean> {
    public TestAdapter(@NotNull List<TestBean> list) {
        super(list);
    }

    @NonNull
    @Override
    public WXViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wx_item_view, parent, false);
        return new WXViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WXViewHolder holder, int position) {
        TextView tv = holder.getView(R.id.tv);
        TestBean bean = getList().get(position);
        tv.setText(bean.name);
    }

    @Override
    public int getItemCount() {
        return getList().size();
    }
}
