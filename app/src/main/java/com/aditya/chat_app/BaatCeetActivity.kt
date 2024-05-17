package com.aditya.chat_app

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.aditya.chat_app.adapter.MessageAdapter
import com.aditya.chat_app.databinding.ActivityBaatCeetBinding
import com.aditya.chat_app.modelClass.Message
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class BaatCeetActivity : AppCompatActivity() {

    lateinit var binding:ActivityBaatCeetBinding
    var senderRoom:String?=null
    var reciverRoom:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityBaatCeetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

  binding.name.text=intent.getStringExtra("name")
        binding.photo.load(intent.getStringExtra("url"))
        val reciverUid=intent.getStringExtra("uid")
        val senderUid=FirebaseAuth.getInstance().currentUser!!.uid
changeStatusBarColor()
        senderRoom=reciverUid+senderUid
        reciverRoom=senderRoom+reciverUid

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }


        val mdbRef=Firebase.database.reference
val listMsg= arrayListOf<Message>()
mdbRef.child("chats").child(senderRoom!!).child("message").addValueEventListener(object: ValueEventListener{
    override fun onDataChange(snapshot: DataSnapshot) {
        for (data in  snapshot.children){
            val messagesData=data.getValue(Message::class.java)
            listMsg.add(messagesData!!)
        }
        binding.rec.adapter=MessageAdapter(this@BaatCeetActivity,listMsg)
        val linerarManager=LinearLayoutManager(this@BaatCeetActivity)
        linerarManager.stackFromEnd=true
        binding.rec.layoutManager=linerarManager

    }

    override fun onCancelled(error: DatabaseError) {
        TODO("Not yet implemented")
    }

})

        binding.sendBtn.setOnClickListener {
val message=binding.message.text.toString()

            val msgObj=Message(message,senderUid)

            mdbRef.child("chats").child(senderRoom!!).child("message").push()
                .setValue(msgObj).addOnSuccessListener {
                    mdbRef.child("chats").child(reciverRoom!!).child("message").push()
                        .setValue(msgObj).addOnSuccessListener {

                        }

                }

      binding.message.setText(" ")

        }




    }
    fun changeStatusBarColor() {
        val window = this?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = ContextCompat.getColor(this, R.color.white)
        window?.decorView?.let {
            WindowCompat.getInsetsController(window, it).apply {
                isAppearanceLightStatusBars = true
            }
        }
    }
}