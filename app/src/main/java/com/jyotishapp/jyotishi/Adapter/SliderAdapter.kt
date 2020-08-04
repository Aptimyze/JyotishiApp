package com.jyotishapp.jyotishi.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.jyotishapp.jyotishi.R

class SliderAdapter(var context: Context) : PagerAdapter() {

    lateinit  var layoutInflater : LayoutInflater

    val slideimages = arrayOf(
       R.drawable.home,
            R.drawable.dial,
            R.drawable.options,
            R.drawable.social
    )

    val slideheading = arrayOf("Your Horoscope is just one call away Download the all new jyotishi app.",
            "We provide all our users with a free calling for 30 minutes with our professional astrologer.",
            "You can either text, video call or voice call our jyotish.",
            "You can find us and know more about us through Facebook, Instagram and Email id.")

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as LinearLayout
    }

    override fun getCount(): Int {
        return slideheading.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

    layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val view = layoutInflater.inflate(R.layout.slidelayout,container,false)

    val slideImageview : ImageView =  view.findViewById(R.id.slideimage) as ImageView
    val slidehead : TextView =  view.findViewById(R.id.slideheading) as TextView

        slideImageview.setImageResource(slideimages[position])
        slidehead.text = slideheading[position]

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

        container.removeView(`object` as LinearLayout)

    }
}