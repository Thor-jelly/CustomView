package com.jelly.thor.customview.wxbook;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 类描述：//分类item<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/11/20 09:58 <br/>
 */
public class WXTitleItemDecoration extends RecyclerView.ItemDecoration {
    private @NotNull
    List<? extends WXBean> list;
    private Builder mBuilder;
    private Paint mPaint;

    public WXTitleItemDecoration(@NotNull List<? extends WXBean> list) {
        this(list, new Builder());
    }

    public WXTitleItemDecoration(@NotNull List<? extends WXBean> list, Builder builder) {
        this.list = list;
        mBuilder = builder;
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (list.isEmpty()) {
            return;
        }
        //第一步设置出分类item顶部留的距离
        final int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        if (position < 0) {
            return;
        }
        if (position == 0) {
            //第一个item肯定是有分类的
            outRect.set(0, mBuilder.height, 0, 0);
        } else {
            //其他通过判断当前的是否等于上一个首字母
            String nowSub = WXUtils.getPinyinFirstChar(list.get(position));
            String oldSub = WXUtils.getPinyinFirstChar(list.get(position - 1));
            if (!nowSub.equals(oldSub)) {
                outRect.set(0, mBuilder.height, 0, 0);
            } else {
                outRect.set(0, 0, 0, 0);
            }
        }
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (list.isEmpty()) {
            return;
        }
        //第二部绘制分类分割线
        final int start = parent.getPaddingStart();
        final int end = parent.getWidth() - parent.getPaddingEnd();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int position = params.getViewLayoutPosition();
            if (position < 0) {
                return;
            }
            String nowSub = WXUtils.getPinyinFirstChar(list.get(position));
            if (position == 0) {
                //第一个item肯定是有分类的，绘制分类
                drawTitle(c, start, end, child, params, position, nowSub);
            } else {
                //其他通过判断当前的是否等于上一个首字母
                String oldSub = WXUtils.getPinyinFirstChar(list.get(position - 1));
                if (!nowSub.equals(oldSub)) {
                    //绘制分类
                    drawTitle(c, start, end, child, params, position, nowSub);
                } else {
                    //不绘制
                }
            }
        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        if (list.isEmpty()) {
            return;
        }
        //绘制悬浮view
        final int position = ((LinearLayoutManager) (parent.getLayoutManager())).findFirstVisibleItemPosition();

        //View child = parent.getChildAt(position);
        //出现一个奇怪的bug，有时候child为空，所以将 child = parent.getChildAt(i)。-》 parent.findViewHolderForLayoutPosition(position).itemView
        View child = parent.findViewHolderForLayoutPosition(position).itemView;

        String nowSub = WXUtils.getPinyinFirstChar(list.get(position));

        //是否移动画布
        boolean isTranslateCanvas = false;
        if (position + 1 < list.size()) {
            String nextSub = WXUtils.getPinyinFirstChar(list.get(position + 1));
            if (!nowSub.equals(nextSub)) {
                //下一个位置分类跟当前不同
                //当getTop开始变负，它的绝对值，是第一个可见的Item移出屏幕的距离
//                Log.d("123===", "child.getHeight()=" + child.getHeight());
//                Log.d("123===", "child.getTop()=" + child.getTop());
//                Log.d("123===", "mBuilder.height=" + mBuilder.height);
//                Log.d("123===", "==========");
                if (child.getHeight() + child.getTop() < mBuilder.height) {
                    //当第一个可见的item在屏幕中还剩的高度小于title区域的高度时，开始做悬浮Title的“交换动画”
                    c.save();
                    isTranslateCanvas = true;

                    c.translate(0, child.getHeight() + child.getTop() - mBuilder.height);
                }
            }
        }

        //绘制背景
        mPaint.setColor(mBuilder.color);
        c.drawRect(parent.getPaddingStart(),
                parent.getPaddingTop(),
                parent.getRight() - parent.getPaddingEnd(),
                parent.getPaddingTop() + mBuilder.height,
                mPaint);

        //绘制文字
        mPaint.setColor(mBuilder.topTitleTextColor);
        Rect bounds = new Rect();
        int newStar = child.getPaddingStart() + mBuilder.textMarginStar;
        mPaint.getTextBounds(nowSub, 0, nowSub.length(), bounds);
        c.drawText(nowSub,
                newStar,
                parent.getPaddingTop() + mBuilder.height - (mBuilder.height / 2 - bounds.height() / 2),
                mPaint);

        if (isTranslateCanvas) {
            //恢复画布到之前保存状态
            c.restore();
        }
    }

    /**
     * 绘制分类
     */
    private void drawTitle(Canvas c, int start, int end, View child, RecyclerView.LayoutParams params, int position, String str) {
        //绘制背景
        mPaint.setColor(mBuilder.color);
        c.drawRect(start,
                child.getTop() - params.topMargin - mBuilder.height,
                end,
                child.getTop() - params.topMargin, mPaint
        );
        //绘制文字
        mPaint.setColor(mBuilder.textColor);
        Rect bounds = new Rect();
        int newStar = child.getPaddingStart() + mBuilder.textMarginStar;
        mPaint.getTextBounds(str, 0, str.length(), bounds);
        mPaint.setTextSize(mBuilder.textSize);
        c.drawText(str, newStar, child.getTop() - params.topMargin - (mBuilder.height / 2 - bounds.height() / 2), mPaint);
    }


    public static class Builder {
        /**
         * 分类高度px
         */
        private int height = 100;
        /**
         * 分类背景色
         */
        private int color = Color.RED;
        /**
         * 文字颜色
         */
        private int textColor = Color.WHITE;
        /**
         * 顶部文字颜色
         */
        private int topTitleTextColor = Color.GREEN;
        /**
         * 文字大小 px
         */
        private int textSize = 40;

        /**
         * 分类文字距离开始位置
         */
        private int textMarginStar = 40;

        /**
         * 传进来px
         */
        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setColor(int color) {
            this.color = color;
            return this;
        }

        public Builder setTopTitleTextColor(int topTitleTextColor) {
            this.topTitleTextColor = topTitleTextColor;
            return this;
        }

        public Builder setTextColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public Builder setTextMarginStar(int textMarginStar) {
            this.textMarginStar = textMarginStar;
            return this;
        }

        public Builder setTextSize(int textSize) {
            this.textSize = textSize;
            return this;
        }
    }
}
