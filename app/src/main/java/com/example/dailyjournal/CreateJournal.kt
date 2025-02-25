package com.example.dailyjournal

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dailyjournal.databinding.FragmentCreateJournalBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.ByteArrayOutputStream
import java.time.LocalDate


class CreateJournal : Fragment() {

    private var _binding: FragmentCreateJournalBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var gallery2: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCreateJournalBinding.inflate(inflater, container, false)

        val user = arguments?.getString("user")

        val db = Firebase.firestore

        var uri: String =""


        gallery2 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { ur ->
                    // Handle the selected URI
                    binding.imageDataView.setImageURI(ur)
                    // Do other operations with the URI as needed
                    uri = ur.toString()

                    // Step 1: Save image as Bitmap
                    val bitmapFromImageView: Bitmap? = getBitmapFromImageView(binding.imageDataView)

                    bitmapFromImageView?.let { bitmap ->
                        uri = bitmapToBase64String(bitmap)

                    }


                }
            } else {
                // User canceled or something went wrong
                Toast.makeText(activity, "Action canceled", Toast.LENGTH_SHORT).show()
            }
        }



       // openDocumentPicker()



        binding.apply{

            val gallery = registerForActivityResult(/* contract = */ ActivityResultContracts.GetContent(),
                /* callback = */ ActivityResultCallback{
                    uri=it.toString()
                    imageDataView.setImageURI(it)
                })


            btnAdd.setOnClickListener {
                val data = hashMapOf(
                    "title" to binding.tvName1.text.toString(),
                    "date" to LocalDate.now().toString(),
                    "text" to binding.etDataView.text.toString(),
                    "uri" to uri,
                )

                db.collection("$user").document("${data["title"]}")
                    .set(data)
                    .addOnSuccessListener {
                        val bundle= bundleOf("user" to user)

                        findNavController().navigate(R.id.action_createJournal_to_welcomeScreen2,bundle)

                    }
                    .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }


            }


                btnAddImage.setOnClickListener {
                  //  openDocumentPicker()

                 gallery.launch("image/*")
            }


            btnLeave.setOnClickListener {

                val bundle = bundleOf("user" to user)
                findNavController().navigate(R.id.action_createJournal_to_welcomeScreen2,bundle)

            }
        }

        return binding.root

    }

    private fun bitmapToBase64String(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun getBitmapFromImageView(imageView: ImageView): Bitmap? {
        // Get the drawable from the ImageView
        val drawable = imageView.drawable

        // Convert the drawable to a Bitmap
        return if (drawable != null) {
            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = android.graphics.Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        } else {
            null
        }
    }



    private fun openDocumentPicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"

        gallery2.launch(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}