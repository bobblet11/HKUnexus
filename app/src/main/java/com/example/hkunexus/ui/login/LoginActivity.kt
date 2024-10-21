package com.example.hkunexus.ui.login
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.hkunexus.MainActivity

import com.example.hkunexus.R
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigInteger
import java.util.UUID


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

        loginButton.setOnClickListener { runBlocking {
            val emailInput = email.text.toString()
            val passwordInput = password.text.toString()

//            if (validateLogin(emailInput, passwordInput)) {
//                val goToMain = Intent(this, MainActivity::class.java)
//                startActivity(goToMain)
//            }
            try {
                val result = supabase.auth.signInWith(Email) {
                    this.email = emailInput;
                    this.password = passwordInput;
                }
                val user = supabase.auth.retrieveUserForCurrentSession(updateSession = true)
                val session = supabase.auth.currentSessionOrNull()

                Log.d("MainActivity", "Sign-in successful: $result")

                val myIntent = Intent(loginActivity, MainActivity::class.java);
                myIntent.putExtra("AccessToken", session?.accessToken);
                startActivity(myIntent);


            } catch (e: Exception) {
                Log.e("MainActivity", "Sign-in failed", e)
            }

        }}


        registerButton.setOnClickListener {
            val goToRegister = Intent(this, RegisterActivity::class.java)
            startActivity(goToRegister)
        }
    }

    fun validateLogin(emailInput: String?, passwordInput: String?): Boolean{
        if (emailInput == null || passwordInput == null || emailInput.length <=0 || passwordInput.length <=0){
            return false
            //probs a good idea to do some better validation, like email checking etc
        }
        return true
    }


}
