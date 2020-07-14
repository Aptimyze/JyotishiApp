package com.jyotishapp.jyotishi.Interface

import android.view.View

interface ItemClickListener {
    fun onClick(view: View, position: Int): Unit
}