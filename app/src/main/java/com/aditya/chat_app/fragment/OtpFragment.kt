package com.aditya.chat_app.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aditya.chat_app.R
import com.aditya.chat_app.databinding.FragmentOtpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


class OtpFragment : Fragment() {
    val auth = FirebaseAuth.getInstance()
    lateinit var storedVerificationId: String
    lateinit var binding: FragmentOtpBinding
    lateinit var dialog: ProgressDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOtpBinding.inflate(layoutInflater)

        val bundle: Bundle? = arguments
        val number = bundle?.getString("number")
        binding.showNum.text = "Verify ${number}"


        dialog = ProgressDialog(requireContext())
        dialog.setTitle("Loading")
        dialog.setMessage("Please Wait")
        dialog.show()

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number!!) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

        binding.verifyBtn.setOnClickListener {
            val otp = binding.editText.text.toString()
            if (otp.isNotEmpty() && otp.isNotBlank()) {

                val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, otp)
                signInWithPhoneAuthCredential(credential)

            } else {

                Toast.makeText(requireContext(), "Enter Data Correct", Toast.LENGTH_SHORT).show()
            }

        }

        return binding.root
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    findNavController().navigate(R.id.action_otpFragment_to_homeFragment)
                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    Toast.makeText(requireContext(), "Faild To Login", Toast.LENGTH_SHORT).show()
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            val otpCode=credential.smsCode.toString()

            binding.editText.setText(otpCode)


            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Toast.makeText(requireContext(), "Verification failded", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
            if (e is FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(requireContext(), "Invaled request", Toast.LENGTH_SHORT).show()
                // Invalid request
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Toast.makeText(requireContext(), "SMS Khatm", Toast.LENGTH_SHORT).show()
            } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                // reCAPTCHA verification attempted with null Activity
                Toast.makeText(requireContext(), "Recatcha null", Toast.LENGTH_SHORT).show()
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            dialog.dismiss()
            Toast.makeText(requireContext(), "Send", Toast.LENGTH_SHORT).show()
            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            val resendToken = token
        }
    }


}