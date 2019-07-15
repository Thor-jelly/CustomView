package com.jelly.thor.customview.carhomeitemstyle;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.customview.widget.ViewDragHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * 类描述：//TODO:(这里用一句话描述这个方法的作用)    <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/7/15 13:49 <br/>
 */
public class VerticalDragListView extends FrameLayout {
    /**
     * 这是系统给我们写好的一个工具
     */
    private ViewDragHelper mViewDragHelper;
    private View mDragView;
    private int mNoDragHelpViewMeasuredHeight;
    private int mMeasuredWidth;

    /**
     * 菜单是否打开
     */
    private boolean mMenuIsOpen = false;

    public VerticalDragListView(@NonNull Context context) {
        this(context, null);
    }

    public VerticalDragListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalDragListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mViewDragHelper = ViewDragHelper.create(this, mViewDragHelperCallback);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //现在布局中只放2两个布局，不等于2 抛出异常警告
        int childCount = getChildCount();
        if (childCount != 2) {
            throw new IllegalArgumentException("需要放值2个子布局");
        }

        //只有第二个布局才可以拖动，第一个布局不可以拖动。也就是前面的布局可以拖动后面的布局不可以拖动
        mDragView = getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //获取子view高度必须在onMeasure之后调用！
        //但是这里可能调用多次

        //什么情况下onMeasure可能会调用多次？addView setVisibility
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            //因此在这里调用获取限制高度
            View noDragHelpView = getChildAt(0);
            mNoDragHelpViewMeasuredHeight = noDragHelpView.getMeasuredHeight();

            mMeasuredWidth = getMeasuredWidth() - mDragView.getMeasuredWidth();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //重点返回true
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    //1。拖动我们的子view
    private ViewDragHelper.Callback mViewDragHelperCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            //指定该子View是否可以拖动，就是child
//            return true;

            //前面的布局可以拖动，后面的布局不可以拖动（需要判断当前的view是我设置的可以拖动的view）
            return mDragView == child;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            if (child != mDragView) {
                return 0;
            }
            //竖直移动
            if (top <= 0) {
                return 0;
            }
            /*int dragMaxHeight = mNoDragHelpViewMeasuredHeight + mNoDragHelpViewMeasuredHeight / 2;
            if (top >= dragMaxHeight) {
                return dragMaxHeight;
            }*/
            return top;
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            if (child != mDragView) {
                return 0;
            }
            //水平移动
            if (left <= 0) {
                return 0;
            }
            if (left >= mMeasuredWidth) {
                return mMeasuredWidth;
            }
            return left;
        }


        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            if (releasedChild != mDragView) {
                return;
            }
            Log.d("123===", "x->" + xvel);
            Log.d("123===", "y->" + yvel);
            if (mDragView.getTop() > mNoDragHelpViewMeasuredHeight) {
                //滚到到打开状态
                mMenuIsOpen = true;
                mViewDragHelper.settleCapturedViewAt(0, mNoDragHelpViewMeasuredHeight);
            } else {
                //滚到关闭状态
                mMenuIsOpen = false;
                mViewDragHelper.settleCapturedViewAt(0, 0);
            }
            invalidate();
        }
    };

    //添加完rv后，rv可滑动，菜单滑动不出来
    private float mDownY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mMenuIsOpen) {
            //菜单打开全部拦截
            return true;
        }

        //向下滑动拦截，不要给rv做处理
        //谁拦截谁 父view拦截子view， 但是子view可以调用这个方法
        //requestDisallowInterceptTouchEvent 请求父view不要拦截 ->其实就是改变mGroupFlags的值
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
                //如果写这行会提示 vieDragsHelp没有Down事件
                // （E/ViewDragHelper: Ignoring pointerId=0 because ACTION_DOWN was not received for this pointer before ACTION_MOVE. It likely happened because  ViewDragHelper did not receive all the events in the event stream.）
                mViewDragHelper.processTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = ev.getY();
                if ((moveY - mDownY) >= 0  && !mDragView.canScrollVertically(-1)) {
                    //向下滑动拦截，不让Rv做处理
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    //如果设置viewDragHelp 设置位置，必须添加这个方法
    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }
}