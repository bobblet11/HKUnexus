package com.example.hkunexus.ui.login

import android.R.attr.password
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.hkunexus.MainActivity
import com.example.hkunexus.R
import com.example.hkunexus.data.SupabaseSingleton
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.runBlocking
import java.util.regex.Matcher
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //ALL OF THIS SHOULD BE IN VIEW MODEL!!!!!!!!!
        setContentView(R.layout.activity_login)


        val email = findViewById<EditText>(R.id.loginEmail)
        val password = findViewById<EditText>(R.id.loginPassword)


        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener {
            val goToRegister = Intent(this, RegisterActivity::class.java)
            startActivity(goToRegister)
        }

        loginButton.setOnClickListener {
            val emailInput = email.text.toString() + "@connect.hku.hk"
            val passwordInput = password.text.toString()

            if (!validateLogin(emailInput, passwordInput)){
                // Break out of the listener
                Log.d("LoginActivity", "login validation failed$emailInput, $passwordInput")
                return@setOnClickListener
            }

            if (!SupabaseSingleton.login(emailInput, passwordInput)){
                Log.d("LoginActivity", "login authentication failed")
                return@setOnClickListener
            }

            val accessToken: String = SupabaseSingleton.getAccessToken()

            if (accessToken.isEmpty()){
                Log.d("LoginActivity", "access token is invalid")
                return@setOnClickListener
            }

            Log.d("LoginActivity", "successfully logged in")
            val goToMain = Intent(this, MainActivity::class.java);
            startActivity(goToMain);

        }

    }
    //ALL OF THIS SHOULD BE IN VIEW MODEL!!!!!!!!!
    fun validateLogin(emailInput: String?, passwordInput: String?): Boolean {
        return true
        val notEmpty =
            !(emailInput == null || passwordInput == null || emailInput.isEmpty() || passwordInput.isEmpty())

        val passwordREGEX = Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$"
        );
        val validPassword = passwordREGEX.matcher(passwordInput).matches()

        val emailREGEX = Pattern.compile(
            "^" +
                    "(?!.*@)" +             //no @
                    "[a-zA-Z0-9._%+-]" +    //all validChars
                    "+$"
        );

        val validEmail = emailREGEX.matcher(emailInput).matches()

        return (notEmpty && validEmail && validPassword)
    }
}
