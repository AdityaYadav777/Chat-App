package com.aditya.chat_app.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.aditya.chat_app.BaatCeetActivity
import com.aditya.chat_app.R
import com.aditya.chat_app.modelClass.myUsers

class userAdapter(val listOfUser: ArrayList<myUsers>,val requireContext: Context) :RecyclerView.Adapter<userAdapter.myViewHolder>(){
    class myViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val profile=itemView.findViewById<ImageView>(R.id.phpto)
        val name=itemView.findViewById<TextView>(R.id.name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
      return myViewHolder(
          LayoutInflater.from(parent.context).inflate(R.layout.item_view,parent,false)
      )
    }

    override fun getItemCount(): Int {
       return listOfUser.size
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
     holder.name.text=listOfUser[position].name
        holder.profile.load(listOfUser[position].url)
        holder.itemView.setOnClickListener {
            val i=Intent(requireContext,BaatCeetActivity::class.java)
                i.putExtra("url",listOfUser[position].url)
              i.putExtra("uid",listOfUser[position].uid)
            i.putExtra("name",listOfUser[position].name)
            requireContext.startActivity(i)


        }
    }
}