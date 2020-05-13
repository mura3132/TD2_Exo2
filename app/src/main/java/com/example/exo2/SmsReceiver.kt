package com.example.exo2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log


class SmsReceiver : BroadcastReceiver() {
    lateinit var mContext: Context


    override fun onReceive(context: Context, intent: Intent) {

        mContext = context

        val pref = context.getSharedPreferences("PREF",0)

        val listContact = pref.getStringSet("listContact", mutableSetOf())

        val bundle = intent.extras

        val messages =
            bundle!!["pdus"] as Array<Any>?
        val smsMessage =
            arrayOfNulls<SmsMessage>(messages!!.size)

        for (n in messages.indices) {
            smsMessage[n] =
                SmsMessage.createFromPdu(messages[n] as ByteArray)
        }
        val receivedMessage =
            smsMessage[0]!!.messageBody.toString().toUpperCase()
        var originatingAddress = smsMessage[0]!!.originatingAddress
        originatingAddress =
            if (originatingAddress != null && originatingAddress.length > 3) originatingAddress.substring(
                3
            ) else ""

        val newList = arrayListOf<Contact>()
        val newListNumbers = arrayListOf<String>()

        originatingAddress = originatingAddress.replace(" ", "")
        originatingAddress = "+213$originatingAddress"
        for (element in listContact!!.toList()){
            val name = element.split("|")[0]
            var number = element.split("|")[1]
            val email = element.split("|")[2]

            number = number.replace(" ", "")

            if (number == originatingAddress){

                val thread = Thread(Runnable {
                    try {
                        val sender = MailSender()
                        sender.sendMail(context, intent, name, email)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                })

                thread.start()

                Log.i("done", "message sent")
            }
            newList.add(Contact(name, number, email, false))
            newListNumbers.add(number)
        }





    }

}