package com.jyotishapp.jyotishi.Models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class DateTimeWrapper(val date : String, val time : String) : Parcelable{
    constructor() : this("","")
}