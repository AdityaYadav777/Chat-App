package com.aditya.chat_app.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.aditya.chat_app.R
import com.aditya.chat_app.databinding.FragmentLoginBinding
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.auth.auth
import java.util.concurrent.TimeUnit


class LoginFragment : Fragment() {
lateinit var binding:FragmentLoginBinding
    val auth=FirebaseAuth.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentLoginBinding.inflate(layoutInflater,container,false)

if (auth.currentUser!=null){
    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
}



        binding.btn.setOnClickListener {

            val conCode = "+91"
            val num = conCode + binding.getNumber.text.toString().trim()
            val bundle=Bundle()
            bundle.putString("number",num)
            findNavController().navigate(R.id.action_loginFragment_to_otpFragment,bundle)

        }



        return binding.root
    }

}