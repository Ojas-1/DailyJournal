package com.example.dailyjournal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dailyjournal.databinding.FragmentWelcomeScreenBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


/**
 * A simple [Fragment] subclass.
 * Use the [WelcomeScreen.newInstance] factory method to
 * create an instance of this fragment.
 */
class WelcomeScreen : Fragment() {

    private var _binding: FragmentWelcomeScreenBinding? = null
    // A list to store all the artist from firebase database.


    private lateinit var viewModel : WelcomeActivityViewModel

    // Our database reference object.

    val entries = mutableListOf<Entry>()

    val db = Firebase.firestore

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {




        val user = arguments?.getString("user")


        db.collection("$user")
            .get()
            .addOnSuccessListener { result ->

                for (document in result) {
                    val entry = Entry(document.data["title"].toString(),document.data["date"].toString(),document.data["text"].toString(),
                        document.data["uri"].toString())

                    Log.i("Entry", entry.title)
                    entries.add(entry)
                }


                val rv = binding.rvStudent

                rv.layoutManager = LinearLayoutManager(activity)

                rv.adapter = RvAdapter(entries) { it: Entry ->
                    list(it)
                }


            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "Error getting documents: ", exception)

            }

        _binding = FragmentWelcomeScreenBinding.inflate(inflater, container, false)




        binding.btnAdd.setOnClickListener{
            val bundle = bundleOf("user" to user)
            findNavController().navigate(R.id.action_welcomeScreen2_to_createJournal,bundle)
        }

        binding.btnLeave.setOnClickListener{

            findNavController().navigate(R.id.action_welcomeScreen2_to_signIn)
        }


        return binding.root

    }

    private fun list(fruit:Entry){
        val user = arguments?.getString("user")
        val bundle = bundleOf("user" to user ,"title" to fruit.title, "text" to fruit.text, "uri" to fruit.uri)

        findNavController().navigate(R.id.action_welcomeScreen2_to_journalVIew,bundle)

    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}