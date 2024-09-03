package com.syncrown.toasty.ui.commons

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

class CommonFunc {
    companion object {
        /**
         * 비디오 파일들이 있는 폴더들을 가져오는 함수
         */
        fun getVideoFolders(context: Context): List<String> {
            val folderNames = mutableSetOf<String>()

            // URI for external video content
            val uri: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

            // Projection (columns to return)
            val projection = arrayOf(
                MediaStore.Video.Media.DATA
            )

            // Query the content resolver for video files
            val cursor: Cursor? = context.contentResolver.query(
                uri,
                projection,
                null,
                null,
                null
            )

            cursor?.use {
                val dataIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)

                while (cursor.moveToNext()) {
                    val filePath = cursor.getString(dataIndex)
                    val folderPath = filePath.substring(0, filePath.lastIndexOf('/'))
                    val folderName = folderPath.substring(folderPath.lastIndexOf('/') + 1)
                    folderNames.add(folderName)
                }
            }

            return folderNames.toList()
        }

        /**
         * dp단위로 변경하는 함수
         */
        fun dpToPx(dp: Int, context: Context): Int {
            val density = context.resources.displayMetrics.density
            return Math.round(dp * density)
        }
    }
}