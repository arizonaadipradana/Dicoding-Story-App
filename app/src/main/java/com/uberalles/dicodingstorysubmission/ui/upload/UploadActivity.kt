package com.uberalles.dicodingstorysubmission.ui.upload

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.uberalles.dicodingstorysubmission.databinding.ActivityUploadBinding
import com.uberalles.dicodingstorysubmission.ui.main.MainActivity
import java.io.File

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private lateinit var photoPath: String
    private val viewModel by viewModels<UploadViewModel>()

    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        takePhoto()
        takeGallery()
        uploadPhoto()
    }

    private fun takePhoto() {
        binding.btnCamera.setOnClickListener {
            startTakePhoto()
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoUri: Uri = FileProvider.getUriForFile(
                this@UploadActivity,
                "com.uberalles.dicodingstorysubmission.ui.upload",
                it
            )
            photoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(photoPath)

            myFile.let { file ->
                getFile = file
                binding.photoResultCard.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private fun takeGallery() {
        binding.btnGallery.setOnClickListener {
            choosePicture()
        }
    }

    private fun choosePicture() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a picture!")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val selectedPhoto = it.data?.data as Uri
                selectedPhoto.let { uri ->
                    val myFile = uriToFile(uri, this@UploadActivity)
                    getFile = myFile
                    binding.photoResultCard.setImageURI(uri)
                }
            }
        }


    private fun uploadPhoto() {
        binding.btnUpload.setOnClickListener {
            val description = binding.descriptionTextInput.text.toString()
            if (getFile != null && description.isNotEmpty()) {
                val file = reduceFileImage(getFile as File)
                viewModel.upload(applicationContext, file, description)
            } else if (description.isEmpty()) {
                Toast.makeText(this, "Please fill the description", Toast.LENGTH_SHORT).show()
            } else Toast.makeText(this, "Please choose a photo", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
        }

        viewModel.uploadResponse.observe(this) { authentication ->
            if (authentication != null) {
                showLoading(false)
                Toast.makeText(this, "Upload Success", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@UploadActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else {
                showLoading(true)
                Toast.makeText(this, "Upload Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}