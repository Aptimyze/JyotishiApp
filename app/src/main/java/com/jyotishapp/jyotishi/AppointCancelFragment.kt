package com.jyotishapp.jyotishi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jyotishapp.jyotishi.Models.DateTimeWrapper
import kotlinx.android.synthetic.main.fragment_appoint_cancel.*

class AppointCancelFragment : Fragment() {

    private var appointment : DateTimeWrapper? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_appoint_cancel, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
         appointment = AppointCancelFragmentArgs.fromBundle(it).Appoint
         date_cancel.text = appointment!!.date
         time_cancel.text = appointment!!.time
        }

        cont.setOnClickListener {
            activity?.onBackPressed()
        }
    }

}