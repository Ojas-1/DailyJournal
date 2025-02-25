package com.example.dailyjournal

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.dailyjournal.databinding.FragmentSigninBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import org.w3c.dom.Text
import kotlin.math.sign

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class signIn : Fragment() {
    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentSigninBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        _binding = FragmentSigninBinding.inflate(inflater, container, false)

    if(auth.currentUser != null){
        auth.signOut()
    }
        binding.btnSignUp.setOnClickListener {

            findNavController().navigate(R.id.action_signIn_to_createAcountFragment)
        }

        binding.btnSignIn.setOnClickListener {
            if(validate()){
                signIn(binding.textEmail.text.toString(),binding.etPassword.text.toString())
            }else{
                Toast.makeText(activity,
                    "Empty email or pawssword fields",
                    Toast.LENGTH_LONG).show()
            }

        }

        return binding.root

    }

    fun validate(): Boolean{
        return !TextUtils.isEmpty(binding.textEmail.text) && !TextUtils.isEmpty(binding.etPassword.text)
    }

    fun signIn(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithEmail:success")
                   // val user = auth.currentUser
                    val bundle: Bundle = bundleOf("user" to email)
                    findNavController().navigate(R.id.action_signIn_to_welcomeScreen2,bundle)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        activity,
                        "Authentication failed. Invalid Email or Password",
                        Toast.LENGTH_SHORT,
                    ).show()

                }
            }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}