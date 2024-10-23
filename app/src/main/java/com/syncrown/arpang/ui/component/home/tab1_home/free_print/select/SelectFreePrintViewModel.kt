package com.syncrown.arpang.ui.component.home.tab1_home.free_print.select

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.syncrown.arpang.ui.base.BaseViewModel
import com.syncrown.arpang.ui.component.home.tab1_home.life2cut.image_select.adapter.ImageInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class SelectFreePrintViewModel : BaseViewModel() {
    private val _imageList = MutableLiveData<List<ImageInfo>>()
    val imageList: LiveData<List<ImageInfo>> = _imageList

    private val _folderNames = MutableLiveData<List<String>>()
    val folderNames: LiveData<List<String>> = _folderNames

    private val _selectedImages = MutableLiveData<MutableList<ImageInfo>>(mutableListOf())
    val selectedImages: MutableLiveData<MutableList<ImageInfo>> = _selectedImages

    fun loadFolderNames(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val folderMap = mutableMapOf<String, Int>()
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val selection = "${MediaStore.Images.Media.MIME_TYPE} IN (?, ?, ?)"
            val mimeTypes = arrayOf("image/jpeg", "image/png", "image/gif")
            val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
            val queryUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            val cursor = context.contentResolver.query(
                queryUri,
                projection,
                selection,
                mimeTypes,
                sortOrder
            )

            cursor?.use {
                val dataColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                while (it.moveToNext()) {
                    val filePath = it.getString(dataColumn)
                    val parentFolderName = File(filePath).parentFile?.name
                    if (parentFolderName != null) {
                        folderMap[parentFolderName] =
                            folderMap.getOrDefault(parentFolderName, 0) + 1
                    }
                }
            }

            _folderNames.postValue(folderMap.map { (folderName, count) -> "$folderName ($count)" })
        }
    }

    fun loadImageList(context: Context, folderName: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            val imageList = mutableListOf<ImageInfo>()
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATA
            )
            val mimeTypes = arrayOf("image/jpeg", "image/png", "image/gif")
            val selection: String
            val selectionArgs: Array<String>

            // 폴더 이름에 따라 쿼리 필터링
            if (folderName != null && !folderName.contains("전체보기")) {
                selection =
                    "${MediaStore.Images.Media.MIME_TYPE} IN (?, ?, ?) AND ${MediaStore.Images.Media.DATA} LIKE ?"
                selectionArgs = mimeTypes + arrayOf("%/$folderName/%")
            } else {
                selection = "${MediaStore.Images.Media.MIME_TYPE} IN (?, ?, ?)"
                selectionArgs = mimeTypes
            }

            val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
            val queryUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            val cursor = context.contentResolver.query(
                queryUri,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )

            cursor?.use {
                val dataColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

                while (it.moveToNext()) {
                    val filePath = it.getString(dataColumn)
                    val file = File(filePath)

                    if (file.exists()) {
                        val parentFolder = file.parentFile?.name

                        // 폴더 이름이 맞거나 전체보기를 선택한 경우에만 이미지 추가
                        if (folderName == null || parentFolder == folderName || folderName.contains("전체보기")) {
                            imageList.add(ImageInfo(filePath))
                        }
                    }
                }
            }

            _imageList.postValue(imageList)
        }
    }

    // 선택된 이미지를 추가하는 함수
    fun addSelectedImage(imageInfo: ImageInfo, context: Context) {
        _selectedImages.value?.let {
            it.add(imageInfo)
            _selectedImages.value = it
        }
    }
}