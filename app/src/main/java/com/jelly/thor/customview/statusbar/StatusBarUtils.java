package com.jelly.thor.customview.statusbar;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 类描述：状态栏工具类<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/7/19 09:53 <br/>
 */
public class StatusBarUtils {
    private StatusBarUtils() {
    }

    /**
     * 为我们activity设置状态栏颜色
     */
    public static void setStatusBarColor(AppCompatActivity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //[5.0,)  5.0以上
            //直接调用系统提供的方法
            activity.getWindow().setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //[4.4,5.0)
            activity.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS//透明状态栏效果
//                    | WindowManager.LayoutParams.FLAG_FULLSCREEN //全屏效果
            );

            View view = new View(activity);
            //获取状态栏高度
            int statusBarHeight = getStatusBarHeight(activity);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight));
            view.setBackgroundColor(color);
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            decorView.addView(view);
            //加0 就不显示
            //decorView.addView(view, 0);

            //1. 如果不在根布局中加上下代码,会布局到statusBar上
            //需要再根布局加上 android:fitsSystemWindows="true"
            // 获取activity中setContentView布局的根布局
            //2.设置padding 或直接在 根布局上代码设置 android:fitsSystemWindows="true"
            //ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            //2.1 contentView.setPadding(0,getStatusBarHeight(activity),0,0);
            //      activityView.setPadding(0,getStatusBarHeight(activity),0,0);
            //2.2 View activityView = contentView.getChildAt(0); //获取activity中setContentView布局的根布局
            //      activityView.setFitsSystemWindows(true);
        }
    }


    /**
     * 为我们activity设置全屏
     */
    public static void setFullActivity(AppCompatActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //[5.0,)  5.0以上
            //直接调用系统提供的方法
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //[4.4,5.0)
            activity.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS//透明状态栏效果
            );
        }
    }

    /**
     * 获取状态栏高度
     */
    private static int getStatusBarHeight(AppCompatActivity activity) {
        //先获取资源id,
        Resources res = activity.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId != 0) {
            Log.d("123===", "状态栏高度:" + res.getDimensionPixelSize(resourceId));
            return res.getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
