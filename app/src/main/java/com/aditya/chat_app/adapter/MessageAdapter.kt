package com.aditya.chat_app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.aditya.chat_app.R
import com.aditya.chat_app.modelClass.Message
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context,val messageList:ArrayList<Message>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECIEVE=1
    val ITEM_SEND=2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       if (viewType==1){
           return recivedViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recived,parent,false))
       }else{
        return sendViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.send,parent,false))
       }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {

        val currentMessage=messageList[position]
        if(FirebaseAuth.getInstance().currentUser!!.uid.equals(currentMessage.senderId)){
            return ITEM_SEND
        }else{
            return ITEM_RECIEVE
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(holder.javaClass==sendViewHolder::class.java){

            val viewHolder=holder as sendViewHolder
            holder.sendMessage.text=messageList[position].message
        }else{
            val viewHolder= holder as recivedViewHolder
            holder.recieveMessage.text=messageList[position].message
        }

    }

    class sendViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
val sendMessage=itemView.findViewById<TextView>(R.id.sendMessage)
    }

    class recivedViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
val recieveMessage=itemView.findViewById<TextView>(R.id.reciveMessage)
    }
}