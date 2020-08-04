package com.jyotishapp.jyotishi.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jyotishapp.jyotishi.AppointPastFragment
import com.jyotishapp.jyotishi.AppointUpcomingFragment

class ViewPagerAdapter(manager: FragmentActivity) : FragmentStateAdapter(manager) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
     return when(position){
         0 -> AppointUpcomingFragment()
         1 -> AppointPastFragment()
         else -> AppointUpcomingFragment()
     }
    }
}