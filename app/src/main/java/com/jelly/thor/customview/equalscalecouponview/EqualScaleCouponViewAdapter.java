package com.jelly.thor.customview.equalscalecouponview;

import android.graphics.Point;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jelly.thor.customview.R;

/**
 * 类描述：//TODO:(这里用一句话描述这个方法的作用)    <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/9/6 14:43 <br/>
 */
public class EqualScaleCouponViewAdapter extends RecyclerView.Adapter<EqualScaleCouponViewAdapter.ViewHolder> {

    private AppCompatActivity context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = (AppCompatActivity) parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_equal_scale_coupon_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "position=" + position, Toast.LENGTH_SHORT).show();
            }
        });

        holder.mTestCl.post(new Runnable() {
            @Override
            public void run() {
                //生成testCl view截图
                Point size = new Point();
                context.getWindow().getWindowManager().getDefaultDisplay().getSize(size);
                int px30I = dp2px(context, 30);

                final int newXI = size.x - px30I;
                final int newYI = newXI * 76 / 345;

                holder.mTestCl.setPivotX(0);
                holder.mTestCl.setPivotY(0);


                float scaleX = newXI / (float) dp2px(context, 345);
                float scaleY = newYI / (float) dp2px(context, 76);
                holder.mTestCl.setScaleX(scaleX);
                holder.mTestCl.setScaleY(scaleY);
                ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
                layoutParams.width = newXI;
                layoutParams.height = newYI;
                holder.itemView.setLayoutParams(layoutParams);
            }
        });
    }

    private int dp2px(AppCompatActivity appCompatActivity, int dpI) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpI,
                appCompatActivity.getApplication().getApplicationContext().getResources().getDisplayMetrics());
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        private final ConstraintLayout mTestCl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTestCl = itemView.findViewById(R.id.test_cl);
        }
    }
}
