package com.example.exo2

class Contact (val name : String, val phoneNumber : String, val email : String, var selected : Boolean) {


    fun setSelectedContact(){
        selected = !selected
    }

    override fun toString() : String{
        return "$name|$phoneNumber|$email"
    }
}
