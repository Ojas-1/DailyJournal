package com.example.dailyjournal

import android.os.Bundle
import android.service.autofill.Validators
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.dailyjournal.databinding.FragmentAccountCreateBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth



/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class CreateAcountFragment : Fragment() {

    private var _binding: FragmentAccountCreateBinding? = null
    private lateinit var auth: FirebaseAuth


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAccountCreateBinding.inflate(inflater, container, false)
         auth = Firebase.auth




        // home screen
        binding.btnSignUp1.setOnClickListener {



            if(validate()){
                val email = binding.textEmail3.text.toString()

                val password = binding.etPassword3.text.toString()
                createUser(email,password)

            }





        }

        // back screen
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_createAcountFragment_to_signIn)
        }



        return binding.root

    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

fun validate() : Boolean {
    if (!TextUtils.isEmpty(binding.textEmail3.text) && !TextUtils.isEmpty(binding.etPassword3.text)) {
        if (Patterns.EMAIL_ADDRESS.matcher(binding.textEmail3.text).matches()) {
            // check if password is valid
            if(validPassword(binding.etPassword3.text.toString())){
                return true
            }else{
                Toast.makeText(
                    activity,
                    R.string.invalid_password,
                    Toast.LENGTH_LONG
                ).show()

            }
        } else {
            Toast.makeText(
                activity,
                "Please enter a correct email address",
                Toast.LENGTH_LONG
            ).show()
        }
    } else {
        Toast.makeText(activity, "Email or password field is empty", Toast.LENGTH_LONG).show()
    }

    return false

}

    fun createUser(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("OJ", "createUserWithEmail:success")

                    val bundle = bundleOf("user" to email)

                    findNavController().navigate(R.id.action_createAcountFragment_to_welcomeScreen2,bundle)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("OJ", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        activity,
                        "Authentication failed. User exits or database could not be reached",
                        Toast.LENGTH_LONG,
                    ).show()

                }
            }
    }
    fun validPassword(password: String?) : Boolean {
        if (password.isNullOrEmpty()) {
            return false
        }

        // Min 8 char, 1 letter, 1 number
        val passwordRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}\$")
        return passwordRegex.matches(password)
    }
}