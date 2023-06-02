package com.uberalles.dicodingstorysubmission.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.uberalles.dicodingstorysubmission.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra(EXTRA_NAME)
        val photoUrl = intent.getStringExtra(EXTRA_PHOTO_URL)
        val description = intent.getStringExtra(EXTRA_DESCRIPTION)

        binding.name.text = name
        Glide.with(this)
            .load(photoUrl)
            .skipMemoryCache(true)
            .into(binding.photoUrl)
        binding.description.text = description
    }

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_PHOTO_URL = "extra_photo"
        const val EXTRA_DESCRIPTION = "extra_description"
    }
}