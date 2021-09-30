package com.tharsol.endtb.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tharsol.endtb.R
import com.tharsol.endtb.databinding.ItemSearchListBinding
import com.tharsol.endtb.deserialize.Patient
import com.tharsol.endtb.util.Constants

class PatientSearchAdapter(var itemListener: OnItemClickListener? = null) :
    BaseAdapter<PatientSearchAdapter.ViewHolder>() {

    val items = ArrayList<Patient>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSearchListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            itemListener?.onItemClick(item)
        }
    }

    override fun getItemCount(): Int {
        val size = items.size
        checkEmptyView(size)
        return size
    }

    class ViewHolder(val binding: ItemSearchListBinding?) :
        RecyclerView.ViewHolder(binding?.root!!) {
        fun bind(item: Patient) {
            binding!!.tvProductName.text =
                binding.root.context.getString(R.string.name_1_s, item.name)
            binding.tvTSUValue.text =
                binding.root.context.getString(
                    R.string.gender_1_s,
                    Constants.getGenderName(item.gender)
                )
        }
    }

    interface OnItemClickListener {
        fun onItemClick(patient: Patient)
    }

}