package com.uberalles.dicodingstorysubmission.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.uberalles.dicodingstorysubmission.R
import com.uberalles.dicodingstorysubmission.customview.CustomEditText
import com.uberalles.dicodingstorysubmission.databinding.FragmentSignUpBinding
import com.uberalles.dicodingstorysubmission.ui.main.MainActivity
import com.uberalles.dicodingstorysubmission.utils.UserPrefs
import com.uberalles.dicodingstorysubmission.utils.ViewModelFactory
import com.uberalles.dicodingstorysubmission.utils.dataStore

class SignUp : Fragment() {

    private lateinit var viewModel: AuthViewModel

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = UserPrefs.getInstance((activity as AuthActivity).dataStore)
        viewModel = ViewModelProvider(this, ViewModelFactory(prefs))[AuthViewModel::class.java]

        validator()
        signedUp()
//        edError()
        userSignUp()
    }

    private fun validator() {
        val button = binding.btnSignUp
        val name = binding.edRegisterName
        val email= binding.edRegisterEmail
        val password = binding.edRegisterPassword

        //if name email password is valid then button is enabled, else button is disabled
        name.addTextChangedListener {
            button.isEnabled = name.isValid() && email.isValid() && password.isValid()
        }
        email.addTextChangedListener {
            button.isEnabled = name.isValid() && email.isValid() && password.isValid()
        }
        password.addTextChangedListener {
            button.isEnabled = name.isValid() && email.isValid() && password.isValid()
        }

        button.isEnabled = name.isValid() && email.isValid() && password.isValid()


    }



    private fun signedUp() {
        binding.apply {
            signedUp.setOnClickListener {
                findNavController().navigate(R.id.action_signUp_to_login)
            }
        }
    }

    private fun userSignUp() {
        binding.apply {
            btnSignUp.setOnClickListener { it ->
                val name = binding.edRegisterName.text.toString()
                val email = binding.edRegisterEmail.text.toString()
                val password = binding.edRegisterPassword.text.toString()

                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.putExtra(KEY_NAME, name)

                viewModel.signUp(requireContext(), name, email, password)
                viewModel.signUpResponse.observe(viewLifecycleOwner) { auth ->
                    if (auth != null) {
                        if (auth.error) {
                            Toast.makeText(
                                requireContext(),
                                "Failed to sign up!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Sign up successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            if(findNavController().currentDestination?.id == R.id.signUp)
                                findNavController().navigate(R.id.action_signUp_to_login)
                        }
                    }
                }
                viewModel.isLoading.observe(viewLifecycleOwner) {
                    showLoading(it)
                }
            }
        }
    }


    private fun showLoading(b: Boolean) {
        binding.loading.visibility = if (b) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val KEY_NAME = "name"
    }

}