package com.uberalles.dicodingstorysubmission.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.uberalles.dicodingstorysubmission.R
import com.uberalles.dicodingstorysubmission.utils.Preferences

class SplashFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = Preferences.initPref(requireContext(), "onSignIn")
        val token = sharedPreferences.getString("token", "")

        if (token.isNullOrEmpty()) {
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_splashFragment_to_login)
            }, DURATION)
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_splashFragment_to_mainActivity)
            }, DURATION)
        }
    }

    companion object {
        private const val DURATION: Long = 3000
    }

}