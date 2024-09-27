package com.syncrown.toasty.ui.commons

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.style.ImageSpan

class CenterImageSpan(drawable: Drawable) : ImageSpan(drawable) {
    override fun draw(
        canvas: Canvas, text: CharSequence,
        start: Int, end: Int, x: Float,
        top: Int, y: Int, bottom: Int,
        paint: Paint
    ) {
        val drawable = drawable
        val fontMetricsInt = paint.fontMetricsInt
        val lineHeight = bottom - top
        val drawableHeight = drawable.intrinsicHeight

        val centerY = top + (lineHeight - drawableHeight) / 2

        canvas.save()

        canvas.translate(x, centerY.toFloat())
        drawable.draw(canvas)

        canvas.restore()
    }
}