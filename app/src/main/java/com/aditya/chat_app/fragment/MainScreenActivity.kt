package com.aditya.chat_app.fragment

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.aditya.chat_app.CalFragment
import com.aditya.chat_app.ChatFragment
import com.aditya.chat_app.CommunitiesFragment
import com.aditya.chat_app.R
import com.aditya.chat_app.UpdateFragment
import com.aditya.chat_app.databinding.ActivityMainScreenBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainScreenActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }




swapFragment(ChatFragment())
        binding.myNav.setOnItemSelectedListener {

            when(it.itemId){

                R.id.ChatFragment->swapFragment(ChatFragment())
                R.id.UpdateFragment->swapFragment(UpdateFragment())
                R.id.CommunitiesFragment->swapFragment(CommunitiesFragment())
                R.id.CalFragment->swapFragment(CalFragment())
                else->{

                }
            }
            true

        }
        binding.dots.setOnLongClickListener {

           val auth= FirebaseAuth.getInstance()
            Firebase.auth.signOut()
            val i=Intent(this,LoginFragment::class.java)
            startActivity(i)
            finish()

            return@setOnLongClickListener true

        }


    }
    private fun swapFragment(fragment:Fragment){
        val fmanager=supportFragmentManager
        val manager = fmanager.beginTransaction()
        manager.replace(R.id.fragment, fragment)
        manager.commit()

    }


}