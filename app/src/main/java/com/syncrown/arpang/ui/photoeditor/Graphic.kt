package com.syncrown.arpang.ui.photoeditor

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.syncrown.arpang.R
import com.syncrown.arpang.ui.component.home.tab1_home.free_print.PhotoEditorClient
import com.syncrown.arpang.ui.photoeditor.MultiTouchListener.OnGestureControl

/**
 * Created by Burhanuddin Rashid on 14/05/21.
 *
 * @author <https:></https:>//github.com/burhanrashid52>
 */
internal abstract class Graphic(
    val context: Context,
    val layoutId: Int,
    val viewType: ViewType,
    val graphicManager: GraphicManager?) {

    val rootView: View

    open fun updateView(view: View) {
        //Optional for subclass to override
    }

    init {
        if (layoutId == 0) {
            throw UnsupportedOperationException("Layout id cannot be zero. Please define a layout")
        }
        rootView = LayoutInflater.from(context).inflate(layoutId, null)
        setupView(rootView)
        setupRemoveView(rootView)
    }


    private fun setupRemoveView(rootView: View) {
        //We are setting tag as ViewType to identify what type of the view it is
        //when we remove the view from stack i.e onRemoveViewListener(ViewType viewType, int numberOfAddedViews);
        rootView.tag = viewType
        val imgClose = rootView.findViewById<ImageView>(R.id.imgPhotoEditorClose)
        imgClose?.setOnClickListener { graphicManager?.removeView(this@Graphic) }
    }

    @SuppressLint("ClickableViewAccessibility")
    protected fun toggleSelection() {
        val frmBorder = rootView.findViewById<View>(R.id.frmBorder)
        val imgClose = rootView.findViewById<View>(R.id.imgPhotoEditorClose)
        val imgCrop = rootView.findViewById<View>(R.id.imgPhotoEditorCrop)
        val editText = rootView.findViewById<View>(R.id.tvPhotoEditorText)
        val draws = rootView.findViewById<View>(R.id.imgPhotoEditorDraw)

        if (frmBorder != null) {
            frmBorder.setBackgroundResource(R.drawable.border_dash_stroke_3dp)
            frmBorder.tag = true
        }
        if (imgClose != null) {
            imgClose.visibility = View.VISIBLE
        }
        if (imgCrop != null) {
            imgCrop.visibility = View.VISIBLE
        }
//        if (draws != null) {
//            PhotoEditorClient.photoEditor?.setBrushDrawingMode(true)
//        }

        editText?.requestFocus()
        editText?.isEnabled = true
    }

    protected fun buildGestureController(
        photoEditorView: PhotoEditorView,
        viewState: PhotoEditorViewState
    ): OnGestureControl {
        val boxHelper = BoxHelper(photoEditorView, viewState)
        return object : OnGestureControl {
            override fun onClick() {
                boxHelper.clearHelperBox()
                toggleSelection()
                // Change the in-focus view
                viewState.currentSelectedView = rootView
            }

            override fun onLongClick() {
                updateView(rootView)
            }
        }
    }

    open fun setupView(rootView: View) {}
}