package com.jyotishapp.jyotishi.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jyotishapp.jyotishi.Common.Common
import com.jyotishapp.jyotishi.R
import com.jyotishapp.jyotishi.Interface.ItemClickListener
import com.jyotishapp.jyotishi.Models.TimeSlotItem

class customViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    var time_text_view: TextView = itemView.findViewById<View>(R.id.time_slot) as TextView

    var item_background : LinearLayout = itemView.findViewById(R.id.time_slot_holder) as LinearLayout

    var itemClickListener: ItemClickListener? = null

    fun SetItemClickListener(itemClickListener: ItemClickListener?) {
        this.itemClickListener = itemClickListener
    }

    override fun onClick(v: View) {
        itemClickListener!!.onClick(v, adapterPosition)
    }

    init {
        itemView.setOnClickListener(this)
    }
}

class CustomAdapter(
        var items: ArrayList<TimeSlotItem>,
        var context: Context
) : RecyclerView.Adapter<customViewHolder>() {

    var row_index = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): customViewHolder {
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.time_slot, parent, false)
        return customViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: customViewHolder, position: Int) {
        holder.time_text_view.text = items[position].Time

        holder.SetItemClickListener(object : ItemClickListener {
            override fun onClick(view: View, position: Int) {
                row_index = position
                Common.currentItem = items[position]
                Common.currentAdminUid = items[position].adminId
                notifyDataSetChanged()
            }
        })

        
        //set highlight color
        if (row_index == position) {
            holder.item_background.setBackgroundResource(R.drawable.button_selected2)
            holder.time_text_view.setTextColor(ContextCompat.getColor(context, R.color.black))

        } else {
            holder.item_background.setBackgroundResource(R.drawable.button_unselected)
            holder.time_text_view.setTextColor(ContextCompat.getColor(context, R.color.colorGrey))
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}