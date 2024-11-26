package com.syncrown.arpang.ui.component.home.tab1_home.ar_print.videoselect

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.syncrown.arpang.ui.base.BaseViewModel
import com.syncrown.arpang.ui.component.home.tab1_home.ar_print.videoselect.adapter.VideoInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class VideoSelectViewModel : BaseViewModel() {

    private val _videoList = MutableLiveData<List<VideoInfo>>()
    val videoList: LiveData<List<VideoInfo>> = _videoList

    private val _folderNames = MutableLiveData<List<String>>()
    val folderNames: LiveData<List<String>> = _folderNames

    fun loadFolderNames(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val folderMap = mutableMapOf<String, Int>()
            val projection = arrayOf(MediaStore.Video.Media.DATA)
            val selection = "${MediaStore.Video.Media.MIME_TYPE} = ?"
            val selectionArgs = arrayOf("video/mp4")
            val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"
            val queryUri: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

            val cursor = context.contentResolver.query(
                queryUri,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )

            cursor?.use {
                val dataColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
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

    fun loadVideoList(context: Context, folderName: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            val videoList = mutableListOf<VideoInfo>()
            val projection = arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DURATION
            )
            val selectionArgs: Array<String>
            val selection: String

            if (folderName != null && !folderName.contains("전체보기")) {
                selection =
                    "${MediaStore.Video.Media.MIME_TYPE} = ? AND ${MediaStore.Video.Media.DATA} LIKE ?"
                selectionArgs = arrayOf("video/mp4", "%/$folderName/%")
            } else {
                selection = "${MediaStore.Video.Media.MIME_TYPE} = ?"
                selectionArgs = arrayOf("video/mp4")
            }

            val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"
            val queryUri: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

            val cursor = context.contentResolver.query(
                queryUri,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )

            cursor?.use {
                val dataColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                val durationColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)

                while (it.moveToNext()) {
                    val filePath = it.getString(dataColumn)
                    var duration = it.getLong(durationColumn)

                    // duration이 0이거나 null인 경우 MediaMetadataRetriever로 확인
                    if (duration <= 0) {
                        val retriever = MediaMetadataRetriever()
                        try {
                            retriever.setDataSource(filePath)
                            val durationString =
                                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                            duration = durationString?.toLongOrNull() ?: 0
                        } catch (e: Exception) {
                            e.printStackTrace()
                        } finally {
                            retriever.release()
                        }
                    }

                    // 파일이 실제로 존재하는 경우에만 추가
                    if (File(filePath).exists() && duration > 0) {
                        videoList.add(VideoInfo(filePath, duration))
                    }
                }
            }

            _videoList.postValue(videoList)
        }
    }
}