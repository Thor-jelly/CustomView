package com.jelly.thor.customview.ratingbar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.jelly.thor.customview.R

/**
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/10/16 14:51 <br/>
 */
class RatingBar @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {
    private var mEnable = true
    private lateinit var mStarNormalBitmap: Bitmap
    private lateinit var mStarFocusBitmap: Bitmap
    private var mStarWidth = 0
    private var mStarHeight = 0
    private var mStarDistance = 0
    private var mStarNumber = 0

    init {
        val array = context?.obtainStyledAttributes(attrs, R.styleable.RatingBar)
        array?.apply {
            mEnable = array.getBoolean(R.styleable.RatingBar_star_enable, true)

            val starNormalId = array.getResourceId(R.styleable.RatingBar_normal_star, 0)
            if (starNormalId == 0) {
                throw RuntimeException("请设置正常星星图片！")
            }
            mStarNormalBitmap = BitmapFactory.decodeResource(resources, starNormalId)

            val focusNormalId = array.getResourceId(R.styleable.RatingBar_focus_star, 0)
            if (focusNormalId == 0) {
                throw RuntimeException("请设置选中星星图片！")
            }
            mStarFocusBitmap = BitmapFactory.decodeResource(resources, focusNormalId)

            mStarWidth = array.getDimension(R.styleable.RatingBar_star_width, 0F).toInt()
            mStarHeight = array.getDimension(R.styleable.RatingBar_star_height, 0F).toInt()

            mStarDistance = array.getDimension(R.styleable.RatingBar_star_distance, 0F).toInt()
            mStarNumber = array.getInt(R.styleable.RatingBar_star_number, 0)

            if (mStarHeight != 0 || mStarWidth != 0) {
                if (mStarHeight == 0) {
                    mStarHeight = mStarNormalBitmap.height
                }
                if (mStarWidth == 0) {
                    mStarWidth = mStarNormalBitmap.width
                }
                mStarNormalBitmap =
                    Bitmap.createScaledBitmap(mStarNormalBitmap, mStarWidth, mStarHeight, true)
                mStarFocusBitmap =
                    Bitmap.createScaledBitmap(mStarFocusBitmap, mStarWidth, mStarHeight, true)
            }
            recycle()
        }
    }

    /**
     * 当前选中的星星
     */
    private var mCurrentSelectStar = -1

    fun setSelectStar(selectStar: Int) {
        mCurrentSelectStar = selectStar
        invalidate()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = mStarNormalBitmap.width * mStarNumber + mStarDistance * (mStarNumber - 1)

        val height = mStarNormalBitmap.height

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            for (i in 0..mStarNumber) {
                val oneWidth = mStarNormalBitmap.width

                if (i == 0) {
                    val x = oneWidth * i
                    drawStar(i, this, x)
                } else {
                    val x = oneWidth * i + mStarDistance * i
                    drawStar(i, this, x)
                }
            }
        }
    }

    private fun drawStar(i: Int, canvas: Canvas, x: Int) {
        if (mCurrentSelectStar <= i) {
            canvas.drawBitmap(mStarNormalBitmap, x.toFloat(), 0F, null)
        } else {
            canvas.drawBitmap(mStarFocusBitmap, x.toFloat(), 0F, null)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!mEnable) {
            return super.onTouchEvent(event)
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                val moveX = event.x
                val oneWidth = width / mStarNumber
                var currentGrade = (moveX / oneWidth + 1).toInt()
                if (currentGrade < 0) {
                    currentGrade = 0
                }
                if (currentGrade > mStarNumber) {
                    currentGrade = mStarNumber
                }

                if (mCurrentSelectStar != currentGrade) {
                    mCurrentSelectStar = currentGrade
                    invalidate()
                }
            }
        }
        return true
    }
}