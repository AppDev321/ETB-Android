package com.tharsol.endtb.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tharsol.endtb.databinding.ItemStockListBinding
import com.tharsol.endtb.deserialize.StockProductItem

class StockAdapter() : BaseAdapter<StockAdapter.ViewHolder>() {

    val items = ArrayList<StockProductItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemStockListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        val size = items.size
        checkEmptyView(size)
        return size
    }

    class ViewHolder(val binding: ItemStockListBinding?) :
        RecyclerView.ViewHolder(binding?.root!!) {
        fun bind(item: StockProductItem) {
            binding?.tvProductName?.text = item.name
            binding?.tvTSUValue?.text = item.todaySaleUnit.toString()
            binding?.tvMSUValue?.text = item.monthSaleUnit.toString()
            binding?.tvStockQuantityValue?.text = item.stockQuantity.toString()
        }

    }

}