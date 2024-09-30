package com.syncrown.arpang.ui.commons

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.syncrown.arpang.R

class CustomDynamicTagView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        val backgroundDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 60f
            setColor(ContextCompat.getColor(context, R.color.color_f5f5f5))
        }

        background = backgroundDrawable
        textSize = 16f
        setPadding(
            CommonFunc.dpToPx(15, context),
            CommonFunc.dpToPx(6, context),
            CommonFunc.dpToPx(15, context),
            CommonFunc.dpToPx(6, context)
        )
        setTextColor(Color.BLACK)
    }
}