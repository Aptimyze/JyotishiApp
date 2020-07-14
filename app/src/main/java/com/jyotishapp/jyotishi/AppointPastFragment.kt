package com.jyotishapp.jyotishi

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.jyotishapp.jyotishi.Adapter.AppointmentAdapter
import com.jyotishapp.jyotishi.Common.AppointmentData
import com.jyotishapp.jyotishi.Models.Appointment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.fragment_appoint_past.*
import kotlinx.android.synthetic.main.fragment_appoint_upcoming.*
import kotlinx.android.synthetic.main.fragment_my_appoint.*
import kotlinx.android.synthetic.main.item_appointment_past.view.*
import kotlinx.android.synthetic.main.item_appointment_upcoming.view.*

class AppointPastFragment : Fragment() {

    lateinit var recyclerView: RecyclerView

    lateinit var loading: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_appoint_past, container, false)
        recyclerView = view.findViewById(R.id.appointment_past_recyclerview)
        loading = view.findViewById(R.id.loading_past)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/Users/$uid/appointmentID")

        val adapter = GroupAdapter<GroupieViewHolder>()
        recyclerView.adapter = adapter

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {

                    val generic: GenericTypeIndicator<ArrayList<String?>?> = object : GenericTypeIndicator<ArrayList<String?>?>() {}
                    val b: ArrayList<String?> = p0.getValue(generic)!!

                    AppointmentData.appointmentID = b

                    for (appointID in AppointmentData.appointmentID) {

                        val appointRef = FirebaseDatabase.getInstance().getReference("/appointments/$appointID")

                        appointRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {

                            }

                            override fun onDataChange(p0: DataSnapshot) {

                                val appoint = p0.getValue(Appointment::class.java)
                                if (appoint != null) {
                                    if (appoint.status != "upcoming") {
                                        adapter.add(PastAppointment(appoint.date, appoint.time, appoint.dayName))
                                        tap_to_view_past.visibility = View.GONE
                                    }
                                }
                            }
                        })
                    }

                }

                if(adapter.itemCount == 0){
                    tap_to_view_past.visibility = View.VISIBLE
                }

            }
        })

    }

    class PastAppointment(val date: String, val time: String, val dayName: String) : Item<GroupieViewHolder>() {
        override fun getLayout(): Int {
            return R.layout.item_appointment_past
        }

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.date_past.text = date
            viewHolder.itemView.time_past.text = time
            viewHolder.itemView.day_name_past.text = dayName
        }
    }

}