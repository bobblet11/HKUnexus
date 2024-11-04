package com.example.hkunexus.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.hkunexus.MainActivity
import com.example.hkunexus.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        val email = findViewById<EditText>(R.id.loginEmail)
        val password = findViewById<EditText>(R.id.loginPassword)

        val sharedPreference = getSharedPreferences("Logins", Context.MODE_PRIVATE)
        email.setText(sharedPreference.getString("email", ""))
        password.setText(sharedPreference.getString("password", ""))

        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)


        CoroutineScope(Dispatchers.Main).launch {
            viewModel.uiState.collect { uiState ->
                updateBorderColour(email, uiState.isEmailValid)
                updateBorderColour(password, uiState.isPasswordValid)
            }
        }

        registerButton.setOnClickListener {
            val goToRegister = Intent(this, RegisterActivity::class.java)
            startActivity(goToRegister)
        }

        loginButton.setOnClickListener {
            val emailInput = email.text.toString()
            val passwordInput = password.text.toString()

            if (viewModel.attemptLogin(emailInput, passwordInput)){
                val editor = sharedPreference.edit()
                editor.putString("email", emailInput)
                editor.putString("password", passwordInput)
                editor.apply()

                val goToMain = Intent(this, MainActivity::class.java);
                startActivity(goToMain);
            }

        }

    }

    private fun updateBorderColour(border: EditText, isValid:Boolean){
        val invalidBorder = ContextCompat.getDrawable(this, R.drawable.edit_text_border_red)
        val validBorder = ContextCompat.getDrawable(this, R.drawable.edit_text_border_normal)
        border.background = if(isValid) validBorder else invalidBorder
    }
}
