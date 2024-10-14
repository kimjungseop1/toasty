package com.syncrown.arpang.ui.photoeditor

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.syncrown.arpang.R
import com.yalantis.ucrop.UCrop
import java.io.File
import java.io.FileOutputStream

/**
 * Created by Burhanuddin Rashid on 14/05/21.
 *
 * @author <https:></https:>//github.com/burhanrashid52>
 */
internal class SelectImage(
    private val activity: Activity,
    private val mPhotoEditorView: PhotoEditorView,
    private val mMultiTouchListener: MultiTouchListener,
    private val mViewState: PhotoEditorViewState,
    graphicManager: GraphicManager?
) : Graphic(
    context = mPhotoEditorView.context,
    graphicManager = graphicManager,
    viewType = ViewType.IMAGE,
    layoutId = R.layout.view_photo_editor_image_2
) {
    private var imageView: ImageView? = null
    private var changeImage: AppCompatImageView? = null
    private var currentImageBitmap: Bitmap? = null

    fun buildView(desiredImage: Bitmap?) {
        currentImageBitmap = desiredImage

        imageView?.setImageBitmap(desiredImage)
    }

    private fun setupGesture() {
        val onGestureControl = buildGestureController(mPhotoEditorView, mViewState)
        mMultiTouchListener.setOnGestureControl(onGestureControl)
        val rootView = rootView
        rootView.setOnTouchListener(mMultiTouchListener)
    }

    override fun setupView(rootView: View) {
        imageView = rootView.findViewById(R.id.imgPhotoEditorImage)
        changeImage = rootView.findViewById(R.id.imgPhotoEditorCrop)
    }

    init {
        setupGesture()

        changeImage?.setOnClickListener {
            currentImageBitmap?.let {
                // 이미지 크롭 액티비티 시작
                val sourceUri = getImageUri(it)
                val destinationUri = Uri.fromFile(File(context.cacheDir, "cropped_image.jpg"))

                UCrop.of(sourceUri, destinationUri)
                    .withAspectRatio(1f, 1f)
                    .withMaxResultSize(500, 500)
                    .start(activity)
            }

        }
    }

    private fun getImageUri(bitmap: Bitmap): Uri {
        val file = File(context.cacheDir, "temp_image.jpg")
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.flush()
        stream.close()
        return Uri.fromFile(file)
    }
}