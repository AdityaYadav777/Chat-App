package com.aditya.chat_app.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.fragment.findNavController
import com.aditya.chat_app.R
import com.google.firebase.auth.FirebaseAuth


class SpalshFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val auth=FirebaseAuth.getInstance()
        Handler(Looper.getMainLooper()).postDelayed({
changeStatusBarColor()
            if(auth.currentUser!=null){
             startActivity(Intent(requireContext(),MainScreenActivity::class.java))
            }else{  findNavController().navigate(R.id.action_spalshFragment_to_loginFragment)}


        },2000)




        return inflater.inflate(R.layout.fragment_spalsh, container, false)
    }

    fun changeStatusBarColor() {
        val window = activity?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.themeColor)
        window?.decorView?.let {
            WindowCompat.getInsetsController(window, it).apply {
                isAppearanceLightStatusBars = true
            }
        }
    }

}