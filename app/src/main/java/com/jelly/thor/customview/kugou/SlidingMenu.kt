package com.jelly.thor.customview.kugou

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.HorizontalScrollView
import com.jelly.thor.customview.R

/**
 * 类描述：仿酷狗侧边栏<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/5/30 13:12 <br/>
 */
class SlidingMenu @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    HorizontalScrollView(context, attrs, defStyleAttr) {

    private var rightMargin = dp2px(20)
    private lateinit var contentView: View
    private lateinit var menuView: View

    /**
     * 是否打开菜单，默认是关闭的
     */
    private var mIsOpenMenu = false
    /**
     * 是否拦截
     */
    private var mIsIntercept = false

    /**
     * 菜单宽度
     */
    private val mMenuWidth by lazy {
        //菜单页宽度是 屏幕宽度 - 右边一部分距离(自定义属性)
        getScreenWidth(context) - rightMargin
    }

    private val mGestureDetector by lazy {
        GestureDetector(this.context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                //只在快速滑动的时候调用
                Log.d("123===", "快速滑动回调$velocityX")
                //从日志中可以看出 ，快速往左是一个负数，快速往右是一个正数

                if (mIsOpenMenu) {
                    //原来菜单是打开状态，往左快速滑动 关闭菜单
                    if (velocityX < 0) {
                        closeMenu()
                        return true
                    }
                } else {
                    //原来菜单是关闭状态，往右快速滑动 打开菜单
                    if (velocityX > 0) {
                        openMenu()
                        return true
                    }
                }

                return super.onFling(e1, e2, velocityX, velocityY)
            }
        })
    }

    init {
        //初始化自定义属性
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlidingMenu)

        rightMargin = typedArray.getDimension(R.styleable.SlidingMenu_menuRightMargin, dp2px(20).toFloat()).toInt()

        typedArray.recycle()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        mIsIntercept = false
        //时间拦截
        if (mIsOpenMenu) {
            //如果是打开菜单状态，并且点击了右边主题布局，隐藏菜单
            if (ev.x > mMenuWidth) {
                //关闭菜单
                closeMenu()
                //2 拦截子view点击事件

                //如果返回true，会拦截子view的点击事件，并且会响应自己的onTouchEvent事件
                mIsIntercept = true
                return true
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    /**
     * 宽高部队，指定宽高
     */
    override fun onFinishInflate() {
        super.onFinishInflate()
        //这个方法是布局解析完毕也就是XML布局文件解析完毕

        //1.内容也的宽度是屏幕宽度
        //获取布局中的LinearLayout
        val viewGroup = getChildAt(0) as ViewGroup
        //LinearLayout中，第一个是菜单，第二个是内容
        val childCount = viewGroup.childCount
        if (childCount != 2) {
            throw RuntimeException("布局中只能放置2个布局！")
        }
        //2.菜单页宽度是 屏幕宽度 - 右边一部分距离(自定义属性)
        //菜单栏view
        menuView = viewGroup.getChildAt(0)
        val menuParams = menuView.layoutParams
        menuParams.width = mMenuWidth
        //7.0以下的手机必须添加下面一行代码
        //menuView.layoutParams = menuParams

        contentView = viewGroup.getChildAt(1)
        val contentParams = contentView.layoutParams
        contentParams.width = getScreenWidth(context)
        //7.0以下的手机必须添加下面一行代码
        //contentView.layoutParams = contentParams
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        //3.初始化进来的位置，是内容布局在主界面
        scrollTo(mMenuWidth, 0)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        //如果有拦截，就不需要自己拦截
        if (mIsIntercept) {
            return true
        }

        //快速回调处理事件
        if (mGestureDetector.onTouchEvent(ev)) {
            return true
        }

        //4.处理手指松开后自动到指定位置
        if (ev.action == MotionEvent.ACTION_UP) {
            //只需要根据手指抬起距离来判断
            Log.d("123===", "--->$scrollX")
            if (scrollX > mMenuWidth / 2) {
                //关闭菜单
                closeMenu()
            } else {
                //打开菜单
                openMenu()
            }
            //不加这个会会出现，松开就停止的状态
            //主要是 super.onTouchEvent(ev) 的up事件中调用了 这个方法fling(-initialVelocity);
            return true
        }
        return super.onTouchEvent(ev)
    }

    private fun openMenu() {
        mIsOpenMenu = true
        Log.d("123===", "打开菜单")
        smoothScrollTo(0, 0)
    }

    private fun closeMenu() {
        mIsOpenMenu = false
        Log.d("123===", "关闭菜单")
        smoothScrollTo(mMenuWidth, 0)
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        //5.处理主内容缩放和菜单的透明度
        //变化是从 mMenuWidth 到 0
        val scale: Float = l.toFloat() / mMenuWidth //1 -> 0(可以自定义最小缩放值)
        val rightScale = 0.7F + (1 - 0.7F) * scale//设置缩放速度，以记最小值

        //5.1 设置右边缩放，默认是以中心点缩放
        //5.1.1 设置中心点
        contentView.pivotX = 0F
        contentView.pivotY = contentView.measuredHeight / 2F
        //5.1.2 设置缩放
        contentView.scaleX = rightScale
        contentView.scaleY = rightScale

        //5.2 设置菜单的 透明度 和 缩放
        //5.2.1 半透明->透明 0.0f->1.0f
        val leftAlpha =/* 0.5F +*/ (1 - scale)
        /** (1- 0.5F)*/
        menuView.alpha = leftAlpha
        //5.2.2 缩放 0.7f -> 1.0f
        val leftScale = 0.7F + (1 - 0.7F) * (1 - scale)//设置缩放速度，以记最小值
        menuView.scaleX = leftScale
        menuView.scaleY = leftScale
        //5.2.3 平移
        menuView.translationX = 0.2F * l
    }

    /**
     * dp2px
     */
    private fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics)
            .toInt()
    }

    /**
     * 获取屏幕宽度
     */
    private fun getScreenWidth(context: Context): Int {
        val systemService = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        systemService.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }
}

