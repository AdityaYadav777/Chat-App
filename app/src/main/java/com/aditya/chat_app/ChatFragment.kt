package com.aditya.chat_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.aditya.chat_app.adapter.userAdapter
import com.aditya.chat_app.databinding.FragmentChatBinding
import com.aditya.chat_app.modelClass.myUsers
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database


class ChatFragment : Fragment() {
  lateinit var binding: FragmentChatBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentChatBinding.inflate(layoutInflater)

        val listOfUser= arrayListOf<myUsers>()

        val mdbRef=Firebase.database.reference
        mdbRef.child("Users").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for (data in snapshot.children){
                    val user=data.getValue(myUsers::class.java)
                    if(FirebaseAuth.getInstance().currentUser?.uid!=user!!.uid){
                        listOfUser.add(user!!)
                    }

                }

                binding.myRec.adapter=userAdapter(listOfUser,requireContext())
                binding.myRec.layoutManager=LinearLayoutManager(requireContext())

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })



        return binding.root
    }


}