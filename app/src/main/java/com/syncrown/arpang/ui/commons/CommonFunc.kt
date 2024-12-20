package com.syncrown.arpang.ui.commons

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.view.View
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Locale

class CommonFunc {
    companion object {
        /**
         * 디바이스 아이디 가져오기
         */
        @SuppressLint("HardwareIds")
        fun getDeviceUuid(context: Context): String? {
            return Settings.Secure.getString(
                context.contentResolver, Settings.Secure.ANDROID_ID
            )
        }

        /**
         * dp단위로 변경하는 함수
         */
        fun dpToPx(dp: Int, context: Context): Int {
            val density = context.resources.displayMetrics.density
            return Math.round(dp * density)
        }

        fun dpToPx(dp: Float, context: Context): Float {
            val density = context.resources.displayMetrics.density
            return dp * density
        }

        fun pxToDp(context: Context, px: Float): Float {
            return px / context.resources.displayMetrics.density
        }

        /**
         * millisecond를 변경
         */
        @SuppressLint("DefaultLocale")
        fun convertMillisToMMSS(milliseconds: Long): String {
            val seconds = milliseconds / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val remainingMinutes = minutes % 60
            val remainingSeconds = seconds % 60

            return if (hours > 0) {
                // 1시간 이상인 경우 HH:MM:SS 형식으로 반환
                String.format("%02d:%02d:%02d", hours, remainingMinutes, remainingSeconds)
            } else {
                // 1시간 미만인 경우 MM:SS 형식으로 반환
                String.format("%02d:%02d", remainingMinutes, remainingSeconds)
            }
        }

        /**
         * 캐시에 저장된 비디오파일들 제거
         */
        fun clearAppCache(context: Context) {
            try {
                val cacheDir = context.cacheDir
                if (cacheDir != null && cacheDir.isDirectory) {
                    deleteDir(cacheDir)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun deleteDir(dir: File?): Boolean {
            if (dir != null && dir.isDirectory) {
                val children = dir.list()
                if (children != null) {
                    for (child in children) {
                        val success = deleteDir(File(dir, child))
                        if (!success) {
                            return false
                        }
                    }
                }
            }
            return dir?.delete() ?: false
        }

        fun getFileFromUriWithCheck(context: Context, uri: Uri): File? {
            return if (uri.toString().startsWith("content://")) {
                val filePathColumn = arrayOf(MediaStore.Video.Media.DATA)
                var filePath: String? = null

                context.contentResolver.query(uri, filePathColumn, null, null, null)?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                        filePath = cursor.getString(columnIndex)
                    }
                }

                filePath?.let { File(it) }
            } else {
                // 만약 content://가 아닌 경우, 그대로 반환하거나 예외 처리
                null
            }
        }

        /**
         * 비트맵 이미지를 서버에 전송하기전 Base64인코딩
         */
        fun bitmapToBase64(bitmap: Bitmap): String {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            val byteArray = outputStream.toByteArray()
            return Base64.encodeToString(byteArray, Base64.DEFAULT)
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
                ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id.toLong()
                )

            return uri
        }

        fun getBitmapFromView(view: View): Bitmap {
            view.measure(
                View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(view.height, View.MeasureSpec.EXACTLY)
            )
            val width = view.measuredWidth
            val height = view.measuredHeight
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

            val canvas = Canvas(bitmap)
            canvas.translate(-view.scrollX.toFloat(), -view.scrollY.toFloat())
            view.draw(canvas)

            return bitmap
        }

        fun byteArrToHex(inBytArr: ByteArray): String {
            val strBuilder = StringBuilder()
            val j = inBytArr.size
            for (i in 0 until j) {
                strBuilder.append(byte2Hex(inBytArr[i]))
                strBuilder.append(":")
            }
            return strBuilder.toString()
        }

        private fun byte2Hex(inByte: Byte): String {
            return String.format("%02x", inByte).uppercase(Locale.getDefault())
        }
    }
}