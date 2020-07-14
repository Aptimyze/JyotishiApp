package com.jyotishapp.jyotishi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.jyotishapp.jyotishi.Common.AppointmentData
import com.jyotishapp.jyotishi.Models.Appointment

class AppointHostActivity : BaseClass() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appoint_host)

        supportActionBar?.hide()

        initializeViews()

    }
}