package com.example.hkunexus.ui.login

import android.R.attr.password
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.hkunexus.MainActivity
import com.example.hkunexus.R
import com.example.hkunexus.data.SupabaseSingleton
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.regex.Matcher
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        val email = findViewById<EditText>(R.id.loginEmail)
        val password = findViewById<EditText>(R.id.loginPassword)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)


        CoroutineScope(Dispatchers.Main).launch {
            viewModel.uiState.collect { validationState ->
                updateBorderColour(email, validationState.isEmailValid)
                updateBorderColour(password, validationState.isPasswordValid)
            }
        }

        registerButton.setOnClickListener {
            val goToRegister = Intent(this, RegisterActivity::class.java)
            startActivity(goToRegister)
        }

        loginButton.setOnClickListener {
            val emailInput = email.text.toString() + "@connect.hku.hk"
            val passwordInput = password.text.toString()

            if (!viewModel.validateLogin(emailInput, passwordInput)){
                Log.d("LoginActivity", "login validation failed $emailInput, $passwordInput")
                return@setOnClickListener
            }

            if (!viewModel.authenticateLogin(emailInput, passwordInput)){
                Log.d("LoginActivity", "login authentication failed")
                return@setOnClickListener
            }

            Log.d("LoginActivity", "successfully logged in")
            val goToMain = Intent(this, MainActivity::class.java);
            startActivity(goToMain);

        }

    }

    private fun updateBorderColour(border: EditText, isValid:Boolean){
        val invalidBorder = ContextCompat.getDrawable(this, R.drawable.edit_text_border_red)
        val validBorder = ContextCompat.getDrawable(this, R.drawable.edit_text_border_normal)
        border.background = if(isValid) validBorder else invalidBorder
    }
}
