package com.jyotishapp.jyotishi

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.jyotishapp.jyotishi.Adapter.SliderAdapter
import kotlinx.android.synthetic.main.activity_on_board.*

class OnBoardActivity : AppCompatActivity() {

    val SHARED_PREFS = "sharedPrefs"
    val KEY ="key"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_board)

        finish.visibility = View.INVISIBLE

        supportActionBar?.hide()

        val sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val shouldShowOnBoard = false
        editor.putBoolean(KEY,shouldShowOnBoard)
        editor.apply()

        val adapter = SliderAdapter(this)
        val viewPager = findViewById<ViewPager>(R.id.onBoardViewPager)
        viewPager.adapter = adapter

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
              showCurrentPage(position)
            }
        })

        finish.setOnClickListener{
            val intent = Intent(this,MainScreen::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

    }

    private fun showCurrentPage(pos : Int){
      when(pos){
          0 -> {
              dot1.setBackgroundResource(R.drawable.dotselect)
              dot1.alpha = 1f
              dot2.setBackgroundResource(R.drawable.dot)
              dot2.alpha = 0.5f
              dot3.setBackgroundResource(R.drawable.dot)
              dot3.alpha = 0.5f
              dot4.setBackgroundResource(R.drawable.dot)
              dot4.alpha = 0.5f
          }

          1 -> {
              dot1.setBackgroundResource(R.drawable.dot)
              dot1.alpha = 0.5f
              dot2.setBackgroundResource(R.drawable.dotselect)
              dot2.alpha = 1f
              dot3.setBackgroundResource(R.drawable.dot)
              dot3.alpha = 0.5f
              dot4.setBackgroundResource(R.drawable.dot)
              dot4.alpha = 0.5f
          }

          2 ->
              {
                  dot1.setBackgroundResource(R.drawable.dot)
                  dot1.alpha = 0.5f
                  dot2.setBackgroundResource(R.drawable.dot)
                  dot2.alpha = 0.5f
                  dot3.setBackgroundResource(R.drawable.dotselect)
                  dot3.alpha = 1f
                  dot4.setBackgroundResource(R.drawable.dot)
                  dot4.alpha = 0.5f
              }

          3 -> {
              dot1.setBackgroundResource(R.drawable.dot)
              dot1.alpha = 0.5f
              dot2.setBackgroundResource(R.drawable.dot)
              dot2.alpha = 0.5f
              dot3.setBackgroundResource(R.drawable.dot)
              dot3.alpha = 0.5f
              dot4.setBackgroundResource(R.drawable.dotselect)
              dot4.alpha = 1f
              finish.visibility = View.VISIBLE
          }

      }
    }
}