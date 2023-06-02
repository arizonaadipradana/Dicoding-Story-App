package com.uberalles.dicodingstorysubmission.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.uberalles.dicodingstorysubmission.R
import com.uberalles.dicodingstorysubmission.adapter.StoriesAdapter
import com.uberalles.dicodingstorysubmission.databinding.ActivityMainBinding
import com.uberalles.dicodingstorysubmission.ui.auth.AuthActivity
import com.uberalles.dicodingstorysubmission.ui.auth.AuthViewModel
import com.uberalles.dicodingstorysubmission.ui.upload.UploadActivity
import com.uberalles.dicodingstorysubmission.utils.Preferences
import com.uberalles.dicodingstorysubmission.utils.UserPrefs
import com.uberalles.dicodingstorysubmission.utils.ViewModelFactory
import com.uberalles.dicodingstorysubmission.utils.dataStore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var alertBuilder: AlertDialog.Builder
    private lateinit var adapter: StoriesAdapter
    private val viewModel: StoriesViewModel by viewModels {
        StoriesViewModelFactory(this)
    }
    private lateinit var authViewModel: AuthViewModel

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_open_anim
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_close_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.to_bottom_animation
        )
    }

    private var clicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserPrefs.getInstance(this.dataStore)
        authViewModel = ViewModelProvider(this, ViewModelFactory(pref))[AuthViewModel::class.java]

        getStories()
        buttonAnimation()
    }

    private fun getStories() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager = layoutManager

        adapter = StoriesAdapter()
        binding.rvStories.adapter = adapter


        viewModel.story.observe(this) { paging ->
            adapter.submitData(lifecycle, paging)
        }
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStories.addItemDecoration(itemDecoration)

//        binding.rvStories.layoutManager =
//            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    }


    private fun buttonAnimation() {
        binding.actionPopup.setOnClickListener {
            onAddButtonClicked()
        }
        binding.actionCamera.setOnClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }
        binding.actionLogout.setOnClickListener {
            dialogLogOut()
        }
    }

    private fun dialogLogOut() {
        alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setTitle("Alert")
            .setMessage("Do you want to log out?")
            .setCancelable(true)
            .setPositiveButton("Yes") { _, _ ->
                authViewModel.clearUserPreferences()
                Preferences.logOut(this)
                val intent = Intent(this, AuthActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
//                Preferences.logOut(this)
//                val intent = Intent(this, AuthActivity::class.java)
//                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//                startActivity(intent)
//                finish()
            }
            .setNegativeButton("No") { dialogInterface, it ->
                dialogInterface.cancel()
            }
            .show()
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            binding.actionCamera.visibility = View.VISIBLE
            binding.actionLogout.visibility = View.VISIBLE
        } else {
            binding.actionCamera.visibility = View.INVISIBLE
            binding.actionLogout.visibility = View.INVISIBLE
        }
    }

    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            binding.actionPopup.startAnimation(rotateOpen)
            binding.actionCamera.startAnimation(fromBottom)
            binding.actionLogout.startAnimation(fromBottom)
        } else {
            binding.actionPopup.startAnimation(rotateClose)
            binding.actionCamera.startAnimation(toBottom)
            binding.actionLogout.startAnimation(toBottom)
        }
    }


    override fun onBackPressed() {
        //show dialog to exit
        alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setTitle("Alert")
            .setMessage("Do you want to exit?")
            .setCancelable(true)
            .setPositiveButton("Yes") { _, _ ->
                finish()
                onDestroy()
            }
            .setNegativeButton("No") { dialogInterface, it ->
                dialogInterface.cancel()
            }
            .show()
    }
}