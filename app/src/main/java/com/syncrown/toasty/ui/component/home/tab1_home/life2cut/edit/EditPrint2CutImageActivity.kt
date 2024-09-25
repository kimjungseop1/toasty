package com.syncrown.toasty.ui.component.home.tab1_home.life2cut.edit

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.syncrown.toasty.databinding.ActivityEditPrintTwoCutBinding
import com.syncrown.toasty.ui.base.BaseActivity
import com.syncrown.toasty.ui.component.home.tab1_home.life2cut.crop.ImageStorage
import com.syncrown.toasty.ui.component.home.tab1_home.life2cut.crop.CropImageActivity

class EditPrint2CutImageActivity : BaseActivity() {
    private lateinit var binding: ActivityEditPrintTwoCutBinding

    private lateinit var firstFilePath: String
    private lateinit var secondFilePath: String

    private var currentImage = -1

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityEditPrintTwoCutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firstFilePath = intent.getStringExtra("FIRST_IMAGE_PATH").toString()
        secondFilePath = intent.getStringExtra("SECOND_IMAGE_PATH").toString()

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = "인쇄편집"
        binding.actionbar.actionEtc.text = "인쇄"

        setImageInit()

        setEditView()
    }

    private fun setEditView() {
        binding.changeImageView.setOnClickListener {

        }

        binding.drawingView.setOnClickListener {

        }

        binding.stickerView.setOnClickListener {

        }

        binding.editView.setOnClickListener {

        }
    }

    private fun setImageInit() {
        Glide.with(this).load(firstFilePath).into(binding.firstImageView)
        Glide.with(this).load(secondFilePath).into(binding.secondImageView)

        binding.firstImageView.setOnClickListener {
            goCropImage(firstFilePath)
            currentImage = 1
        }

        binding.secondImageView.setOnClickListener {
            goCropImage(secondFilePath)
            currentImage = 2
        }
    }

    private fun goCropImage(filePath: String) {
        val intent = Intent(this, CropImageActivity::class.java)
        intent.putExtra("CROP_IMAGE_FILE_PATH", filePath)
        cropImageLauncher.launch(intent)
    }

    private val cropImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val cropBitmap = ImageStorage.bitmap

                if (currentImage == 1) {
                    Glide.with(this).load(cropBitmap).into(binding.firstImageView)
                } else {
                    Glide.with(this).load(cropBitmap).into(binding.secondImageView)
                }
            }
        }
}