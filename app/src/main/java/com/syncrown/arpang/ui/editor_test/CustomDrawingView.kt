package com.syncrown.arpang.ui.editor_test

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class CustomDrawingView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private var paint: Paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    private var path = Path()
    private var bounds = RectF()

    private var isSelected = false
    private var lastTouchX = 0f
    private var lastTouchY = 0f

    private var isDragging = false
    private var rotationAngle = 0f
    private var initialRotationAngle = 0f

    private var isDone = false

    private lateinit var rotateButton: ImageView
    private lateinit var deleteButton: ImageView
    private lateinit var borderView: View

    init {
        updateButtonPositions()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun attachViews(rotateBtn: ImageView, deleteBtn: ImageView, border: View) {
        rotateButton = rotateBtn
        deleteButton = deleteBtn
        borderView = border

        rotateButton.visibility = View.GONE
        deleteButton.visibility = View.GONE
        borderView.visibility = View.GONE

        // 회전 버튼의 터치 이벤트 처리
        rotateButton.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 터치 시작 시 초기 회전 각도 저장
                    initialRotationAngle = calculateAngle(event.rawX, event.rawY)
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    // 터치 이동 중 새로운 각도 계산
                    val newAngle = calculateAngle(event.rawX, event.rawY)
                    var angleDelta = newAngle - initialRotationAngle

                    // 너무 작은 변화는 무시하기 위해 임계값 설정
                    if (kotlin.math.abs(angleDelta) > 1) {
                        // 회전 각도를 적절한 범위 내로 제한하여 과도한 회전을 방지
                        rotationAngle += angleDelta
                        angleDelta = angleDelta.coerceIn(-2f, 2f)

                        // 초기 회전 각도를 업데이트
                        initialRotationAngle = newAngle

                        // 회전을 적용하고 뷰를 업데이트
                        applyRotationToViews(rotationAngle)
                        updateButtonPositions()
                        invalidate()
                    }
                    true
                }

                MotionEvent.ACTION_UP -> true
                else -> false
            }
        }


        deleteButton.setOnClickListener {
            clearDrawing()
        }
    }

    fun setColor(newColor: Int) {
        paint.color = newColor
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        if (isDone) {
            // finalizeDrawing이 호출된 후에는 터치 이벤트를 무시
            return false
        }

        Log.e("jung","isTouchWithinBorder(x, y) : " + isTouchWithinBorder(x, y))

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (bounds.contains(x, y)) {
                    // 드래그 시작
                    isSelected = true
                    lastTouchX = x
                    lastTouchY = y
                    isDragging = true

                    return true
                } else {
                    // 새로운 경로 시작
                    path.moveTo(x, y)
                    showBorderAndButtons()
                    return true
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (isDragging) {
                    // 드래그 중 경로 및 버튼 이동
                    val dx = x - lastTouchX
                    val dy = y - lastTouchY

                    bounds.offset(dx, dy)
                    path.offset(dx, dy)

                    lastTouchX = x
                    lastTouchY = y

                    updateButtonPositions()
                    updateBorderPosition()
                    invalidate()
                    return true
                } else {
                    // 경로 확장
                    path.lineTo(x, y)
                    updateBounds()
                    invalidate()
                    return true
                }
            }

            MotionEvent.ACTION_UP -> {
                // 드래그 종료
                if (isDragging) {
                    isDragging = false
                } else {
                    updateBounds()
                    updateButtonPositions()
                    updateBorderPosition()
                }
                saveCurrentState()
                return true
            }

            else -> return false
        }
    }

    private fun isTouchWithinBorder(x: Float, y: Float): Boolean {
        // borderView의 경계 좌표를 얻습니다.
        val left = borderView.left.toFloat()
        val right = borderView.right.toFloat()
        val top = borderView.top.toFloat()
        val bottom = borderView.bottom.toFloat()

        // 터치 좌표(x, y)가 borderView의 경계 내에 있는지 확인합니다.
        return x in left..right && y >= top && y <= bottom
    }


    private fun calculateAngle(x: Float, y: Float): Float {
        val dx = x - bounds.centerX()
        val dy = y - bounds.centerY()
        return Math.toDegrees(atan2(dy, dx).toDouble()).toFloat()
    }

    private fun saveCurrentState() {
        lastTouchX = bounds.centerX()
        lastTouchY = bounds.centerY()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.save()
        canvas.rotate(rotationAngle, bounds.centerX(), bounds.centerY())
        canvas.drawPath(path, paint)
        canvas.restore()
    }

    private fun updateBounds() {
        path.computeBounds(bounds, true)
        bounds.inset(-10f, -10f)
        updateButtonPositions()
        updateBorderPosition()
    }

    private fun updateButtonPositions() {
        if (::rotateButton.isInitialized && ::deleteButton.isInitialized) {
            val centerX = bounds.centerX()
            val centerY = bounds.centerY()
            val halfWidth = bounds.width() / 2
            val halfHeight = bounds.height() / 2

            val corners = arrayOf(
                PointF(centerX - halfWidth, centerY - halfHeight), // 좌상단
                PointF(centerX + halfWidth, centerY - halfHeight), // 우상단
                PointF(centerX - halfWidth, centerY + halfHeight), // 좌하단
                PointF(centerX + halfWidth, centerY + halfHeight)  // 우하단
            )

            // 각 코너의 회전된 좌표 계산
            for (corner in corners) {
                val dx = corner.x - centerX
                val dy = corner.y - centerY
                val rotatedX =
                    centerX + (dx * cos(Math.toRadians(rotationAngle.toDouble())) - dy * sin(
                        Math.toRadians(rotationAngle.toDouble())
                    ))
                val rotatedY =
                    centerY + (dx * sin(Math.toRadians(rotationAngle.toDouble())) + dy * cos(
                        Math.toRadians(rotationAngle.toDouble())
                    ))
                corner.set(rotatedX.toFloat(), rotatedY.toFloat())
            }

            // 회전된 위치에 따라 버튼의 좌표를 설정
            rotateButton.x = corners[1].x + 20f - rotateButton.width / 2
            rotateButton.y = corners[1].y - 20f - rotateButton.height / 2

            deleteButton.x = corners[0].x - 20f - deleteButton.width / 2
            deleteButton.y = corners[0].y - 20f - deleteButton.height / 2

            // 회전 각도를 업데이트
            applyRotationToViews(rotationAngle)
        }
    }

    private fun updateBorderPosition() {
        if (::borderView.isInitialized) {
            val params = borderView.layoutParams as ConstraintLayout.LayoutParams
            params.width = bounds.width().toInt()
            params.height = bounds.height().toInt()
            borderView.layoutParams = params

            borderView.x = bounds.left
            borderView.y = bounds.top

            // 회전 적용
            borderView.pivotX = (borderView.width / 2).toFloat()
            borderView.pivotY = (borderView.height / 2).toFloat()
            borderView.rotation = rotationAngle
        }
    }

    private fun applyRotationToViews(angle: Float) {
        if (::borderView.isInitialized && ::rotateButton.isInitialized && ::deleteButton.isInitialized) {
            borderView.rotation = angle

            // 회전 중심 설정
            rotateButton.pivotX = (rotateButton.width / 2).toFloat()
            rotateButton.pivotY = (rotateButton.height / 2).toFloat()
            rotateButton.rotation = angle

            deleteButton.pivotX = (deleteButton.width / 2).toFloat()
            deleteButton.pivotY = (deleteButton.height / 2).toFloat()
            deleteButton.rotation = angle
        }
    }

    private fun clearDrawing() {
        path.reset()
        isSelected = false
        rotationAngle = 0f

        // 버튼 및 테두리를 숨기지만, 다시 그리기 가능하도록 설정
        rotateButton.visibility = View.GONE
        deleteButton.visibility = View.GONE
        borderView.visibility = View.GONE

        invalidate()
    }

    private fun showBorderAndButtons() {
        if (::rotateButton.isInitialized && ::deleteButton.isInitialized && ::borderView.isInitialized) {
            rotateButton.visibility = View.VISIBLE
            deleteButton.visibility = View.VISIBLE
            borderView.visibility = View.VISIBLE
        }
    }

    fun finalizeDrawing() {
        // 나머지 뷰들(회전 버튼, 삭제 버튼, 테두리)을 숨기고 그림만 남기기
        if (::rotateButton.isInitialized && ::deleteButton.isInitialized && ::borderView.isInitialized) {
            rotateButton.visibility = View.GONE
            deleteButton.visibility = View.GONE
            borderView.visibility = View.GONE
        }

        // 뷰를 다시 그리도록 요청
        invalidate()
    }

    fun finalizeDrag(isDone: Boolean) {
        // 탭을눌러 완료 상태 이므로 더이상 이동 안되도록
        this.isDone = isDone
    }
}
