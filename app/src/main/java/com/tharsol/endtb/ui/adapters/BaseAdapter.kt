package com.tharsol.endtb.ui.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T : RecyclerView.ViewHolder?> : RecyclerView.Adapter<T>() {
    private var empty: View? = null
    fun setEmpty(empty: View?) {
        this.empty = empty
    }

    protected fun checkEmptyView(size: Int) {
        if (empty != null) empty!!.visibility = if (size == 0) View.VISIBLE else View.GONE
    }

    companion object {
        const val TYPE_CONTENT = 0
        const val TYPE_FOOTER = 1
    }
}