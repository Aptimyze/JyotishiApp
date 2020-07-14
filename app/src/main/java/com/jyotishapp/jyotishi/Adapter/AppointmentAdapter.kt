package com.jyotishapp.jyotishi.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jyotishapp.jyotishi.Models.Appointment
import com.jyotishapp.jyotishi.R
import kotlinx.android.synthetic.main.item_appointment_past.view.*
import kotlinx.android.synthetic.main.item_appointment_upcoming.view.*

private const val APPOINT_TYPE_UPCOMING: Int = 0
private const val APPOINT_TYPE_PAST: Int = 1

class AppointmentAdapter(var appointList : ArrayList<Appointment>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class UpcomingViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    fun bind(appointment: Appointment){
        itemView.date_upcoming.text = appointment.date
        itemView.time_upcoming.text = appointment.time
        itemView.day_name_upcoming.text = appointment.dayName
    }
    }

    class PastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
     fun bind(appointment: Appointment){
         itemView.date_past.text = appointment.date
         itemView.time_past.text = appointment.time
         itemView.day_name_past.text = appointment.dayName
     }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
      if(viewType == APPOINT_TYPE_UPCOMING){
          val view = LayoutInflater.from(parent.context).inflate(R.layout.item_appointment_upcoming, parent, false)
          return UpcomingViewHolder(view)
      }
        else{
          val view = LayoutInflater.from(parent.context).inflate(R.layout.item_appointment_past, parent, false)
          return PastViewHolder(view)
      }
    }

    override fun getItemCount(): Int {
      return appointList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       if (getItemViewType(position) == APPOINT_TYPE_UPCOMING){
           (holder as UpcomingViewHolder).bind(appointList[position])

       }
        else{
           (holder as PastViewHolder).bind(appointList[position])

       }
    }

    override fun getItemViewType(position: Int): Int {
        return if(appointList[position].status == "upcoming"){
            APPOINT_TYPE_UPCOMING
        }
        else{
            APPOINT_TYPE_PAST
        }

    }
}