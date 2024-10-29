package com.example.hkunexus.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.hkunexus.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RegisterActivity : AppCompatActivity() {

    private val viewModel: RegisterActivityViewModel by viewModels()

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
                startActivity(goToOTP)
            }
        }
    }

    private fun updateBorderColour(border: EditText, isValid:Boolean){
        val invalidBorder = ContextCompat.getDrawable(this, R.drawable.edit_text_border_red)
        val validBorder = ContextCompat.getDrawable(this, R.drawable.edit_text_border_normal)
        border.background = if(isValid) validBorder else invalidBorder
    }
}