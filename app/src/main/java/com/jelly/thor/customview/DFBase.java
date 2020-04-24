package com.jelly.thor.customview;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

/**
 * 类描述：详情DialogFragment基类<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/3/18 14:23 <br/>
 */
public abstract class DFBase extends AppCompatDialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        if (window != null) {
            setBackgroundDrawable(window);
            setOutBackgroundDrawableTransparent(window);
        }
        //外部点击事件
        setOutClick();

        return inflater.inflate(createView(), container, false);
    }

    protected void setOutClick() {
        //外部点击事件setCancelable(true);//getDialog().setCanceledOnTouchOutside(false);
    }

    protected void setOutBackgroundDrawableTransparent(Window window){
        //window.setDimAmount(0);
    }

    protected void setBackgroundDrawable(Window window) {
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    /**
     * 设置布局
     *
     * @return R.layout.xxx
     */
    @LayoutRes
    public abstract int createView();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    /**
     * 在onViewCreated中初始化View
     */
    protected abstract void initView(View view);

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            setDialogParams(window);
        }
    }

    /**
     * 设置dialogFragment参数
     */
    protected void setDialogParams(Window window) {
        //Point point = new Point();
        //window.getWindowManager().getDefaultDisplay().getSize(point);

        //设置宽度百分比
        //#第一种
        //window.setLayout((int) (point.x * 0.5), ViewGroup.LayoutParams.WRAP_CONTENT);

        //WindowManager.LayoutParams layoutParams = window.getAttributes();
        //#第二种
        //layoutParams.width = (int) (point.x);
        //layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;

        //设置dialog位置
        //layoutParams.gravity = Gravity.BOTTOM;
        //设置dialog动画
        //layoutParams.windowAnimations = R.style.xxxx;
        //window.setAttributes(layoutParams);
    }
}
