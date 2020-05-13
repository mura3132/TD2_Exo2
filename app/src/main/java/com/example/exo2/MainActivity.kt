package com.example.exo2

import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    val MY_PERMISSIONS_REQUEST_SMS_RECEIVE = 10
    val listContact = arrayListOf<Contact>()
    lateinit var adapter: ContactAdapter
    lateinit var layoutManager : LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layoutManager = LinearLayoutManager(this)
        recycler.layoutManager = layoutManager

        adapter = ContactAdapter(this)
        recycler.adapter = adapter

        loadContacts()

       // sendMail()

        saveBtn.setOnClickListener {
            saveContactConfiguration()
        }

    }

    private fun sendMail() {
        val CUSTOM_INTENT = "random_test"
        val i = Intent()
        i.action = CUSTOM_INTENT
        i.setClass(this, SmsReceiver::class.java)
        sendBroadcast(i)
    }

    private fun loadContacts() {
        var builder = StringBuilder()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (checkSelfPermission(
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(
                Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.RECEIVE_SMS),
                PERMISSIONS_REQUEST_READ_CONTACTS)
            //callback onRequestPermissionsResult
        } else {
            builder = getContacts()
            adapter.notifyDataSetChanged()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts()
            } else {
                //  toast("Permission must be granted in order to display contacts information")
            }
        }
    }

    fun saveContactConfiguration(){

        val pref = getSharedPreferences("PREF",0)
        val editor = pref.edit()
        val nameList = mutableSetOf<String>()

        for (contact in listContact){
            if (contact.selected){
                nameList.add(contact.toString())
            }
        }

        editor.putStringSet("listContact", nameList)

        editor.commit()

    }

    private fun getContacts(): StringBuilder {
        val builder = StringBuilder()
        val resolver: ContentResolver = contentResolver;
        val cursor = resolver.query(
            ContactsContract.Contacts.CONTENT_URI, null, null, null,
            null)

        if (cursor != null && cursor.count > 0) {
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phoneNumber = (cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))).toInt()

                // get the user's email address
                // get the user's email address
                var email: String = ""
                val ce = contentResolver.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                    arrayOf(id),
                    null
                )
                if (ce != null && ce.moveToFirst()) {
                    email =
                        ce.getString(ce.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
                    ce.close()
                }

                if (phoneNumber > 0) {
                    val cursorPhone = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", arrayOf(id), null)

                    if(cursorPhone != null && cursorPhone.count > 0) {
                        while (cursorPhone.moveToNext()) {
                            val phoneNumValue = cursorPhone.getString(
                                cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            builder.append("Contact: ").append(name).append(", Phone Number: ").append(
                                phoneNumValue).append("\n\n")
                            listContact.add(Contact(name, phoneNumValue, email, false))
                            Log.e("Name ===>",phoneNumValue)
                        }
                    }

                    cursorPhone?.close()
                }
            }
        } else {
            //   toast("No contacts available!")
        }
        cursor?.close()
        return builder
    }
}
