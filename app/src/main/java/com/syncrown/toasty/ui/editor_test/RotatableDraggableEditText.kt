package com.syncrown.toasty.ui.editor_test

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout
import com.syncrown.toasty.R
import com.syncrown.toasty.databinding.RotatableDraggableEdittextBinding
import kotlin.math.atan2


class RotatableDraggableEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: RotatableDraggableEdittextBinding

    private var rotationAngle = 0f
    private var startX = 0f
    private var startY = 0f
    private var isDragging = false
    private var isRotating = false
    private var centerX = 0f
    private var centerY = 0f

    init {
        // Inflate the layout using view binding
        val inflater = LayoutInflater.from(context)
        binding = RotatableDraggableEdittextBinding.inflate(inflater, this)

        // Initialize views
        setupEditText()
        setupRotateButton()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupEditText() {
        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateEditTextWidth()

                updateBackground(binding.editText.hasFocus())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.editText.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = event.rawX - this@RotatableDraggableEditText.x
                    startY = event.rawY - this@RotatableDraggableEditText.y
                    isDragging = true
                }
                MotionEvent.ACTION_MOVE -> {
                    if (isDragging) {
                        this@RotatableDraggableEditText.x = event.rawX - startX
                        this@RotatableDraggableEditText.y = event.rawY - startY
                    }
                }
                MotionEvent.ACTION_UP -> isDragging = false
            }
            false
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupRotateButton() {
        binding.rotateButton.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isRotating = true

                    // 현재 뷰의 중심을 회전 중심으로 설정
                    val location = IntArray(2)
                    this@RotatableDraggableEditText.getLocationOnScreen(location)
                    centerX = (location[0] + this@RotatableDraggableEditText.width / 2f)
                    centerY = (location[1] + this@RotatableDraggableEditText.height / 2f)
                }
                MotionEvent.ACTION_MOVE -> {
                    if (isRotating) {
                        val angle = calculateRotationAngle(event.rawX, event.rawY)
                        this@RotatableDraggableEditText.rotation = rotationAngle + angle
                    }
                }
                MotionEvent.ACTION_UP -> {
                    isRotating = false

                    // 회전 각도 누적
                    rotationAngle = this@RotatableDraggableEditText.rotation
                }
            }
            true
        }

    }

    private fun calculateRotationAngle(x: Float, y: Float): Float {
        val deltaX = x - centerX
        val deltaY = y - centerY
        return Math.toDegrees(atan2(deltaY.toDouble(), deltaX.toDouble())).toFloat()
    }

    fun updateEditTextWidth() {
        val text = binding.editText.text.toString()

        // Paint 객체를 사용하여 텍스트의 너비 측정
        val paint = binding.editText.paint
        val textWidth = paint.measureText(text)

        // 최소 너비를 설정할 수 있습니다 (필요 시)
        val minWidth = 20 // 최소 너비 (픽셀 단위)

        // LayoutParams의 width를 WRAP_CONTENT로 설정
        val params = binding.editText.layoutParams
        params.width = if (textWidth.toInt() < minWidth) minWidth else LayoutParams.WRAP_CONTENT
        binding.editText.layoutParams = params
    }

    fun updateBackground(hasFocus: Boolean) {
        if (hasFocus) {
            binding.editText.setBackgroundResource(R.drawable.custom_bg_edit)
            binding.rotateButton.visibility = VISIBLE
        } else {
            if (binding.editText.text.isNullOrEmpty()) {
                // 텍스트가 없고 힌트만 있을 때 배경 설정
                binding.editText.setBackgroundResource(R.drawable.custom_bg_edit)
            } else {
                // 텍스트가 있을 때 배경을 투명하게 설정
                binding.editText.setBackgroundColor(Color.TRANSPARENT)
                binding.rotateButton.visibility = GONE
            }
        }

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun getEditText() = binding.editText
}

