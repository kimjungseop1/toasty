package com.syncrown.arpang.ui.photoeditor

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatEditText
import com.syncrown.arpang.R
import com.syncrown.arpang.ui.component.home.tab1_home.free_print.PhotoEditorClient

/**
 * Created by Burhanuddin Rashid on 18/05/21.
 *
 * @author <https:></https:>//github.com/burhanrashid52>
 */
internal class BoxHelper(
    private val mPhotoEditorView: PhotoEditorView,
    private val mViewState: PhotoEditorViewState
) {
    fun clearHelperBox() {
        for (i in 0 until mPhotoEditorView.childCount) {
            val childAt = mPhotoEditorView.getChildAt(i)
            val frmBorder = childAt.findViewById<FrameLayout>(R.id.frmBorder)
            frmBorder?.setBackgroundResource(0)
            val editFocus = childAt.findViewById<AppCompatEditText>(R.id.tvPhotoEditorText)
            editFocus?.clearFocus()
            editFocus?.isEnabled = false
            val imgClose = childAt.findViewById<ImageView>(R.id.imgPhotoEditorClose)
            imgClose?.visibility = View.GONE
            val imgCrop = childAt.findViewById<ImageView>(R.id.imgPhotoEditorCrop)
            imgCrop?.visibility = View.GONE
            val drawingView = childAt.findViewById<PhotoEditorView>(R.id.imgPhotoEditorDraw)
            if (drawingView != null) {
                PhotoEditorClient.photoEditor?.setBrushDrawingMode(false)
            }
        }
        mViewState.clearCurrentSelectedView()
    }

    fun clearAllViews(drawingView: DrawingView?) {
        for (i in 0 until mViewState.addedViewsCount) {
            mPhotoEditorView.removeView(mViewState.getAddedView(i))
        }
        drawingView?.let {
            if (mViewState.containsAddedView(it)) {
                mPhotoEditorView.addView(it)
            }
        }

        mViewState.clearAddedViews()
        mViewState.clearRedoViews()
        drawingView?.clearAll()
    }
}