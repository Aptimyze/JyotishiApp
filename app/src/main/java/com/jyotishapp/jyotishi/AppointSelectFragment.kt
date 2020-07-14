package com.jyotishapp.jyotishi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.jyotishapp.jyotishi.Adapter.CustomAdapter
import com.jyotishapp.jyotishi.Common.AppointmentData
import com.jyotishapp.jyotishi.Common.Common
import com.jyotishapp.jyotishi.Common.Constant
import com.jyotishapp.jyotishi.Common.TimeSlotData
import com.jyotishapp.jyotishi.Models.Appointment
import com.jyotishapp.jyotishi.Models.TimeSlotItem
import kotlinx.android.synthetic.main.fragment_appoint_select.*
import kotlinx.android.synthetic.main.fragment_appoint_select.back
import kotlinx.android.synthetic.main.fragment_appoint_select.confirm
import kotlinx.android.synthetic.main.fragment_appoint_select.day_one_holder
import kotlinx.android.synthetic.main.fragment_appoint_select.day_three_holder
import kotlinx.android.synthetic.main.fragment_appoint_select.day_two_holder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AppointSelectFragment : Fragment() , View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_appoint_select, container, false)
    }

    private var index: Int = -1
    private val DAY_ALPHA = "dayAlpha"
    private val DAY_BETA = "dayBeta"
    private val DAY_GAMMA = "dayGamma"
    var selectedDay: String? = null
    var selectedDay_name: String? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

       initClicks()
       initDays()
        refreshLists()

        getSlots(DAY_ALPHA)
        getSlots(DAY_BETA)
        getSlots(DAY_GAMMA)

        getAppointIDs(DAY_ALPHA)
        getAppointIDs(DAY_BETA)
        getAppointIDs(DAY_GAMMA)

        view_loading2_frag.visibility = View.VISIBLE

        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/Users/$uid")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                day_one_date_frag.text = p0.child("/date_one").value.toString()
                day_two_date_frag.text = p0.child("/date_two").value.toString()
                day_three_date_frag.text = p0.child("/date_three").value.toString()

                day_one_day_frag.text = p0.child("/day_one_name").value.toString()
                day_two_day_frag.text = p0.child("/day_two_name").value.toString()
                day_three_day_frag.text = p0.child("/day_three_name").value.toString()

                view_loading2_frag.visibility = View.INVISIBLE
            }
        })

    }

    private fun initClicks() {
        back.setOnClickListener(this)
        day_one_holder.setOnClickListener(this)
        day_two_holder.setOnClickListener(this)
        day_three_holder.setOnClickListener(this)
        confirm.setOnClickListener(this)
    }

    private fun initDays() {
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val sdf2 = SimpleDateFormat("dd")
        val date = Date(System.currentTimeMillis())
        val day_original = sdf.format(date)

        val calendar_o = Calendar.getInstance()
        calendar_o.time = date
        calendar_o.add(Calendar.DAY_OF_YEAR, 1)
        val day_namer = calendar_o.get(Calendar.DAY_OF_WEEK)
        val Date = calendar_o.time
        val day_one: String = sdf.format(Date)
        val date_one: String = sdf2.format(Date)
        val day_one_name: String =
                when (day_namer) {
                    1 -> "Sun"
                    2 -> "Mon"
                    3 -> "Tue"
                    4 -> "Wed"
                    5 -> "Thu"
                    6 -> "Fri"
                    7 -> "Sat"
                    else -> ""
                }

        val calendar = Calendar.getInstance()
        calendar.time = Date
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val day_namer2 = calendar.get(Calendar.DAY_OF_WEEK)
        val newDate = calendar.time
        val day_two: String = sdf.format(newDate)
        val date_two: String = sdf2.format(newDate)
        val day_two_name: String =
                when (day_namer2) {
                    1 -> "Sun"
                    2 -> "Mon"
                    3 -> "Tue"
                    4 -> "Wed"
                    5 -> "Thu"
                    6 -> "Fri"
                    7 -> "Sat"
                    else -> ""
                }

        val calendar_two = Calendar.getInstance()
        calendar_two.time = newDate
        calendar_two.add(Calendar.DAY_OF_YEAR, 1)
        val day_namer3 = calendar_two.get(Calendar.DAY_OF_WEEK)
        val newerDate = calendar_two.time
        val day_three: String = sdf.format(newerDate)
        val date_three: String = sdf2.format(newerDate)
        val day_three_name: String =
                when (day_namer3) {
                    1 -> "Sun"
                    2 -> "Mon"
                    3 -> "Tue"
                    4 -> "Wed"
                    5 -> "Thu"
                    6 -> "Fri"
                    7 -> "Sat"
                    else -> ""
                }

        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/Users/$uid")

        val map = HashMap<String, Any>()
        map["day_one"] = day_one
        map["day_two"] = day_two
        map["day_three"] = day_three
        map["date_one"] = date_one
        map["date_two"] = date_two
        map["date_three"] = date_three
        map["day_one_name"] = day_one_name
        map["day_two_name"] = day_two_name
        map["day_three_name"] = day_three_name

        ref.updateChildren(map)
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "failed , error : ${it.message}", Toast.LENGTH_SHORT).show()
                }
    }

    private fun dayOneChecked() {

        selectedDay = DAY_ALPHA

        selectedDay_name = "${day_one_day_frag.text}"

        val adapter =
                CustomAdapter(TimeSlotData.testListDayAlpha, requireContext())
        time_recyclerview_frag.adapter = adapter

        day_one_holder.setBackgroundResource(R.drawable.button_selected)
        day_one_day_frag.setTextColor(ContextCompat.getColor(requireContext(), R.color.ColorYellow))
        day_one_date_frag.setTextColor(ContextCompat.getColor(requireContext(), R.color.ColorYellow))

        day_two_holder.setBackgroundResource(R.drawable.button_unselected)
        day_two_day_frag.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorGrey))
        day_two_date_frag.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorGrey))

        day_three_holder.setBackgroundResource(R.drawable.button_unselected)
        day_three_day_frag.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorGrey))
        day_three_date_frag.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorGrey))
    }

    private fun dayTwoChecked() {

        selectedDay = DAY_BETA

        selectedDay_name = "${day_two_day_frag.text}"

        val adapter =
                CustomAdapter(TimeSlotData.testListDayBeta, requireContext())
        time_recyclerview_frag.adapter = adapter

        day_one_holder.setBackgroundResource(R.drawable.button_unselected)
        day_one_day_frag.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorGrey))
        day_one_date_frag.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorGrey))

        day_two_holder.setBackgroundResource(R.drawable.button_selected)
        day_two_day_frag.setTextColor(ContextCompat.getColor(requireContext(), R.color.ColorYellow))
        day_two_date_frag.setTextColor(ContextCompat.getColor(requireContext(), R.color.ColorYellow))

        day_three_holder.setBackgroundResource(R.drawable.button_unselected)
        day_three_day_frag.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorGrey))
        day_three_date_frag.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorGrey))
    }

    private fun dayThreeChecked() {

        selectedDay = DAY_GAMMA

        selectedDay_name = "${day_three_day_frag.text}"

        val adapter =
                CustomAdapter(TimeSlotData.testListDayGamma, requireContext())
        time_recyclerview_frag.adapter = adapter

        day_one_holder.setBackgroundResource(R.drawable.button_unselected)
        day_one_day_frag.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorGrey))
        day_one_date_frag.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorGrey))

        day_two_holder.setBackgroundResource(R.drawable.button_unselected)
        day_two_day_frag.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorGrey))
        day_two_date_frag.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorGrey))

        day_three_holder.setBackgroundResource(R.drawable.button_selected)
        day_three_day_frag.setTextColor(ContextCompat.getColor(requireContext(), R.color.ColorYellow))
        day_three_date_frag.setTextColor(ContextCompat.getColor(requireContext(), R.color.ColorYellow))
    }

    private fun refreshLists() {

        TimeSlotData.listDayAlpha.clear()
        TimeSlotData.listDayBeta.clear()
        TimeSlotData.listDayGamma.clear()

        TimeSlotData.testListDayAlpha.clear()
        TimeSlotData.testListDayBeta.clear()
        TimeSlotData.testListDayGamma.clear()

        AppointmentData.appointmentIDDayAlpha.clear()
        AppointmentData.appointmentIDDayBeta.clear()
        AppointmentData.appointmentIDDayGamma.clear()
    }

    private fun getSlots(dayName: String) {

        val ref = FirebaseDatabase.getInstance().getReference("/Admin/slots/$dayName")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                val generic: GenericTypeIndicator<ArrayList<String?>?> = object : GenericTypeIndicator<ArrayList<String?>?>() {}
                val b: ArrayList<String?> = p0.getValue(generic)!!

                if (dayName == DAY_ALPHA) {
                    TimeSlotData.listDayAlpha = b
                    for (i in 0..11) {
                        if (b[i] == "available")
                            TimeSlotData.testListDayAlpha.add(TimeSlotItem(Constant.listOfTimeSlots[i], false, "only one admin", b[i]!!))
                    }
                }

                if (dayName == DAY_BETA) {
                    TimeSlotData.listDayBeta = b
                    for (i in 0..11) {
                        if (b[i] == "available")
                            TimeSlotData.testListDayBeta.add(TimeSlotItem(Constant.listOfTimeSlots[i], false, "only one admin", b[i]!!))
                    }
                }

                if (dayName == DAY_GAMMA) {
                    TimeSlotData.listDayGamma = b
                    for (i in 0..11) {
                        if (b[i] == "available")
                            TimeSlotData.testListDayGamma.add(TimeSlotItem(Constant.listOfTimeSlots[i], false, "only one admin", b[i]!!))
                    }
                }
            }
        })
    }

    private fun getAppointIDs(dayName: String) {

        val ref = FirebaseDatabase.getInstance().getReference("/Admin/appointIDs/$dayName")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                val generic: GenericTypeIndicator<ArrayList<String?>?> = object : GenericTypeIndicator<ArrayList<String?>?>() {}
                val b: ArrayList<String?> = p0.getValue(generic)!!

                if (dayName == DAY_ALPHA) {
                    AppointmentData.appointmentIDDayAlpha = b
                }

                if (dayName == DAY_BETA) {
                    AppointmentData.appointmentIDDayBeta = b
                }

                if (dayName == DAY_GAMMA) {
                    AppointmentData.appointmentIDDayGamma = b
                }
            }
        })
    }

    private fun bookAppointment(position: Int , view : View) {

        val ref = FirebaseDatabase.getInstance().getReference("/Admin/slots/$selectedDay")

        if (position != -1 && selectedDay != null) {

            val email = FirebaseAuth.getInstance().currentUser?.email
            val uid = FirebaseAuth.getInstance().uid

            if (selectedDay == DAY_ALPHA) {
                TimeSlotData.listDayAlpha[position] = "not available"
                     ref.setValue(TimeSlotData.listDayAlpha)
                        .addOnCompleteListener {

                            val userRef = FirebaseDatabase.getInstance().getReference("/Users/$uid")
                            userRef.addListenerForSingleValueEvent(object : ValueEventListener{
                                override fun onCancelled(p0: DatabaseError) {

                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    val day_one : String? = p0.child("/day_one").value.toString()

                                    val appointIDRef = FirebaseDatabase.getInstance().getReference("/Admin/appointIDs/$DAY_ALPHA")
                                    val appointRef = FirebaseDatabase.getInstance().getReference("/appointments").push()
                                    AppointmentData.appointmentIDDayAlpha[position] = appointRef.key!!

                                    appointIDRef.setValue(AppointmentData.appointmentIDDayAlpha)

                                    val appoint = Appointment(email!! , uid!! , day_one!! , Common.currentItem?.Time!!,
                                            appointRef.key!!, selectedDay_name!!,"upcoming",DAY_ALPHA, position)

                                    appointRef.setValue(appoint).addOnCompleteListener {

                                        val reference = FirebaseDatabase.getInstance().getReference("/Users/$uid/appointmentID")
                                        reference.addListenerForSingleValueEvent(object : ValueEventListener{
                                            override fun onCancelled(p0: DatabaseError) {

                                            }

                                            override fun onDataChange(p0: DataSnapshot) {

                                                if (p0.exists()) {
                                                    val generic: GenericTypeIndicator<ArrayList<String?>?> = object : GenericTypeIndicator<ArrayList<String?>?>() {}
                                                    val b: ArrayList<String?> = p0.getValue(generic)!!

                                                    b.add(appointRef.key)
                                                    reference.setValue(b).addOnCompleteListener {

                                                        val latestAppointRef = FirebaseDatabase.getInstance().getReference("/Users/$uid/latestappointmentID")
                                                        latestAppointRef.setValue(appointRef.key)

                                                        val action = AppointSelectFragmentDirections.actionAppointSelectFragmentToAppointConfirmFragment()
                                                        Navigation.findNavController(view).navigate(action)
                                                    }
                                                } else {
                                                    val b: ArrayList<String?> = ArrayList<String?>()
                                                    b.add(appointRef.key)

                                                    reference.setValue(b).addOnCompleteListener {


                                                        val latestAppointRef = FirebaseDatabase.getInstance().getReference("/Users/$uid/latestappointmentID")
                                                        latestAppointRef.setValue(appointRef.key)

                                                        val action = AppointSelectFragmentDirections.actionAppointSelectFragmentToAppointConfirmFragment()
                                                        Navigation.findNavController(view).navigate(action)
                                                    }
                                                }

                                            }
                                        })

                                    }

                                }
                            })

                        }
            }

            if (selectedDay == DAY_BETA) {
                TimeSlotData.listDayBeta[position] = "not available"
                ref.setValue(TimeSlotData.listDayBeta)
                        .addOnCompleteListener {

                            val userRef = FirebaseDatabase.getInstance().getReference("/Users/$uid")
                            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {

                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    val day_two: String? = p0.child("/day_two").value.toString()

                                    val appointIDRef = FirebaseDatabase.getInstance().getReference("/Admin/appointIDs/$DAY_BETA")
                                    val appointRef = FirebaseDatabase.getInstance().getReference("/appointments").push()
                                    AppointmentData.appointmentIDDayBeta[position] = appointRef.key!!

                                    appointIDRef.setValue(AppointmentData.appointmentIDDayBeta)

                                    val appoint = Appointment(email!!, uid!!, day_two!!, Common.currentItem?.Time!!,
                                            appointRef.key!!, selectedDay_name!!,"upcoming",DAY_BETA,position)

                                    appointRef.setValue(appoint).addOnCompleteListener {

                                        val reference = FirebaseDatabase.getInstance().getReference("/Users/$uid/appointmentID")
                                        reference.addListenerForSingleValueEvent(object : ValueEventListener {
                                            override fun onCancelled(p0: DatabaseError) {

                                            }

                                            override fun onDataChange(p0: DataSnapshot) {

                                                if (p0.exists()) {
                                                    val generic: GenericTypeIndicator<ArrayList<String?>?> = object : GenericTypeIndicator<ArrayList<String?>?>() {}
                                                    val b: ArrayList<String?> = p0.getValue(generic)!!

                                                    b.add(appointRef.key)
                                                    reference.setValue(b).addOnCompleteListener {

                                                        val latestAppointRef = FirebaseDatabase.getInstance().getReference("/Users/$uid/latestappointmentID")
                                                        latestAppointRef.setValue(appointRef.key)

                                                        val action = AppointSelectFragmentDirections.actionAppointSelectFragmentToAppointConfirmFragment()
                                                        Navigation.findNavController(view).navigate(action)
                                                    }
                                                } else {
                                                    val b: ArrayList<String?> = ArrayList<String?>()
                                                    b.add(appointRef.key)

                                                    reference.setValue(b).addOnCompleteListener {

                                                        val latestAppointRef = FirebaseDatabase.getInstance().getReference("/Users/$uid/latestappointmentID")
                                                        latestAppointRef.setValue(appointRef.key)

                                                        val action = AppointSelectFragmentDirections.actionAppointSelectFragmentToAppointConfirmFragment()
                                                        Navigation.findNavController(view).navigate(action)
                                                    }
                                                }

                                            }
                                        })

                                    }

                                }
                            })

                        }
            }

            if (selectedDay == DAY_GAMMA) {
                TimeSlotData.listDayGamma[position] = "not available"
                ref.setValue(TimeSlotData.listDayGamma)
                        .addOnCompleteListener {

                            val userRef = FirebaseDatabase.getInstance().getReference("/Users/$uid")
                            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {

                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    val day_three: String? = p0.child("/day_three").value.toString()

                                    val appointIDRef = FirebaseDatabase.getInstance().getReference("/Admin/appointIDs/$DAY_GAMMA")
                                    val appointRef = FirebaseDatabase.getInstance().getReference("/appointments").push()
                                    AppointmentData.appointmentIDDayGamma[position] = appointRef.key!!

                                    appointIDRef.setValue(AppointmentData.appointmentIDDayGamma)

                                    val appoint = Appointment(email!!, uid!!, day_three!!, Common.currentItem?.Time!!,
                                            appointRef.key!!, selectedDay_name!!,"upcoming",DAY_GAMMA, position)
                                    appointRef.setValue(appoint).addOnCompleteListener {

                                        val reference = FirebaseDatabase.getInstance().getReference("/Users/$uid/appointmentID")
                                        reference.addListenerForSingleValueEvent(object : ValueEventListener {
                                            override fun onCancelled(p0: DatabaseError) {

                                            }

                                            override fun onDataChange(p0: DataSnapshot) {

                                                if (p0.exists()) {
                                                    val generic: GenericTypeIndicator<ArrayList<String?>?> = object : GenericTypeIndicator<ArrayList<String?>?>() {}
                                                    val b: ArrayList<String?> = p0.getValue(generic)!!

                                                    b.add(appointRef.key)
                                                    reference.setValue(b).addOnCompleteListener {

                                                        val latestAppointRef = FirebaseDatabase.getInstance().getReference("/Users/$uid/latestappointmentID")
                                                        latestAppointRef.setValue(appointRef.key)

                                                        val action = AppointSelectFragmentDirections.actionAppointSelectFragmentToAppointConfirmFragment()
                                                        Navigation.findNavController(view).navigate(action)
                                                    }
                                                }

                                                else {
                                                    val b: ArrayList<String?> = ArrayList<String?>()
                                                    b.add(appointRef.key)

                                                    reference.setValue(b).addOnCompleteListener {

                                                        val latestAppointRef = FirebaseDatabase.getInstance().getReference("/Users/$uid/latestappointmentID")
                                                        latestAppointRef.setValue(appointRef.key)

                                                        val action = AppointSelectFragmentDirections.actionAppointSelectFragmentToAppointConfirmFragment()
                                                        Navigation.findNavController(view).navigate(action)
                                                    }
                                                }

                                            }
                                        })

                        }
            }
                            })

                        }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.confirm -> {
                if (Common.currentItem != null) {
                    for (i in 0..11) {
                        if (Constant.listOfTimeSlots[i] == Common.currentItem!!.Time) {
                            index = i
                            bookAppointment(index , v)
                        }
                    }

                } else
                    Toast.makeText(requireContext(), "Please select a time slot", Toast.LENGTH_SHORT).show()
            }
            R.id.back -> {
               activity?.onBackPressed()
            }
            R.id.day_one_holder -> dayOneChecked()
            R.id.day_two_holder -> dayTwoChecked()
            R.id.day_three_holder -> dayThreeChecked()
        }
    }


}