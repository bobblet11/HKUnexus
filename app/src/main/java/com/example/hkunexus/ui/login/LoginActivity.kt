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
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.runBlocking
import java.util.regex.Matcher
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity() {

    val loginActivity = this;

    val supabase = createSupabaseClient(
        supabaseUrl = "https://ctiaasznssbnyizmglhv.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImN0aWFhc3puc3Nibnlpem1nbGh2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3Mjg1NDQ3NzEsImV4cCI6MjA0NDEyMDc3MX0.t0-AHECeFc0PWItTVJ-X0BGGclh_LEbFhFOtBi9rNd4"
    ) {
        install(Postgrest)
        install(Auth)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            val emailInput = email.text.toString()
            val passwordInput = password.text.toString()

            if (!validateLogin(emailInput, passwordInput)){
                // Break out of the listener
                return@setOnClickListener
            }

            runBlocking {
                try {
                    val result = supabase.auth.signInWith(Email) {
                        this.email = emailInput;
                        this.password = passwordInput;
                    }
                    val user = supabase.auth.retrieveUserForCurrentSession(updateSession = true)
                    val session = supabase.auth.currentSessionOrNull()

                    Log.d("MainActivity", "Sign-in successful: $result")

                    val goToMain = Intent(loginActivity, MainActivity::class.java);
                    goToMain.putExtra("AccessToken", session?.accessToken);
                    startActivity(goToMain);

                } catch (e: Exception) {
                    Log.e("MainActivity", "Sign-in failed", e)
                }
            }
        }

    }

    fun validateLogin(emailInput: String?, passwordInput: String?): Boolean {
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
