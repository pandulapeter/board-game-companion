package com.rbtgames.boardgame.feature.playerDetail.list

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.utils.dimension

class ColorItemDecoration(private val context: Context) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent.getChildAdapterPosition(view) != 0) {
            outRect.left = context.dimension(R.dimen.content_padding)
        }
    }
}