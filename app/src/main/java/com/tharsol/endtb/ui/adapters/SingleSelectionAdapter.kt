package com.sameelenterprises.mrep.adapters.searchablelisting

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.tharsol.endtb.R
import com.tharsol.endtb.databinding.LayoutItemRadioSingleItemBinding
import com.tharsol.endtb.databinding.LayoutItemSingleItemBinding
import com.tharsol.endtb.model.SingleItem

open class SingleSelectionAdapter protected constructor(
    listItems: List<SingleItem>,
    showRadioSelection: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    private val mListOriginalItems: List<SingleItem>
    private var mListItems: ArrayList<SingleItem>? = ArrayList()
    var selectedItem: SingleItem? = null
        private set
    private val mShowRadioSelection: Boolean
    var searchTag = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (mShowRadioSelection) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_item_radio_single_item, parent, false)
            return RadioItemViewHolder(LayoutItemRadioSingleItemBinding.bind(view))
        }
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item_single_item, parent, false)
        return ItemViewHolder(LayoutItemSingleItemBinding.bind(view))
    }

    override fun onBindViewHolder(rHolder: RecyclerView.ViewHolder, position: Int) {
        val item = mListItems!![position]
        if (mShowRadioSelection) {
            val holder = rHolder as RadioItemViewHolder
            holder.binding?.textViewTitle?.text = item.title
            holder.binding?.textViewTitle?.setTextToHighlight(searchTag)
            holder.binding?.textViewDescription?.visibility =
                if (TextUtils.isEmpty(item.description)) View.GONE else View.VISIBLE
            holder.binding?.linearLayoutTwoDescriptions?.visibility =
                if (TextUtils.isEmpty(item.description1) && TextUtils.isEmpty(item.description2)) View.GONE else View.VISIBLE
            holder.binding?.textViewDescription1?.visibility =
                if (TextUtils.isEmpty(item.description1)) View.GONE else View.VISIBLE
            holder.binding?.textViewDescription2?.visibility =
                if (TextUtils.isEmpty(item.description2)) View.GONE else View.VISIBLE
            holder.binding?.textViewDescription?.text = item.description
            holder.binding?.textViewDescription!!.setTextToHighlight(searchTag)
            holder.binding.textViewDescription1!!.text = item.description1
            holder.binding.textViewDescription1.setTextToHighlight(searchTag)
            holder.binding.textViewDescription2.text = item.description2
            holder.binding.textViewDescription2.setTextToHighlight(searchTag)

            holder.binding.root.setOnClickListener {
                selectedItem = item
                holder.binding.radioSingle.isChecked = true
                notifyDataSetChanged()
                onItemSelected(selectedItem)
            }
            holder.binding.radioSingle.setOnClickListener {
                selectedItem = item
                holder.binding.radioSingle.isChecked = true
                notifyDataSetChanged()
            }
            holder.binding.radioSingle.isChecked = item == selectedItem
        } else {
            val holder = rHolder as ItemViewHolder
            holder.binding!!.textViewTitle!!.text = item.title
            holder.binding.textViewTitle!!.setTextToHighlight(searchTag)
            holder.binding.textViewDescription!!.visibility =
                if (TextUtils.isEmpty(item.description)) View.GONE else View.VISIBLE
            holder.binding.linearLayoutTwoDescriptions!!.visibility =
                if (TextUtils.isEmpty(item.description1) && TextUtils.isEmpty(item.description2)) View.GONE else View.VISIBLE
            holder.binding?.textViewDescription1.visibility =
                if (TextUtils.isEmpty(item.description1)) View.GONE else View.VISIBLE
            holder.binding?.textViewDescription2.visibility =
                if (TextUtils.isEmpty(item.description2)) View.GONE else View.VISIBLE
            holder.binding.textViewDescription.text = item.description
            holder.binding.textViewDescription.setTextToHighlight(searchTag)
            holder.binding.textViewDescription1.text = item.description1
            holder.binding.textViewDescription1.setTextToHighlight(searchTag)
            holder.binding.textViewDescription2.text = item.description2
            holder.binding.textViewDescription2.setTextToHighlight(searchTag)
            holder.binding.root.setOnClickListener {
                selectedItem = item
                onItemSelected(selectedItem)
            }
        }
    }


//    fun setItems(items: List<SingleItem>?) {
//        mListItems = items
//        notifyDataSetChanged()
//    }

    override fun getItemCount(): Int {
        return if (mListItems == null) 0 else mListItems!!.size
    }

    override fun getFilter(): Filter {
        return MyFilter()
    }

    internal inner class MyFilter : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val results = FilterResults()
            if (TextUtils.isEmpty(constraint)) {
                results.count = mListOriginalItems.size
                results.values = mListOriginalItems
            } else {
                val items = mListOriginalItems.filter {
                    it.title != null && it.title!!.contains(constraint, true)
                            || it.description != null && it.description!!.contains(constraint, true)
                            || it.description1 != null && it.description1!!.contains(
                        constraint,
                        true
                    )
                            || it.description2 != null && it.description2!!.contains(
                        constraint,
                        true
                    )
                            || it.description2 != null && it.description2!!.contains(
                        constraint,
                        true
                    )
                }
                results.count = items.size
                results.values = items
            }
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            mListItems = results.values as ArrayList<SingleItem>
            notifyDataSetChanged()
        }
    }

    internal class RadioItemViewHolder(val binding: LayoutItemRadioSingleItemBinding?) :
        RecyclerView.ViewHolder(binding!!.root)

    internal class ItemViewHolder(val binding: LayoutItemSingleItemBinding?) :
        RecyclerView.ViewHolder(binding!!.root)

    open fun onItemSelected(item: SingleItem?) {}

    init {
        mListItems?.addAll(listItems)
        mListOriginalItems = listItems
        mShowRadioSelection = showRadioSelection
    }
}