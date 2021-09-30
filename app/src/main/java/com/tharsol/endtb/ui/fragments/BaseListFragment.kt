package com.tharsol.endtb.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

abstract class BaseListFragment : Fragment() {

    protected var list: RecyclerView? = null
    protected var swipe: SwipeRefreshLayout? = null
    var empty: TextView? = null
    var fab: FloatingActionButton? = null;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        empty!!.visibility = View.GONE
        setEnableSwipe(false)
        if (showFab()) fab?.show();
        list!!.layoutManager = LinearLayoutManager(context)
        setAdaptor()
        loadData()
    }

    /**
     * this function should be call in onViewCreated before super of  onViewCreated
     */
    protected fun updateViews(
        list: RecyclerView?,
        swipe: SwipeRefreshLayout? = null,
        empty: TextView? = null, fab: FloatingActionButton? = null
    ) {
        this.list = list
        this.swipe = swipe
        this.empty = empty
    }

    protected open fun setAdaptor() {}
    protected abstract fun loadData()
    protected abstract fun showFab(): Boolean

    protected fun setEnableSwipe(enable: Boolean) {
        swipe!!.isEnabled = enable
    }


}