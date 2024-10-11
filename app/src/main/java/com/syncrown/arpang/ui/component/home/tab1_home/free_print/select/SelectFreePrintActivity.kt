package com.syncrown.arpang.ui.component.home.tab1_home.free_print.select

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.syncrown.arpang.databinding.ActivitySelectFreeImageBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.commons.GridSpacingItemDecoration
import com.syncrown.arpang.ui.component.home.tab1_home.free_print.EditFreePrintActivity
import com.syncrown.arpang.ui.component.home.tab1_home.free_print.FreeImageStorage
import com.syncrown.arpang.ui.component.home.tab1_home.life2cut.crop.ImageStorage
import com.syncrown.arpang.ui.component.home.tab1_home.life2cut.image_select.adapter.ImageInfo
import com.syncrown.arpang.ui.component.home.tab1_home.life2cut.image_select.adapter.ImageSelectGridItemAdapter

class SelectFreePrintActivity : BaseActivity() {
    private lateinit var binding: ActivitySelectFreeImageBinding
    private val imageSelectViewModel: SelectFreePrintViewModel by viewModels()

    override fun observeViewModel() {
        imageSelectViewModel.folderNames.observe(this) { folderNames ->
            setSpinner(folderNames)
        }

        imageSelectViewModel.imageList.observe(this) { imageList ->
            setImageList(imageList)
        }
    }

    override fun initViewBinding() {
        binding = ActivitySelectFreeImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }
        binding.actionbar.actionTitle.text = "자유인쇄"

        imageSelectViewModel.loadFolderNames(this)
        imageSelectViewModel.loadImageList(this)
    }

    private fun setSpinner(folderNames: List<String>) {
        val arrayList = ArrayList<String>()
        arrayList.add(
            "전체보기" + " (" + folderNames.sumOf {
                it.substringAfterLast(" (").substringBeforeLast(")").toInt()
            } + ")"
        )
        arrayList.addAll(folderNames)

        binding.folderSpinner.attachDataSource(arrayList)
        binding.folderSpinner.selectedIndex = 0
        binding.folderSpinner.setOnSpinnerItemSelectedListener { parent, view, position, id ->
            val selectedFolder = parent.getItemAtPosition(position).toString().split(" (")[0]
            imageSelectViewModel.loadImageList(this, selectedFolder)
        }
    }

    private fun setImageList(imageList: List<ImageInfo>) {
        val spanCount = 4
        val spacing = 6
        val includeEdge = false

        binding.recyclerImg.itemDecorationCount.let { count ->
            for (i in 0 until count) {
                binding.recyclerImg.removeItemDecorationAt(0)
            }
        }

        binding.recyclerImg.layoutManager = GridLayoutManager(this, spanCount)
        binding.recyclerImg.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                CommonFunc.dpToPx(spacing, this),
                includeEdge
            )
        )

        val adapter = ImageSelectGridItemAdapter(
            this,
            imageList,
            object : ImageSelectGridItemAdapter.OnItemClickListener {
                override fun onItemClick(imageInfo: ImageInfo) {
                    imageSelectViewModel.addSelectedImage(imageInfo, this@SelectFreePrintActivity)

                    val exif = ExifInterface(imageInfo.filePath)
                    val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

                    val rotation = when (orientation) {
                        ExifInterface.ORIENTATION_ROTATE_90 -> 90
                        ExifInterface.ORIENTATION_ROTATE_180 -> 180
                        ExifInterface.ORIENTATION_ROTATE_270 -> 270
                        else -> 0
                    }

                    val bitmap = BitmapFactory.decodeFile(imageInfo.filePath)
                    val rotatedBitmap = if (rotation != 0) {
                        val matrix = Matrix()
                        matrix.postRotate(rotation.toFloat())
                        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                    } else {
                        bitmap
                    }

                    FreeImageStorage.bitmap = rotatedBitmap

                    setResult(RESULT_OK)
                    finish()
                }
            })

        binding.recyclerImg.adapter = adapter
    }


}