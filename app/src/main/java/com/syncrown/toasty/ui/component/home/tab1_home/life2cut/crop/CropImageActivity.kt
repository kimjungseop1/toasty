package com.syncrown.toasty.ui.component.home.tab1_home.life2cut.crop

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.lifecycle.lifecycleScope
import com.syncrown.toasty.databinding.ActivityCropImageBinding
import com.syncrown.toasty.ui.base.BaseActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CropImageActivity : BaseActivity() {
    private lateinit var binding: ActivityCropImageBinding
    private lateinit var filePath: String

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityCropImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        filePath = intent.getStringExtra("CROP_IMAGE_FILE_PATH").toString()

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = "이미지 조정"
        binding.actionbar.actionEtc.text = "확인"
        binding.actionbar.actionEtc.setOnClickListener {
            lifecycleScope.launch {
                val cropped: Bitmap? = withContext(Dispatchers.Default) {
                    binding.cropImageView.getCroppedImage()
                }

                //싱글톤 사용(크롭된 이미지를 이전 화면의 이미지뷰에 띄우자, intent로 전달시 용량 크기 이슈있음)
                ImageStorage.bitmap = cropped

                setResult(RESULT_OK)
                finish()
            }
        }

        setImage()
    }

    private fun setImage() {
        binding.cropImageView.setImageUriAsync(path2uri(this, filePath))

    }

    @SuppressLint("Range", "Recycle")
    fun path2uri(context: Context, filePath: String): Uri {
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
            "_data = '$filePath'", null, null
        )

        cursor!!.moveToNext()
        val id = cursor.getInt(cursor.getColumnIndex("_id"))
        val uri =
            ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toLong())

        return uri
    }
}