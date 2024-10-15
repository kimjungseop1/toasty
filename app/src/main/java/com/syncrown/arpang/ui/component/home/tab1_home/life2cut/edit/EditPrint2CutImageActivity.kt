package com.syncrown.arpang.ui.component.home.tab1_home.life2cut.edit

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityEditPrintTwoCutBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.commons.DialogCommon
import com.syncrown.arpang.ui.component.home.tab1_home.ar_print.edit.adapter.StickerAdapter
import com.syncrown.arpang.ui.component.home.tab1_home.life2cut.TwoCutImageStorage
import com.syncrown.arpang.ui.component.home.tab1_home.life2cut.crop.CropImageActivity
import com.syncrown.arpang.ui.component.home.tab1_home.life2cut.crop.ImageStorage
import com.syncrown.arpang.ui.component.home.tab1_home.life2cut.image_select.ImageSelectActivity
import com.syncrown.arpang.ui.component.home.tab1_home.life2cut.input_tag.InputTagActivity
import com.syncrown.arpang.ui.component.home.tab1_home.life2cut.preview.TwoCutPreviewActivity
import com.syncrown.arpang.ui.photoeditor.OnPhotoEditorListener
import com.syncrown.arpang.ui.photoeditor.OnSaveBitmap
import com.syncrown.arpang.ui.photoeditor.PhotoEditor
import com.syncrown.arpang.ui.photoeditor.PhotoEditorView
import com.syncrown.arpang.ui.photoeditor.SaveFileResult
import com.syncrown.arpang.ui.photoeditor.SaveSettings
import com.syncrown.arpang.ui.photoeditor.ViewType
import com.syncrown.arpang.ui.photoeditor.shape.ShapeBuilder
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


class EditPrint2CutImageActivity : BaseActivity(), OnPhotoEditorListener,
    StickerAdapter.StickerListener {
    private lateinit var binding: ActivityEditPrintTwoCutBinding

    private lateinit var firstFilePath: String
    private lateinit var secondFilePath: String

    private var currentImage = -1

    private lateinit var mPhotoEditor: PhotoEditor
    private lateinit var mPhotoEditorView: PhotoEditorView

    private lateinit var dialogCommon: DialogCommon
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            //소프트키 뒤로 버튼 처리
            dialogCommon.showEditCancel(supportFragmentManager, {
                //TODO 계속 편집
            }, {
                //TODO 편집 취소
                finish()
            })

        }
    }

    private lateinit var resultBitmap: Bitmap

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityEditPrintTwoCutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(callback)
        dialogCommon = DialogCommon()

        firstFilePath = intent.getStringExtra("FIRST_IMAGE_PATH").toString()
        secondFilePath = intent.getStringExtra("SECOND_IMAGE_PATH").toString()

        binding.actionbar.actionBack.setOnClickListener {
            dialogCommon.showEditCancel(supportFragmentManager, {
                //TODO 계속 편집
            }, {
                //TODO 편집 취소
                finish()
            })
        }
        binding.actionbar.actionTitle.text = "인쇄편집"
        binding.actionbar.actionEtc.text = "인쇄"
        binding.actionbar.actionEtc.setOnClickListener {
            mPhotoEditor.clearHelperBox()

            if (AppDataPref.isTwoCutPreView) {
                go2CutPreview()
            } else {
                resultBitmap = CommonFunc.getBitmapFromView(binding.resultImg)
                TwoCutImageStorage.bitmap = resultBitmap

                val isSuccess = saveBitmapToDownloadFolder(resultBitmap)
                if (isSuccess) {
                    Log.e("jung","이미지가 다운로드 폴더에 저장되었습니다.")
                    finish()
                } else {
                    Log.e("jung","이미지 저장에 실패했습니다.")
                }
            }
        }

        //에디터 초기화
        mPhotoEditorView = binding.photoEditorImageView
        mPhotoEditor = PhotoEditor.Builder(this, mPhotoEditorView)
            .setPinchTextScalable(true)
            .build()
        mPhotoEditor.setOnPhotoEditorListener(this)
        mPhotoEditorView.source.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                android.R.color.transparent
            )
        )

        setImageInit()

        lifecycleScope.launch {
            setEditView()
        }
    }

    private fun saveBitmapToDownloadFolder(bitmap: Bitmap): Boolean {
        val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val fileName = "${System.currentTimeMillis()}.png"
        val file = File(downloadDir, fileName)

        return try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    private fun setEditView() {
        binding.editorNavigation.menu.findItem(R.id.edit_1).setCheckable(false)
        binding.editorNavigation.menu.findItem(R.id.edit_2).setCheckable(false)
        binding.editorNavigation.menu.findItem(R.id.edit_3).setCheckable(false)
        binding.editorNavigation.menu.findItem(R.id.edit_4).setCheckable(false)

        binding.editorNavigation.setOnItemSelectedListener { item ->
            mPhotoEditor.setBrushDrawingMode(false)
            binding.stickerLayout.root.visibility = View.GONE
            binding.etcContentView.root.visibility = View.GONE

            when (item.itemId) {
                R.id.edit_1 -> {
                    if (item.isCheckable) {
                        item.setCheckable(false)
                    } else {
                        goImageSelect()
                        item.setCheckable(true)
                    }
                    true
                }

                R.id.edit_2 -> {
                    if (item.isCheckable) {
                        item.setCheckable(false)
                    } else {
                        mPhotoEditor.addDrawing()
                        item.setCheckable(true)
                    }
                    true
                }

                R.id.edit_3 -> {
                    if (item.isCheckable) {
                        binding.stickerLayout.root.visibility = View.GONE
                        item.setCheckable(false)
                    } else {
                        binding.stickerLayout.root.visibility = View.VISIBLE
                        val gridLayoutManager = GridLayoutManager(this, 3)
                        binding.stickerLayout.rvEmoji.layoutManager = gridLayoutManager
                        val stickerAdapter = StickerAdapter(this)
                        binding.stickerLayout.rvEmoji.adapter = stickerAdapter
                        binding.stickerLayout.rvEmoji.setHasFixedSize(true)
                        stickerAdapter.setStickerListener(this)

                        item.setCheckable(true)
                    }

                    true
                }

                R.id.edit_4 -> {
                    if (item.isCheckable) {
                        binding.etcContentView.root.visibility = View.GONE
                        item.setCheckable(false)
                    } else {
                        binding.etcContentView.root.visibility = View.VISIBLE

                        binding.etcContentView.publicSwitch.setOnCheckedChangeListener { _, isChecked ->
                            if (isChecked) {
                                // 공개
                            } else {
                                // 비공개
                            }
                        }

                        binding.etcContentView.tagSetting.setOnClickListener {
                            goInputTag()
                        }

                        binding.etcContentView.preview.setOnClickListener {
                            go2CutPreview()
                        }

                        item.setCheckable(true)
                    }
                    true
                }

                else -> false
            }
        }

    }


    private fun setImageInit() {
        Glide.with(this).load(firstFilePath).into(binding.firstImageView)
        Glide.with(this).load(secondFilePath).into(binding.secondImageView)

        if (mPhotoEditor.brushDrawableMode == true) {
            binding.firstImageView.setOnClickListener(null)
            binding.secondImageView.setOnClickListener(null)
        } else {
            binding.firstImageView.setOnClickListener {
                goCropImage(firstFilePath)
                currentImage = 1
            }

            binding.secondImageView.setOnClickListener {
                goCropImage(secondFilePath)
                currentImage = 2
            }
        }

    }

    private fun goCropImage(filePath: String) {
        val intent = Intent(this, CropImageActivity::class.java)
        intent.putExtra("CROP_IMAGE_FILE_PATH", filePath)
        cropImageLauncher.launch(intent)
    }

    private fun goImageSelect() {
        val intent = Intent(this, ImageSelectActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goInputTag() {
        val intent = Intent(this, InputTagActivity::class.java)
        startActivity(intent)
    }

    private fun go2CutPreview() {
        mPhotoEditor.clearHelperBox()

        resultBitmap = CommonFunc.getBitmapFromView(binding.resultImg)
        TwoCutImageStorage.bitmap = resultBitmap

        val intent = Intent(this, TwoCutPreviewActivity::class.java)
        startActivity(intent)
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

    override fun onEditTextChangeListener(rootView: View, text: String, colorCode: Int) {}

    override fun onAddViewListener(viewType: ViewType, numberOfAddedViews: Int) {}

    override fun onRemoveViewListener(viewType: ViewType, numberOfAddedViews: Int) {}

    override fun onStartViewChangeListener(viewType: ViewType) {
        Log.d(TAG, "onStartViewChangeListener() called with: viewType = [$viewType]")
    }

    override fun onStopViewChangeListener(viewType: ViewType) {
        Log.d(TAG, "onStopViewChangeListener() called with: viewType = [$viewType]")
    }

    override fun onTouchSourceImage(event: MotionEvent) {
        Log.d(TAG, "onTouchView() called with: event = [$event]")
    }

    override fun onStickerClick(bitmap: Bitmap) {
        mPhotoEditor.addImage(bitmap)
    }
}