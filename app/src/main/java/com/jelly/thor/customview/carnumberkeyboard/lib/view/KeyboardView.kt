package com.jelly.thor.customview.carnumberkeyboard.lib.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.jelly.thor.customview.R
import com.jelly.thor.customview.carnumberkeyboard.lib.model.KeyModel
import com.jelly.thor.customview.carnumberkeyboard.lib.model.KeyTypeEnum
import com.jelly.thor.customview.carnumberkeyboard.lib.model.LayoutModel
import com.jelly.thor.customview.carnumberkeyboard.lib.model.RowModel

/**
 * 类描述：键盘view<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/9/2 13:28 <br/>
 */
class KeyboardView @JvmOverloads constructor(
    /**
     * 中文布局
     */
    val cnLayoutModel: LayoutModel,
    /**
     * 英文布局
     */
    val engLayoutModel: LayoutModel? = null,
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        /**
         * 英文item
         */
        @JvmStatic
        val ENG = LayoutModel(
            listOf(
                RowModel(
                    listOf(
                        KeyModel("1"),
                        KeyModel("2"),
                        KeyModel("3"),
                        KeyModel("4"),
                        KeyModel("5"),
                        KeyModel("6"),
                        KeyModel("7"),
                        KeyModel("8"),
                        KeyModel("9"),
                        KeyModel("0")
                    )
                ),
                RowModel(
                    listOf(
                        KeyModel("Q"),
                        KeyModel("W"),
                        KeyModel("E"),
                        KeyModel("R"),
                        KeyModel("T"),
                        KeyModel("Y"),
                        KeyModel("U"),
                        KeyModel("I"),
                        KeyModel("O"),
                        KeyModel("P")
                    )
                ),
                RowModel(
                    listOf(
                        KeyModel("A"),
                        KeyModel("S"),
                        KeyModel("D"),
                        KeyModel("F"),
                        KeyModel("G"),
                        KeyModel("H"),
                        KeyModel("J"),
                        KeyModel("K"),
                        KeyModel("L")
                    )
                ),
                RowModel(
                    listOf(
                        KeyModel("中文", keyTypeEnum = KeyTypeEnum.CN),
                        KeyModel("Z"),
                        KeyModel("X"),
                        KeyModel("C"),
                        KeyModel("V"),
                        KeyModel("B"),
                        KeyModel("N"),
                        KeyModel("M"),
                        KeyModel("删除", icon = R.mipmap.ic_launcher, keyTypeEnum = KeyTypeEnum.DEL)
                    )
                )
            )
        )
    }

    //todo:默认为英文
    private var mSelectLayoutModule = KeyTypeEnum.ENG


    init {
        //中文布局
        createKeyboardLayout(cnLayoutModel)
        //英文布局
        if (engLayoutModel != null) {
            createKeyboardLayout(engLayoutModel)
        }

        updateKeyboardLayout(mSelectLayoutModule)
    }

    /**
     * 更新键盘布局
     */
    private fun updateKeyboardLayout(keyType: KeyTypeEnum) {
        mSelectLayoutModule = keyType
        when (keyType) {
            KeyTypeEnum.CN -> {
                //中文布局
                createKeyboardLayout(cnLayoutModel)
            }
            KeyTypeEnum.ENG -> {
                //英文布局
                if (engLayoutModel == null) {
                    return
                }
                createKeyboardLayout(engLayoutModel)
            }
        }
    }

    /**
     * 创建view
     */
    private fun createKeyboardLayout(layoutModel: LayoutModel) {
        //以第一行的键盘数量为基准
        val maxColumn = layoutModel.rowItem[0].keyItem.size

        //行数
        val rowSize = layoutModel.rowItem.size


    }

}