package com.syncrown.arpang.ui.commons

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class DimCropOverlayView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val dimPaint = Paint().apply {
        color = 0x99000000.toInt()  // dim 색상 (반투명 검정)
    }
    private val clearPaint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    private var cropRect: RectF? = null

    // cropOverlay 위치와 크기 정보를 업데이트하는 함수
    fun setCropRect(cropOverlay: View) {
        cropRect = RectF(
            cropOverlay.x,
            cropOverlay.y,
            cropOverlay.x + cropOverlay.width,
            cropOverlay.y + cropOverlay.height
        )
        invalidate()  // 다시 그리기 요청
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        setLayerType(LAYER_TYPE_SOFTWARE, null)

        // 전체 화면을 dim 처리
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), dimPaint)

        // cropOverlay 부분은 투명하게 처리
        cropRect?.let {
            canvas.drawRect(it, clearPaint)
        }
    }
}
