package com.jyotishapp.jyotishi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.jyotishapp.jyotishi.Common.AppointmentData
import com.jyotishapp.jyotishi.Models.Appointment
import kotlinx.android.synthetic.main.fragment_appoint_host.*

class AppointHostFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_appoint_host, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        take_Appointment.setOnClickListener {
            val action = AppointHostFragmentDirections.actionAppointHostFragmentToAppointSelectFragment2()
            Navigation.findNavController(it).navigate(action)
        }

        my_appointments.setOnClickListener {
            val action = AppointHostFragmentDirections.actionAppointHostFragmentToMyAppointFragment()
            Navigation.findNavController(it).navigate(action)
        }

        back_arrow.setOnClickListener {
            activity?.finish()
        }
    }

}