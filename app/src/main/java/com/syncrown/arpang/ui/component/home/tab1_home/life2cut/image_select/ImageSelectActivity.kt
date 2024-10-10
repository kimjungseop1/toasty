package com.syncrown.arpang.ui.component.home.tab1_home.life2cut.image_select

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.syncrown.arpang.databinding.ActivityImageSelectBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.commons.GridSpacingItemDecoration
import com.syncrown.arpang.ui.commons.HorizontalSpaceItemDecoration
import com.syncrown.arpang.ui.component.home.tab1_home.life2cut.edit.EditPrint2CutImageActivity
import com.syncrown.arpang.ui.component.home.tab1_home.life2cut.image_select.adapter.ImageInfo
import com.syncrown.arpang.ui.component.home.tab1_home.life2cut.image_select.adapter.ImageSelectGridItemAdapter
import com.syncrown.arpang.ui.component.home.tab1_home.life2cut.image_select.adapter.SelectedImageListAdapter

class ImageSelectActivity : BaseActivity() {
    private lateinit var binding: ActivityImageSelectBinding
    private val imageSelectViewModel: ImageSelectViewModel by viewModels()

    override fun observeViewModel() {
        imageSelectViewModel.folderNames.observe(this) { folderNames ->
            setSpinner(folderNames)
        }

        imageSelectViewModel.imageList.observe(this) { imageList ->
            setImageList(imageList)
        }

        imageSelectViewModel.selectedImages.observe(this) { selectedImages ->
            setSelectedImageList(selectedImages)
        }
    }

    override fun initViewBinding() {
        binding = ActivityImageSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = "인생 두컷"
        binding.actionbar.actionEtc.text = "만들기"
        binding.actionbar.actionEtc.setOnClickListener {
            goEditImagePrint()
        }

        imageSelectViewModel.loadFolderNames(this)
        imageSelectViewModel.loadImageList(this)

        updateView()
    }

    private fun setSelectedImageList(selectedImages: MutableList<ImageInfo>) {
        binding.recyclerAddView.itemDecorationCount.let { count ->
            for (i in 0 until count) {
                binding.recyclerAddView.removeItemDecorationAt(0)
            }
        }

        val selectAdapter = SelectedImageListAdapter(this, selectedImages)
        binding.recyclerAddView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerAddView.addItemDecoration(
            HorizontalSpaceItemDecoration(
                CommonFunc.dpToPx(
                    6,
                    this
                )
            )
        )
        selectAdapter.setOnItemClickListener(object : SelectedImageListAdapter.OnItemClickListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onClick(imageInfo: ImageInfo) {
                imageSelectViewModel.removeSelectedImage(imageInfo)

                updateView()
            }
        })
        binding.recyclerAddView.adapter = selectAdapter
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

        binding.recyclerImage.itemDecorationCount.let { count ->
            for (i in 0 until count) {
                binding.recyclerImage.removeItemDecorationAt(0)
            }
        }

        binding.recyclerImage.layoutManager = GridLayoutManager(this, spanCount)
        binding.recyclerImage.addItemDecoration(
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
                @SuppressLint("NotifyDataSetChanged")
                override fun onItemClick(imageInfo: ImageInfo) {
                    imageSelectViewModel.addSelectedImage(imageInfo, this@ImageSelectActivity)

                    updateView()
                }
            })

        binding.recyclerImage.adapter = adapter
    }

    private fun updateView() {
        if (imageSelectViewModel.currentSelectCnt() == 0) {
            binding.recyclerAddView.visibility = View.GONE
            binding.emptyAddView.visibility = View.VISIBLE
        } else {
            binding.recyclerAddView.visibility = View.VISIBLE
            binding.emptyAddView.visibility = View.GONE
        }
    }

    private fun goEditImagePrint() {
        val selectedImages = imageSelectViewModel.selectedImages.value

        val firstFilePath: String? = selectedImages?.getOrNull(0)?.filePath
        val secondFilePath: String? = selectedImages?.getOrNull(1)?.filePath

        if (firstFilePath != null && secondFilePath != null) {
            val intent = Intent(this, EditPrint2CutImageActivity::class.java)
            intent.putExtra("FIRST_IMAGE_PATH", firstFilePath)
            intent.putExtra("SECOND_IMAGE_PATH", secondFilePath)
            startActivity(intent)
            finish()
        } else {
            Log.e("jung", "이미지 선택되지 않았음")
        }
    }
}