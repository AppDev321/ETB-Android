package com.tharsol.endtb.ui.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tharsol.endtb.model.NotificationDataModel
import com.tharsol.endtb.R;
import com.tharsol.endtb.util.ImageUtil
import kotlinx.coroutines.CoroutineScope

class NotificationListAdapter(private val mList: List<NotificationDataModel>, var itemListener: OnItemClickListener? = null) : RecyclerView.Adapter<NotificationListAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_layout, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        ImageUtil.loadImage(holder.imageView, ItemsViewModel.image)
        // sets the text to the textview from our itemHolder class
        holder.textPharmacy.text = "Pharmacy: " + ItemsViewModel.pharmacy
        holder.textDate.text = "Notification Date: " + ItemsViewModel.date
        holder.textId.text = "Notification ID/No. :" + ItemsViewModel.Id

        holder.itemView.setOnClickListener {
            itemListener?.onItemClick(ItemsViewModel)
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = ItemView.findViewById(R.id.imageview)
        val textPharmacy: TextView = ItemView.findViewById(R.id.textPharmacy)
        val textDate: TextView = ItemView.findViewById(R.id.textDate)
        val textId: TextView = ItemView.findViewById(R.id.textId)

    }

    interface OnItemClickListener {
        fun onItemClick(notificationData: NotificationDataModel)
    }
}