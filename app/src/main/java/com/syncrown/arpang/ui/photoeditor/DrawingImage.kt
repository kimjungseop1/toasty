package com.syncrown.arpang.ui.photoeditor

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.syncrown.arpang.R
import com.syncrown.arpang.ui.component.home.tab1_home.free_print.PhotoEditorClient
import com.syncrown.arpang.ui.photoeditor.shape.ShapeBuilder

/**
 * Created by Burhanuddin Rashid on 14/05/21.
 *
 * @author <https:></https:>//github.com/burhanrashid52>
 */
internal class DrawingImage(
    private val mPhotoEditorView: PhotoEditorView,
    private val mMultiTouchListener: MultiTouchListener,
    private val mViewState: PhotoEditorViewState,
    graphicManager: GraphicManager?
) : Graphic(
    context = mPhotoEditorView.context,
    graphicManager = graphicManager,
    viewType = ViewType.IMAGE,
    layoutId = R.layout.view_photo_editor_image_3
) {
    private var mPhotoEditor: PhotoEditor
    private lateinit var mShapeBuilder: ShapeBuilder

    private lateinit var editorView: PhotoEditorView

    fun buildView() {
        mPhotoEditor.setBrushDrawingMode(true)
        mShapeBuilder = ShapeBuilder()
        mPhotoEditor.setShape(mShapeBuilder.withShapeColor(Color.WHITE))
    }

    private fun setupGesture() {
        val onGestureControl = buildGestureController(mPhotoEditorView, mViewState)
        mMultiTouchListener.setOnGestureControl(onGestureControl)
        val rootView = rootView
        rootView.setOnTouchListener(mMultiTouchListener)
    }

    override fun setupView(rootView: View) {
        editorView = rootView.findViewById(R.id.imgPhotoEditorDraw)
    }

    init {
        mPhotoEditor = PhotoEditor.Builder(context, editorView)
            .setPinchTextScalable(true)
            .build()
        editorView.source.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                android.R.color.transparent
            )
        )

        PhotoEditorClient.photoEditor = mPhotoEditor

        setupGesture()
    }
}