package ru.totowka.recyclerview.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_DRAG
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_IDLE
import androidx.recyclerview.widget.RecyclerView

class StorageItemTouchHelperCallback(val adapter: StorageItemTouchHelper) :
    ItemTouchHelper.Callback() {

    private var dragFromPosition = -1
    private var dragToPosition = -1

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        var dragFlags: Int = 0
        var swipeFlags: Int = 0

        when(viewHolder) {
            is StorageAdapter.BasketViewHolder -> {
                swipeFlags = ItemTouchHelper.START
            }
            is StorageAdapter.AppleViewHolder -> {
                swipeFlags = ItemTouchHelper.END
                dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            }
        }
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if(actionState == ACTION_STATE_DRAG) {
            viewHolder?.itemView?.animate()?.apply {
                scaleX(X_SCALE_TO)
                scaleY(Y_SCALE_TO)
                alpha(ALPHA_TO)
            }
            viewHolder?.also {dragFromPosition = it.adapterPosition}
        } else if(actionState == ACTION_STATE_IDLE) {
            if(dragFromPosition != -1 && dragToPosition != -1 && dragFromPosition != dragToPosition) {
                adapter.onItemMove(dragFromPosition, dragToPosition)
                dragFromPosition = -1
                dragToPosition = -1
            }
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        viewHolder.itemView.animate()?.apply {
            scaleX(X_SCALE_FROM)
            scaleY(Y_SCALE_FROM)
            alpha(ALPHA_FROM)
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return if(target is StorageAdapter.SumViewHolder) {
            false
        } else {
            dragToPosition = target.adapterPosition
            false
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.onItemDismiss(viewHolder.adapterPosition);
    }

    companion object {
        var X_SCALE_FROM = 1f
        var X_SCALE_TO = 1.1f
        var Y_SCALE_FROM = 1f
        var Y_SCALE_TO = 1.1f
        var ALPHA_TO = 0.9f
        var ALPHA_FROM = 1f
    }
}