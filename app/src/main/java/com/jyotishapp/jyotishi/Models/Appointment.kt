package com.jyotishapp.jyotishi.Models

class Appointment(val email : String , val clientUid : String , val date : String ,
                  val time:String, val key : String, val dayName : String ,
                  val status : String, val dayIdentifier : String, val position : Int)

{

    constructor() : this("","","","", "", "","","",-1)
}