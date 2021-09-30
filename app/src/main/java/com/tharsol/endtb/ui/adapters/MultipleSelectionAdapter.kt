package com.tharsol.endtb.ui.adapters

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.tharsol.endtb.R
import com.tharsol.endtb.databinding.LayoutItemCheckMultipleItemBinding
import com.tharsol.endtb.model.SingleItem
import java.util.*

class MultipleSelectionAdapter(listItems: List<SingleItem>) :
    RecyclerView.Adapter<MultipleSelectionAdapter.ViewHolder>(), Filterable {

    private var mListItems: List<SingleItem>
    private val mOrginal: List<SingleItem>
    var maxSelection = -1
    private var mSearchTag = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item_check_multiple_item, parent, false)
        return ViewHolder(LayoutItemCheckMultipleItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: SingleItem = mListItems[position]
        holder.binding?.textViewTitle?.setText(item.title, mSearchTag)
        holder.binding?.textViewDescription?.setVisibility(if (TextUtils.isEmpty(item.description)) View.GONE else View.VISIBLE)
        holder.binding?.linearLayoutTwoDescriptions!!.visibility =
            if (TextUtils.isEmpty(item.description1) && TextUtils.isEmpty(item.description2)) View.GONE else View.VISIBLE
        holder.binding?.textViewDescription1?.setVisibility(if (TextUtils.isEmpty(item.description1)) View.GONE else View.VISIBLE)
        holder.binding?.textViewDescription2?.setVisibility(if (TextUtils.isEmpty(item.description2)) View.GONE else View.VISIBLE)
        holder.binding?.textViewDescription?.setText(item.description, mSearchTag)
        holder.binding?.textViewDescription1?.setText(item.description1, mSearchTag)
        holder.binding?.textViewDescription2?.setText(item.description2, mSearchTag)
        holder.binding?.checkBox!!.isChecked = item.isSelected
        holder.binding?.root!!.setOnClickListener { v: View? ->
            val previousState: Boolean = item.isSelected
            item.isSelected = (!previousState)
            holder.binding?.checkBox!!.isChecked = !previousState
        }
        holder.binding?.checkBox!!.setOnClickListener { v: View? ->
            val previousState: Boolean = item.isSelected
            item.isSelected = !previousState
            holder.binding?.checkBox!!.isChecked = !previousState
        }
    }

    val selectedItems: List<SingleItem>
        get() {
            val items: MutableList<SingleItem> = ArrayList<SingleItem>()
            for (i in mOrginal) {
                if (i.isSelected) {
                    items.add(i)
                }
            }
            return items
        }

    fun setSearchTag(searchTag: String) {
        mSearchTag = searchTag
    }

    fun setItems(items: List<SingleItem>) {
        mListItems = items
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mListItems.size
    }

    override fun getFilter(): Filter {
        return MyFilter()
    }

    private inner class MyFilter : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val results = FilterResults()
            if (TextUtils.isEmpty(charSequence)) {
                results.count = mOrginal.size
                results.values = mOrginal
            } else {
                val filteredItems: MutableList<SingleItem> = ArrayList<SingleItem>()
                for (s in mOrginal) {
                    if (s.title?.toLowerCase()!!.contains(charSequence.toString().toLowerCase())
                        || !TextUtils.isEmpty(s.referenceId) && s.referenceId
                            ?.toLowerCase()!!.contains(
                                charSequence.toString().toLowerCase()
                            ) || !TextUtils.isEmpty(s.description) && s.description
                        !!.toLowerCase().contains(
                            charSequence.toString().toLowerCase()
                        ) || !TextUtils.isEmpty(s.description1) && s.description1
                        !!.toLowerCase().contains(
                            charSequence.toString().toLowerCase()
                        ) || !TextUtils.isEmpty(s.description2) && s.description2
                        !!.toLowerCase().contains(charSequence.toString().toLowerCase())
                    ) {
                        filteredItems.add(s)
                    }
                }
                results.values = filteredItems
                results.count = filteredItems.size
            }
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            mListItems = results.values as List<SingleItem>
            notifyDataSetChanged()
        }
    }

    class ViewHolder(var binding: LayoutItemCheckMultipleItemBinding?) : RecyclerView.ViewHolder(
        binding!!.root
    )

    init {
        mListItems = listItems
        mOrginal = listItems
    }
}