package com.syncrown.toasty.ui.photoeditor.shape

import android.graphics.Paint
import com.syncrown.toasty.ui.photoeditor.shape.AbstractShape

/**
 * Simple data class to be put in an ordered Stack
 */
open class ShapeAndPaint(
    val shape: AbstractShape,
    val paint: Paint
)