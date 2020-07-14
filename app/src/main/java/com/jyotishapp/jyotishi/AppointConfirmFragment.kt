package com.jyotishapp.jyotishi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.jyotishapp.jyotishi.Models.Appointment
import kotlinx.android.synthetic.main.fragment_appoint_confirm.*

class AppointConfirmFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_appoint_confirm, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val uid = FirebaseAuth.getInstance().uid
        val latestAppointRef = FirebaseDatabase.getInstance().getReference("/Users/$uid/latestappointmentID")
        latestAppointRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val generic: GenericTypeIndicator<String?> = object : GenericTypeIndicator<String?>() {}
                val appointID : String? = p0.getValue(generic)

                val ref = FirebaseDatabase.getInstance().getReference("/appointments/$appointID")
                ref.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val appoint = p0.getValue(Appointment::class.java)
                        if (appoint != null) {
                            appointDate_frag.text = appoint.date
                            appointTime_frag.text = appoint.time
                        }
                    }
                })

            }
        })


        back.setOnClickListener {
            activity?.finish()
        }
    }



}