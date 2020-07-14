package com.jyotishapp.jyotishi

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.jyotishapp.jyotishi.Adapter.AppointmentAdapter
import com.jyotishapp.jyotishi.Common.AppointmentData
import com.jyotishapp.jyotishi.Interface.ItemClickListener
import com.jyotishapp.jyotishi.Models.Appointment
import com.jyotishapp.jyotishi.Models.DateTimeWrapper
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import com.xwray.groupie.Section
import kotlinx.android.synthetic.main.fragment_appoint_upcoming.*
import kotlinx.android.synthetic.main.item_appointment_upcoming.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await


class AppointUpcomingFragment : Fragment() {

    lateinit var recyclerView : RecyclerView

    lateinit var loading : LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_appoint_upcoming, container, false)
        recyclerView = view.findViewById(R.id.appointment_upcoming_recyclerview)
        loading = view.findViewById(R.id.loading_upcoming)

        return view
    }

    var adapter = GroupAdapter<GroupieViewHolder>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        val uid = FirebaseAuth.getInstance().uid
        recyclerView.adapter = adapter

        tap_to_view_upcoming.visibility = View.VISIBLE

        val ref = FirebaseDatabase.getInstance().getReference("/Users/$uid/appointmentID")

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
                                        if (appoint.status == "upcoming") {
                                     val classObject = UpcomingAppointment(appoint.date, appoint.time, appoint.dayName, requireContext(),
                                                    appoint.key, appoint.dayIdentifier, appoint.position)
                                         adapter.add(classObject)

                                            tap_to_view_upcoming.visibility = View.GONE
                                        }
                                    }
                                }
                            })
                        }

                    }

                    if (adapter.itemCount == 0){
                        tap_to_view_upcoming.visibility = View.VISIBLE
                    }

                }
            })

    }

    class UpcomingAppointment(val date : String, val time : String, val dayName : String, val context : Context,
                              val key : String, val dayIdentifier : String, val pos : Int) : Item<GroupieViewHolder>()
             {

        override fun getLayout(): Int {
            return R.layout.item_appointment_upcoming
        }

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
           viewHolder.itemView.date_upcoming.text = date
           viewHolder.itemView.time_upcoming.text = time
           viewHolder.itemView.day_name_upcoming.text = dayName
           viewHolder.itemView.cancel.setOnClickListener {

               val builder = AlertDialog.Builder(context)
               builder.setTitle("Confirm action")
               builder.setMessage("Are you sure you want to cancel this appointment ? This cannot be undone")
               builder.setPositiveButton("Yes, cancel"){dialog: DialogInterface?, which: Int ->
                   cancelAppointment(key, dayIdentifier, pos,it, date, time)
               }
               builder.setNegativeButton("No"){dialog: DialogInterface?, which: Int ->  }
               builder.show()
           }
        }

        private fun cancelAppointment(key : String, dayIdentifier: String, position : Int, view : View, date : String , time : String) {

            val cancelRef = FirebaseDatabase.getInstance().getReference("/Admin/appointIDs/$dayIdentifier")
            cancelRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {

                    CoroutineScope(Dispatchers.IO).launch {

                        val generic: GenericTypeIndicator<ArrayList<String?>?> = object : GenericTypeIndicator<ArrayList<String?>?>() {}
                        val b: ArrayList<String?> = p0.getValue(generic)!!

                        if (b.contains(key)) {

                            b[position] = "NA"

                            cancelRef.setValue(b).await()

                            val uid = FirebaseAuth.getInstance().uid
                            val userRef = FirebaseDatabase.getInstance().getReference("/Users/$uid/appointmentID")
                            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {

                                }

                                override fun onDataChange(p0: DataSnapshot) {

                                    CoroutineScope(Dispatchers.IO).launch{
                                    val generic: GenericTypeIndicator<ArrayList<String?>?> = object : GenericTypeIndicator<ArrayList<String?>?>() {}
                                    val b: ArrayList<String?> = p0.getValue(generic)!!

                                    if (b.contains(key)) {
                                        b.remove(key)
                                        userRef.setValue(b).await()

                                        val slotRef = FirebaseDatabase.getInstance().getReference("/Admin/slots/$dayIdentifier")
                                        slotRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                            override fun onCancelled(p0: DatabaseError) {

                                            }

                                            override fun onDataChange(p0: DataSnapshot) {

                                                CoroutineScope(Dispatchers.IO).launch {

                                                    val generic: GenericTypeIndicator<ArrayList<String?>?> = object : GenericTypeIndicator<ArrayList<String?>?>() {}
                                                    val b: ArrayList<String?> = p0.getValue(generic)!!

                                                    b[position] = "available"
                                                    slotRef.setValue(b).await()

                                                    val appointRef = FirebaseDatabase.getInstance().getReference("/appointments")
                                                    appointRef.child("/$key").removeValue().await()

                                                    val action = MyAppointFragmentDirections.actionMyAppointFragmentToAppointCancelFragment(DateTimeWrapper(date, time))
                                                    Navigation.findNavController(view).navigate(action)

                                                }

                                            }
                                        })

                                    }

                                }

                                }
                            })
                        }

                    }
                }

            })

        }


    }

}