package com.syncrown.arpang.ui.component.home.tab1_home.life2cut.crop

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityCropImageBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.component.home.tab1_home.ar_print.ArImageStorage
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min


class CropImageActivity : BaseActivity() {
    private lateinit var binding: ActivityCropImageBinding
    private lateinit var filePath: String

    private var lastX = 0f
    private var lastY = 0f
    private var overlayStartX = 0f
    private var overlayStartY = 0f

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityCropImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        filePath = intent.getStringExtra("CROP_IMAGE_FILE_PATH") ?: ""
        Log.e("jung","filepath : $filePath")
        val bitmap = ArImageStorage.bitmap

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = getString(R.string.edit_crop_image)
        binding.actionbar.actionEtc.text = getString(R.string.edit_crop_image_ok)
        binding.actionbar.actionEtc.setOnClickListener {
            lifecycleScope.launch {
                cropAndSaveImage()

                setResult(RESULT_OK)
                finish()
            }
        }

        if (filePath.isNotBlank() && filePath != "null") {
            setImage()
        }

        if (bitmap != null) {
            setImage(bitmap)
        }


        setCropView()
    }

    private fun setImage() {
        Glide.with(this)
            .load(CommonFunc.path2uri(this, filePath))
            .into(binding.cropImageView)
    }

    private fun setImage(bitmap: Bitmap) {
        Glide.with(this)
            .load(bitmap)
            .into(binding.cropImageView)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setCropView() {
        binding.cropOverlay.visibility = View.VISIBLE
        binding.cropOverlay.layoutParams.width = 200
        binding.cropOverlay.layoutParams.height = 200
        binding.cropOverlay.x = 100f
        binding.cropOverlay.y = 100f
        binding.cropOverlay.requestLayout()

        // resizeHandle 드래그로 cropOverlay 크기 조정
        binding.resizeHandle.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = event.rawX
                    lastY = event.rawY
                }

                MotionEvent.ACTION_MOVE -> {
                    val dx = event.rawX - lastX
                    val dy = event.rawY - lastY
                    resizeOverlay(dx, dy)
                    lastX = event.rawX
                    lastY = event.rawY
                    updateDimOverlay()
                }
            }
            true
        }

        // cropOverlay 배경을 드래그하여 이동
        binding.cropOverlay.setOnTouchListener { view, event ->
            val isInsideResizeHandle =
                isPointInsideView(event.rawX, event.rawY, binding.resizeHandle)

            if (!isInsideResizeHandle) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        lastX = event.rawX
                        lastY = event.rawY
                        overlayStartX = view.x
                        overlayStartY = view.y
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val dx = event.rawX - lastX
                        val dy = event.rawY - lastY
                        moveOverlay(view, dx, dy)
                        updateDimOverlay()
                    }
                }
            }
            true
        }

        updateDimOverlay()
    }

    private fun updateDimOverlay() {
        // cropOverlay의 위치와 크기를 DimOverlayView에 전달
        binding.dimOverlay.setCropRect(binding.cropOverlay)
    }

    private fun resizeOverlay(dx: Float, dy: Float) {
        val imageViewWidth = binding.cropImageView.width
        val imageViewHeight = binding.cropImageView.height
        val maxWidth = imageViewWidth - binding.cropOverlay.x.toInt()
        val maxHeight = imageViewHeight - binding.cropOverlay.y.toInt()
        val minSize = 100

        val newWidth = max(min(binding.cropOverlay.width + dx.toInt(), maxWidth), minSize)
        val newHeight = max(min(binding.cropOverlay.height + dy.toInt(), maxHeight), minSize)

        binding.cropOverlay.layoutParams.width = newWidth
        binding.cropOverlay.layoutParams.height = newHeight
        binding.cropOverlay.requestLayout()
    }

    private fun moveOverlay(view: View, dx: Float, dy: Float) {
        val newX = overlayStartX + dx
        val newY = overlayStartY + dy

        if (newX >= 0 && newX + view.width <= binding.cropImageView.width) {
            view.x = newX
        }
        if (newY >= 0 && newY + view.height <= binding.cropImageView.height) {
            view.y = newY
        }
    }

    private fun isPointInsideView(x: Float, y: Float, view: View): Boolean {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val viewX = location[0].toFloat()
        val viewY = location[1].toFloat()
        return (x >= viewX && x <= viewX + view.width && y >= viewY && y <= viewY + view.height)
    }

    // 이미지를 잘라서 저장하는 함수
    private fun cropAndSaveImage() {
        // ImageView에서 Drawable을 Bitmap으로 가져오기
        val drawable = binding.cropImageView.drawable
        val bitmap = (drawable as BitmapDrawable).bitmap

        // ImageView 크기와 원본 Bitmap 크기
        val imageViewWidth = binding.cropImageView.width.toFloat()
        val imageViewHeight = binding.cropImageView.height.toFloat()
        val bitmapWidth = bitmap.width.toFloat()
        val bitmapHeight = bitmap.height.toFloat()

        // 이미지가 centerCrop으로 어떻게 표시되는지 계산
        val scale: Float
        val dx: Float
        val dy: Float

        if (bitmapWidth / bitmapHeight > imageViewWidth / imageViewHeight) {
            // 이미지가 ImageView보다 더 넓음, 높이를 ImageView에 맞추고 좌우가 잘림
            scale = imageViewHeight / bitmapHeight
            dx = (bitmapWidth * scale - imageViewWidth) / 2
            dy = 0f  // 위아래는 잘리지 않음
        } else {
            // 이미지가 ImageView보다 더 높음, 너비를 ImageView에 맞추고 위아래가 잘림
            scale = imageViewWidth / bitmapWidth
            dx = 0f  // 좌우는 잘리지 않음
            dy = (bitmapHeight * scale - imageViewHeight) / 2
        }

        // cropOverlay의 좌표와 크기를 Bitmap 좌표로 변환
        val cropOverlayX = binding.cropOverlay.x
        val cropOverlayY = binding.cropOverlay.y
        val cropOverlayWidth = binding.cropOverlay.width
        val cropOverlayHeight = binding.cropOverlay.height

        // cropOverlay 좌표를 원본 Bitmap 좌표로 변환
        val cropX = ((cropOverlayX + dx) / scale).toInt().coerceAtLeast(0)
        val cropY = ((cropOverlayY + dy) / scale).toInt().coerceAtLeast(0)
        var cropWidth = (cropOverlayWidth / scale).toInt()
        var cropHeight = (cropOverlayHeight / scale).toInt()

        // 비율 계산 (width:height 비율 유지)
        val overlayAspectRatio = cropOverlayWidth / cropOverlayHeight.toFloat()

        // 잘라낼 범위가 Bitmap 크기를 초과하지 않도록 비율에 맞춰 크기 조정
        if (cropX + cropWidth > bitmap.width) {
            cropWidth = bitmap.width - cropX
            cropHeight = (cropWidth / overlayAspectRatio).toInt()  // 비율 유지
        }
        if (cropY + cropHeight > bitmap.height) {
            cropHeight = bitmap.height - cropY
            cropWidth = (cropHeight * overlayAspectRatio).toInt()  // 비율 유지
        }

        // 이미지 잘라내기
        val croppedBitmap = Bitmap.createBitmap(bitmap, cropX, cropY, cropWidth, cropHeight)

        // 잘라낸 이미지를 외부 저장소에 저장
        if (bitmap != null) {
            ArImageStorage.bitmap = croppedBitmap
        } else {
            ImageStorage.bitmap = croppedBitmap
        }
        Log.e("jung", "이미지 저장됨")
    }
}