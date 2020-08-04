package com.jyotishapp.jyotishi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.jyotishapp.jyotishi.Adapter.ViewPagerAdapter
import com.jyotishapp.jyotishi.Common.AppointmentData
import com.jyotishapp.jyotishi.Models.Appointment
import kotlinx.android.synthetic.main.fragment_my_appoint.*

class MyAppointFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_appoint, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewpager2.adapter =
                ViewPagerAdapter(requireActivity())

        TabLayoutMediator(tablayout, viewpager2,
                TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                    when (position) {
                        0 -> tab.text = "Upcoming"
                        1 -> tab.text = "Past"
                    }
                }).attach()


      back_my_appoint.setOnClickListener {
          activity?.onBackPressed()
      }

    }

}




