package com.aditya.chat_app.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import coil.load
import com.aditya.chat_app.databinding.FragmentHomeBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    var myUri:Uri?=null
    var url:String?=null
    private lateinit var database: DatabaseReference
    val uid= FirebaseAuth.getInstance().currentUser!!.uid
// ...


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        database = Firebase.database.reference
        binding.getProfile.setOnClickListener {
            launcher.launch("image/*")
        }


       getData()
        binding.btn.setOnClickListener {
            val name=binding.getName.text.toString()

            if(name.isNotEmpty()){
                uploadData()
            }else{
                Toast.makeText(requireContext(),"Enter Name",Toast.LENGTH_SHORT).show()
            }

        }
        return binding.root
    }

    private fun getData() {

        database.child("Users").child(uid).get().addOnSuccessListener {data->

            if (data.value!=null) {
                binding.getProfile.load(data.child("url").value)
                binding.getName.setText(data.child("name").value.toString())
                myUri=data.child("url").value as Uri
            }
        }


    }

    private fun uploadData() {
         val db=Firebase.database
        val storageRef = Firebase.storage.reference
        val sd = getFileName(requireContext(), myUri!!)
       val ref= storageRef.child("Img/${sd}").putFile(myUri!!).addOnSuccessListener {
           Toast.makeText(requireContext(),"Upload data",Toast.LENGTH_SHORT).show()
       storageRef.child("Img/${sd}").downloadUrl.addOnSuccessListener {
           url=it.toString()
           Toast.makeText(requireContext(),"Url Downloaded",Toast.LENGTH_SHORT).show()

           uploadInRealtimeDB()

       }

       }

    }

    private fun uploadInRealtimeDB(){
        val name=binding.getName.text.toString()

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener {task->

        val    token=task.result


            val data= hashMapOf(
                "uid" to uid,
                "name" to name,
                "url" to url,
                "token" to token
            )
            database.child("Users").child(uid).setValue(data).addOnCompleteListener {
                Toast.makeText(requireContext(),"Uploaded all data",Toast.LENGTH_SHORT).show()
                val i=Intent(requireContext(),MainScreenActivity::class.java)
                requireContext().startActivity(i)
                requireActivity().finish()
            }

        })


    }

    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->

        if (uri != null) {
            binding.getProfile.setImageURI(uri)
            myUri=uri

        }

    }
    @SuppressLint("Range")
    private fun getFileName(context: Context, uri: Uri): String? {
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor.use {
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                }
            }
        }
        return uri.path?.lastIndexOf('/')?.let {
            uri.path?.substring(it)
        }
    }
}