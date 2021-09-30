package com.tharsol.endtb.ui.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import com.tharsol.endtb.R
import com.tharsol.endtb.databinding.LayoutTbProductItemBinding
import com.tharsol.endtb.deserialize.Product
import com.tharsol.endtb.model.SingleItem
import com.tharsol.endtb.serialize.TransProduct
import com.tharsol.endtb.ui.widget.Dialogs

abstract class TbProductItem(val context: Context,
                             val patient: Int? = null,
                             val products: List<Product>) {

    var binding: LayoutTbProductItemBinding? = null
    var selectedProduct: SingleItem? = null


    init {
        binding = LayoutTbProductItemBinding.inflate(LayoutInflater.from(context))
        onClickListener()
    }

    @SuppressLint("ClickableViewAccessibility") private fun onClickListener() {
        binding!!.delete.setOnClickListener{onRemove(this)}
        binding!!.editTextProductName.editor.setOnClickListener { showProductsDialog() }
    }

    fun getView(): View {
        return binding!!.root
    }

    fun isEmpty(): Boolean {
        if (selectedProduct == null || TextUtils.isEmpty(binding!!.editTextProductName.text)) {
            binding!!.editTextProductName.setError(context.getString(R.string.error_empty_field))
            return true
        }
        if (TextUtils.isEmpty(binding?.editTextProductQty!!.text)) {
            binding?.editTextProductQty!!.setError(context.getString(R.string.error_empty_field))
            return true
        }

        return false
    }

    fun getItem(): TransProduct {
        return TransProduct(
            patient, binding!!.editTextProductQty.text.toString().toInt(), selectedProduct?.value)
    }

    private fun showProductsDialog() {

        val dialogs = Dialogs(context)
        dialogs.addOnItemSelectedListener(object : Dialogs.AddItemSelectedListener {
            override fun onItemSelected(dialogInterface: DialogInterface?,
                                        singleItem: SingleItem?) {
                selectedProduct = singleItem
                binding?.editTextProductName?.setText(singleItem?.title)
                binding!!.editTextProductName.setError(null)
            }

        })
        dialogs.showSingleChoiceItemsDialog(products.map {
            val item = SingleItem()
            item.value = it.id
            item.referenceId = it.productCode
            item.title = it.name
            item.description = it.pharmaCompanyName
            return@map item
        }, context.getString(R.string.choose_an_item), false)
    }

    abstract fun onRemove(item: TbProductItem)
}