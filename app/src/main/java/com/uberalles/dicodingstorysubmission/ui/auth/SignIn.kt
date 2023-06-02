package com.uberalles.dicodingstorysubmission.ui.auth

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.uberalles.dicodingstorysubmission.R
import com.uberalles.dicodingstorysubmission.databinding.FragmentLoginBinding
import com.uberalles.dicodingstorysubmission.response.SignInResponse
import com.uberalles.dicodingstorysubmission.utils.Preferences
import com.uberalles.dicodingstorysubmission.utils.UserPrefs
import com.uberalles.dicodingstorysubmission.utils.UserPrefs.Companion.preferenceDefaultValue
import com.uberalles.dicodingstorysubmission.utils.ViewModelFactory
import com.uberalles.dicodingstorysubmission.utils.dataStore

class SignIn : Fragment() {

    private lateinit var viewModel: AuthViewModel

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = UserPrefs.getInstance((activity as AuthActivity).dataStore)
        viewModel = ViewModelProvider(this, ViewModelFactory(pref))[AuthViewModel::class.java]


        signUp()
        signIn()
    }

    private fun signIn() {
        binding.apply {
            btnSignIn.setOnClickListener {
                val email = binding.edLoginEmail.text.toString()
                val password = binding.edLoginPassword.text.toString()

                val imm =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(it.windowToken, 0)

                viewModel.signIn(requireContext(), email, password)

                viewModel.getUserPreferences("Token").observe(viewLifecycleOwner) { token ->
                    Log.e("AuthViewModel", "Token changed: $token")
                    if (token != preferenceDefaultValue) {
                        var alertDialog: AlertDialog? = null
                        alertDialog = AlertDialog.Builder(requireContext()).apply {
                            setTitle("Login Successfully")
                            setMessage("Press OK to continue")
                            setPositiveButton("OK") { _, _ ->
                                alertDialog?.dismiss()

                                Preferences.saveToken(token, requireContext())
                                findNavController().navigate(R.id.action_login_to_mainActivity)
                                requireActivity().finish()
                            }
                            create()
                        }.show()
                    }
                }

            }
            viewModel.isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }
        }
    }

    private fun processSignIn(data: SignInResponse) {
        if (data.error) {
            Toast.makeText(requireContext(), data.message, Toast.LENGTH_SHORT).show()
        } else {
            Preferences.saveToken(data.loginResult.token, requireContext())
            findNavController().navigate(R.id.action_login_to_mainActivity)
            requireActivity().finish()
        }
    }

    private fun signUp() {
        binding.apply {
            notSignedUp.setOnClickListener {
                findNavController().navigate(R.id.action_login_to_signUp)
            }
        }
    }

    private fun showLoading(b: Boolean) {
        binding.loading.visibility = if (b) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}