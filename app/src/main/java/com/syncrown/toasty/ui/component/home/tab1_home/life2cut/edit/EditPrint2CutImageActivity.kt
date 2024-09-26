package com.syncrown.toasty.ui.component.home.tab1_home.life2cut.edit

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton.OnCheckedChangeListener
import com.syncrown.toasty.R
import com.syncrown.toasty.databinding.ActivityEditPrintTwoCutBinding
import com.syncrown.toasty.ui.base.BaseActivity
import com.syncrown.toasty.ui.component.home.tab1_home.ar_print.edit.adapter.StickerAdapter
import com.syncrown.toasty.ui.component.home.tab1_home.life2cut.crop.CropImageActivity
import com.syncrown.toasty.ui.component.home.tab1_home.life2cut.crop.ImageStorage
import com.syncrown.toasty.ui.component.home.tab1_home.life2cut.image_select.ImageSelectActivity
import com.syncrown.toasty.ui.component.home.tab1_home.life2cut.input_tag.InputTagActivity
import com.syncrown.toasty.ui.component.home.tab1_home.life2cut.preview.TwoCutPreviewActivity
import com.syncrown.toasty.ui.photoeditor.OnPhotoEditorListener
import com.syncrown.toasty.ui.photoeditor.PhotoEditor
import com.syncrown.toasty.ui.photoeditor.PhotoEditorView
import com.syncrown.toasty.ui.photoeditor.ViewType
import com.syncrown.toasty.ui.photoeditor.shape.ShapeBuilder
import kotlinx.coroutines.launch


class EditPrint2CutImageActivity : BaseActivity(), OnPhotoEditorListener,
    StickerAdapter.StickerListener {
    private lateinit var binding: ActivityEditPrintTwoCutBinding

    private lateinit var firstFilePath: String
    private lateinit var secondFilePath: String

    private var currentImage = -1

    private lateinit var mPhotoEditor: PhotoEditor
    private lateinit var mPhotoEditorView: PhotoEditorView
    private lateinit var mShapeBuilder: ShapeBuilder

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

    private fun setEditView() {
        binding.editorNavigation.menu.findItem(R.id.edit_1).setCheckable(false)
        binding.editorNavigation.menu.findItem(R.id.edit_2).setCheckable(false)
        binding.editorNavigation.menu.findItem(R.id.edit_3).setCheckable(false)
        binding.editorNavigation.menu.findItem(R.id.edit_4).setCheckable(false)

        binding.editorNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.edit_1 -> {
                    if (item.isCheckable) {
                        goImageSelect()
                        item.setCheckable(false)
                    } else {
                        item.setCheckable(true)
                    }
                    true
                }

                R.id.edit_2 -> {
                    if (item.isCheckable) {
                        mPhotoEditor.setBrushDrawingMode(false)
                        item.setCheckable(false)
                    } else {
                        mPhotoEditor.setBrushDrawingMode(true)
                        mShapeBuilder = ShapeBuilder()
                        mPhotoEditor.setShape(mShapeBuilder.withShapeColor(Color.WHITE))
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
                        item.setCheckable(true)
                    }
                    true
                }

                else -> false
            }
        }

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