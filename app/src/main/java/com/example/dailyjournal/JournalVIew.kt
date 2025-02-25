package com.example.dailyjournal

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.dailyjournal.databinding.FragmentJournalVIewBinding
import com.example.dailyjournal.databinding.FragmentWelcomeScreenBinding



class JournalVIew : Fragment() {
    private var _binding: FragmentJournalVIewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val title = arguments?.getString("title")
        val text = arguments?.getString("text")
        val user = arguments?.getString("user")
        val uri = arguments?.getString("uri")

        _binding= FragmentJournalVIewBinding.inflate(inflater, container, false)





        binding.apply{
            textView.setText(title)
            etDataView.setText(text)
            if(uri!=""){

                    try{
                        binding.imageDataView.setImageURI(uri?.toUri())
                    }catch( e: SecurityException){
                        binding.imageDataView.setImageResource(R.drawable.pencil)
                    }



//                // Replace with your actual Base64-encoded string
//                val base64String = uri
//
//                // Step 1: Convert Base64-encoded string to Bitmap
//                val decodedBitmap: Bitmap? = base64String?.let { base64StringToBitmap(it) }
//
//                // Step 2: Display the decoded Bitmap in an ImageView
//                decodedBitmap?.let { bitmap ->
//                    binding.imageDataView.setImageBitmap(bitmap)
//                }
            }

        }


            binding.btnLeave.setOnClickListener{
                val bundle = bundleOf("user" to user)
                findNavController().navigate(R.id.action_journalVIew_to_welcomeScreen2,bundle)
            }
        return binding.root

    }

    private fun base64StringToBitmap(base64String: String): Bitmap? {
        try {
            val decodedByteArray = Base64.decode(base64String, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.size)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}