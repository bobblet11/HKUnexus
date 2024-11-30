package com.example.hkunexus.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.hkunexus.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RegisterActivity : AppCompatActivity() {

    private val viewModel: RegisterActivityViewModel by viewModels()


    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
            viewModel.setPostImage(uri)
            val inputStream = contentResolver.openInputStream(uri)
            inputStream.use{
                    input ->
                viewModel.imageFile!!.outputStream().use{
                        output ->
                    input!!.copyTo(output)
                }
            }

            updateBannerPhoto()
            inputStream?.close()

            Log.d("PhotoPicker", viewModel.imageFile.toString())
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    private fun updateBannerPhoto() {
        findViewById<ImageView>(R.id.post_image).setImageURI(
            viewModel.uiState.value.postImage
        )

        if (viewModel.hasPostImage()) {
            findViewById<ImageView>(R.id.post_image).visibility = View.VISIBLE
            findViewById<ImageView>(R.id.remove_post_image).visibility = View.VISIBLE
        } else {
            findViewById<ImageView>(R.id.post_image).visibility = View.GONE
            findViewById<ImageView>(R.id.remove_post_image).visibility = View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val otpButton = findViewById<Button>(R.id.goToOTP)
        val firstname = findViewById<EditText>(R.id.firstname)
        val lastname = findViewById<EditText>(R.id.lastname)
        val displayName = findViewById<EditText>(R.id.registerUsername)
        val email = findViewById<EditText>(R.id.registrationEmail)
        val password = findViewById<EditText>(R.id.registrationPassword)
        val verifiedPassword = findViewById<EditText>(R.id.registerVerifyPassword)

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.uiState.collect { uiState ->
                updateBorderColour(firstname, uiState.isFirstNameValid)
                updateBorderColour(lastname, uiState.isLastNameValid)
                updateBorderColour(displayName, uiState.isUsernameValid)
                updateBorderColour(email, uiState.isEmailValid)
                updateBorderColour(password, uiState.isPasswordValid)
                updateBorderColour(verifiedPassword, uiState.isPasswordVerified && uiState.isPasswordValid)
            }
        }

        otpButton.setOnClickListener {
            val firstNameInput = firstname.text.toString()
            val lastNameInput = lastname.text.toString()
            val displayNameInput = displayName.text.toString()
            val emailInput = email.text.toString()
            val passwordInput = password.text.toString()
            val verifiedPasswordInput = verifiedPassword.text.toString()

            if(viewModel.attemptRegister(firstNameInput, lastNameInput, emailInput, passwordInput, verifiedPasswordInput, displayNameInput)){
                val goToOTP = Intent(this, RegisterOtpActivity::class.java)
                val bundle = Bundle()
                bundle.putString("email", "$emailInput@connect.hku.hk")
                goToOTP.putExtras(bundle)
                startActivity(goToOTP)
            }
        }

        findViewById<Button>(R.id.upload_post_image).setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        findViewById<ImageView>(R.id.remove_post_image).setOnClickListener {
            viewModel.setPostImage(null)
            updateBannerPhoto()
        }

        updateBannerPhoto()
    }

    private fun updateBorderColour(border: EditText, isValid:Boolean){
        val invalidBorder = ContextCompat.getDrawable(this, R.drawable.edit_text_border_red)
        val validBorder = ContextCompat.getDrawable(this, R.drawable.edit_text_border_normal)
        border.background = if(isValid) validBorder else invalidBorder
    }


}