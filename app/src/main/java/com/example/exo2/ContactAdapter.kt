package com.example.exo2


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ContactAdapter(val activity : MainActivity) : RecyclerView.Adapter<ContactAdapter.NoteViewHolder>(){
    class NoteViewHolder(v : View) : RecyclerView.ViewHolder(v){
        val contactLayout = v.findViewById<RelativeLayout>(R.id.contactLayout)
        val nameContact = v.findViewById<TextView>(R.id.nomView)
        val phoneContact = v.findViewById<TextView>(R.id.phoneView)
        val emailContact = v.findViewById<TextView>(R.id.emailView)
        val contactSelected = v.findViewById<CheckBox>(R.id.contactSelected)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(LayoutInflater.from(activity).inflate(R.layout.contact_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return activity.listContact.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.nameContact.text = activity.listContact[position].name
        holder.phoneContact.text = activity.listContact[position].phoneNumber
        holder.emailContact.text =  activity.listContact[position].email
        holder.contactSelected.isChecked = activity.listContact[position].selected
        holder.contactSelected.setOnCheckedChangeListener(null)

        holder.contactSelected.setOnClickListener {
            activity.listContact[position].setSelectedContact()
        }

    }
}