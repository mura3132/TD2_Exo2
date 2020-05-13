package com.example.exo2

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat.getSystemService
import java.util.*
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart


class MailSender {


    fun sendMail(context : Context, intent : Intent, name : String, email : String){
        val username = "iamexo2td2@gmail.com"
        val password = "azerty@123"

        val props = Properties()
        props.put("mail.smtp.auth", "true")
        props.put("mail.smtp.starttls.enable", "true")
        props.put("mail.smtp.host", "smtp.gmail.com")
        props.put("mail.smtp.port", "587")

        val session: Session = Session.getInstance(props,
            object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(username, password)
                }
            })


        try {
            val message: Message = MimeMessage(session)
            message.setFrom(InternetAddress("from-email@gmail.com"))
            message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(email)
            )
            message.setSubject("Sms Received")
            message.setText(
                "Mr.$name ;\n" +
                        "We have received your messgae"
            )


            Transport.send(message)

            val titre = "Mail sent succefully"
            val contenu = "Un email a ete envoyé a Mr. $name"

            val pIntent = PendingIntent.getActivity(context, System.currentTimeMillis().toInt(), intent, 0)
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val mChannel = NotificationChannel(
                    "ch00", "ch00", NotificationManager.IMPORTANCE_HIGH)
                notificationManager.createNotificationChannel(mChannel)
                val noti = Notification.Builder(context,"ch00")
                    .setContentTitle(titre)
                    .setContentText(contenu).setSmallIcon(android.R.drawable.btn_star)
                    .setContentIntent(pIntent).setAutoCancel(true)

                    .build()
                notificationManager.notify(0, noti)

            }else{

                val noti = Notification.Builder(context)
                    .setContentTitle(titre)
                    .setContentText(contenu).setSmallIcon(android.R.drawable.btn_star)
                    .setContentIntent(pIntent).setAutoCancel(true)

                    .build()
                notificationManager.notify(0, noti)

            }

        } catch (e : MessagingException ) {
            throw RuntimeException(e)
        }
    }
}