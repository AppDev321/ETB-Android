package com.tharsol.endtb.ui.widget

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sameelenterprises.mrep.adapters.searchablelisting.SingleSelectionAdapter
import com.tharsol.endtb.R
import com.tharsol.endtb.model.SingleItem
import com.tharsol.endtb.ui.BaseActivity
import com.tharsol.endtb.ui.adapters.MultipleSelectionAdapter
import com.tharsol.endtb.util.DateUtilz
import com.tharsol.endtb.util.Utilities
import org.joda.time.DateTime
import java.util.*

class Dialogs(private val mContext: Context) {

    interface AddItemSelectedListener {
        fun onItemSelected(dialogInterface: DialogInterface?, singleItem: SingleItem?)
    }

    interface AddItemsSelectedListener {
        fun onItemSelected(dialogInterface: DialogInterface?, selectedItems: List<SingleItem?>?)
    }

    private var mDialog: Dialog? = null
    private var mAddItemSelectedListener: AddItemSelectedListener? = null
    private var mAddItemsSelectedListener: AddItemsSelectedListener? = null
    private var mSearchActive = false
    fun addOnItemSelectedListener(listener: AddItemSelectedListener?) {
        mAddItemSelectedListener = listener
    }

    fun addOnItemsSelectedListener(listener: AddItemsSelectedListener?) {
        mAddItemsSelectedListener = listener
    }

    fun showMultipleChoiceItemsDialog(items: List<SingleItem>, title: String?) {
        showMultipleChoiceItemsDialog(items, title, null, null)
    }

    fun showMultipleChoiceItemsDialog(
        items: List<SingleItem>,
        title: String?,
        subTitle: String?
    ) {
        showMultipleChoiceItemsDialog(items, title, subTitle, null, null)
    }

    fun showMultipleChoiceItemsDialog(
        items: List<SingleItem>,
        title: String?,
        negativeButtonText: String?,
        onNegativeClickListener: View.OnClickListener?
    ) {
        showMultipleChoiceItemsDialog(
            items,
            title,
            null,
            negativeButtonText,
            onNegativeClickListener
        )
    }

    fun showMultipleChoiceItemsDialog(
        items: List<SingleItem>,
        title: String?,
        subTitle: String?,
        negativeButtonText: String?,
        onNegativeClickListener: View.OnClickListener?
    ) {
        showMultipleChoiceItemsDialog(
            items,
            title,
            subTitle,
            negativeButtonText,
            onNegativeClickListener,
            -1
        )
    }

    fun showMultipleChoiceItemsDialog(
        items: List<SingleItem>,
        title: String?,
        subTitle: String?,
        negativeButtonText: String?,
        onNegativeClickListener: View.OnClickListener?,
        maxSelection: Int
    ) {
        mDialog = Dialog(mContext)
        mDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog?.setContentView(R.layout.layout_dialog_single_item_searchable)
        mDialog?.setCanceledOnTouchOutside(false)
        mDialog?.setCancelable(false)
        val textViewTitle = mDialog!!.findViewById<TextView>(R.id.text_view_heading)
        if (!TextUtils.isEmpty(title)) textViewTitle.text = title
        val textViewSubTitle = mDialog!!.findViewById<TextView>(R.id.text_view_sub_heading)
        if (!TextUtils.isEmpty(subTitle)) {
            textViewSubTitle.visibility = View.VISIBLE
            textViewSubTitle.text = subTitle
        }
        val linearLayoutManager = LinearLayoutManager(mContext)
        val recyclerView: RecyclerView = mDialog!!.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = linearLayoutManager
        val adapter = MultipleSelectionAdapter(items)
        adapter.maxSelection = (maxSelection)
        recyclerView.adapter = adapter
        val editText = mDialog!!.findViewById<EditText>(R.id.edit_text_search)
        editText.setOnClickListener { view: View? ->
            editText.isFocusableInTouchMode = true
            editText.requestFocus()
            BaseActivity.showKeyboard(editText)
        }
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                mSearchActive = true
                adapter.getFilter().filter(charSequence)
                adapter.setSearchTag(charSequence.toString())
                //                adapter.setItems(filteredItems);
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        editText.setOnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                if (mAddItemsSelectedListener != null) {
                    val selectedItems: List<SingleItem?> = adapter.selectedItems
                    if (selectedItems.isEmpty()) {
                        return@setOnEditorActionListener false
                    }
                    mAddItemsSelectedListener!!.onItemSelected(mDialog, selectedItems)
                }
                dismiss()
                return@setOnEditorActionListener true
            }
            false
        }
        val buttonCancel = mDialog!!.findViewById<Button>(R.id.button_cancel)
        buttonCancel.setOnClickListener(onNegativeClickListener
            ?: View.OnClickListener { view: View? -> dismiss() })
        buttonCancel.text =
            if (TextUtils.isEmpty(negativeButtonText)) mContext.getString(R.string.cancel) else negativeButtonText
        Utilities.changeButtonBackground(buttonCancel, Color.WHITE)
        val buttonOkay = mDialog!!.findViewById<Button>(R.id.button_ok)
        Utilities.changeButtonBackground(buttonOkay, Color.WHITE)
        buttonOkay.setOnClickListener { view: View? ->
            if (mAddItemsSelectedListener != null) {
                val selectedItems: List<SingleItem?> = adapter.selectedItems
                if (selectedItems.size == 0) {
                    return@setOnClickListener
                }
                mAddItemsSelectedListener!!.onItemSelected(mDialog, selectedItems)
            }
            dismiss()
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rv, dx, dy)
                if (mSearchActive) return
                val isRecyclerScrollable = rv.computeVerticalScrollRange() > rv.height
                editText.visibility = if (isRecyclerScrollable) View.VISIBLE else View.GONE
            }
        })
        mDialog!!.show()
    }

    fun showSingleChoiceItemsDialog(
        items: List<SingleItem>,
        title: String?,
        showRadioButton: Boolean
    ) {
        showSingleChoiceItemsDialog(items, title, showRadioButton, false)
    }

    fun showSingleChoiceItemsDialog(
        items: List<SingleItem>,
        title: String?,
        showRadioButton: Boolean,
        showNonOption: Boolean
    ) {
        mDialog = Dialog(mContext)
        mDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog?.setContentView(R.layout.layout_dialog_single_item_searchable)
        mDialog!!.setCanceledOnTouchOutside(false)
        mDialog!!.setCancelable(false)
        val textViewTitle = mDialog!!.findViewById<TextView>(R.id.text_view_heading)
        textViewTitle.text = title
        val linearLayoutManager = LinearLayoutManager(mContext)
        val recyclerView: RecyclerView = mDialog!!.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = linearLayoutManager
        val adapter: SingleSelectionAdapter =
            object : SingleSelectionAdapter(items, showRadioButton) {
                override fun onItemSelected(item: SingleItem?) {
                    if (!showRadioButton && mAddItemSelectedListener != null) {
                        mAddItemSelectedListener!!.onItemSelected(mDialog, item)
                        dismiss()
                    }
                }
            }
        recyclerView.adapter = adapter
        val editText = mDialog!!.findViewById<EditText>(R.id.edit_text_search)
        editText.setOnClickListener { view: View? ->
            editText.isFocusableInTouchMode = true
            editText.requestFocus()
            BaseActivity.showKeyboard(editText)
        }
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                try {
                    mSearchActive = true
                    adapter.getFilter().filter(charSequence)
                    adapter.searchTag = (charSequence.toString())
                } catch (ex: Exception) {
                    Utilities.LogException(ex)
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        editText.setOnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                if (mAddItemSelectedListener != null) {
                    if (adapter.selectedItem == null) {
                        return@setOnEditorActionListener false
                    }
                    mAddItemSelectedListener!!.onItemSelected(mDialog, adapter.selectedItem)
                }
                dismiss()
                return@setOnEditorActionListener true
            }
            false
        }
        val buttonCancel = mDialog!!.findViewById<RoundedButton>(R.id.button_cancel)
        buttonCancel.setOnClickListener { view: View? -> dismiss() }
        Utilities.changeButtonBackground(buttonCancel, Color.WHITE)
        val buttonOkay = mDialog!!.findViewById<RoundedButton>(R.id.button_ok)
        Utilities.changeButtonBackground(buttonOkay, Color.WHITE)
        buttonOkay.setOnClickListener { view: View? ->
            if (mAddItemSelectedListener != null) {
                if (adapter.selectedItem == null) {
                    return@setOnClickListener
                }
                mAddItemSelectedListener!!.onItemSelected(mDialog, adapter.selectedItem)
            }
            dismiss()
        }
        buttonOkay.visibility = if (showRadioButton) View.VISIBLE else View.GONE
        val buttonNone = mDialog!!.findViewById<RoundedButton>(R.id.button_none)
        if (showNonOption) {
            buttonNone.visibility = View.VISIBLE
            Utilities.changeButtonBackground(buttonNone, Color.WHITE)
            buttonNone.setOnClickListener { view: View? ->
                if (mAddItemSelectedListener != null) {
                    mAddItemSelectedListener!!.onItemSelected(mDialog, null)
                }
                dismiss()
            }
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rv, dx, dy)
                if (mSearchActive) return
                val isRecyclerScrollable = rv.computeVerticalScrollRange() > rv.height
                editText.visibility = if (isRecyclerScrollable) View.VISIBLE else View.GONE
            }
        })
        mDialog!!.show()
    }


    fun dismiss() {
        if (mDialog != null) {
            mDialog!!.dismiss()
        }
    }

    companion object {
        fun showDatePickerWithLastWeek(editText: EditText) {
            val dateTime: DateTime = DateUtilz.parseStandardViewDate(editText.text.toString())
            val datePickerDialog = DatePickerDialog(
                editText.context,
                { view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                    val calendar = Calendar.getInstance()
                    calendar[year, month] = dayOfMonth
                    editText.setText(DateUtilz.formatStandardViewDate(DateTime(calendar.timeInMillis)))
                },
                dateTime.year,
                dateTime.monthOfYear - 1,
                dateTime.dayOfMonth
            )
            val maxDate = DateTime.now()
            val minDate = maxDate.minusDays(7)
            datePickerDialog.datePicker.minDate = minDate.millis
            datePickerDialog.datePicker.maxDate = maxDate.millis
            datePickerDialog.show()
        }

        fun showDatePickerFromNow(editText: EditText) {
            val dateTime: DateTime = DateUtilz.parseStandardViewDate(editText.text.toString())
            val datePickerDialog = DatePickerDialog(
                editText.context,
                { view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                    val calendar = Calendar.getInstance()
                    calendar[year, month] = dayOfMonth
                    editText.setText(DateUtilz.formatStandardViewDate(DateTime(calendar.timeInMillis)))
                },
                dateTime.year,
                dateTime.monthOfYear - 1,
                dateTime.dayOfMonth
            )
            val minDate = DateTime.now()
            datePickerDialog.datePicker.minDate = minDate.millis
            datePickerDialog.show()
        }
    }
}