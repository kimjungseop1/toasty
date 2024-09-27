package com.syncrown.arpang.ui.commons

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HorizontalSpaceItemDecoration(private val spaceWidth: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.right = spaceWidth

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = spaceWidth
        }
    }
}
