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

    val supabase = createSupabaseClient(
        supabaseUrl = "https://ctiaasznssbnyizmglhv.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImN0aWFhc3puc3Nibnlpem1nbGh2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3Mjg1NDQ3NzEsImV4cCI6MjA0NDEyMDc3MX0.t0-AHECeFc0PWItTVJ-X0BGGclh_LEbFhFOtBi9rNd4"
    ) {
        install(Postgrest)
        install(Auth)
    }
    @Serializable
    data class DemoRowDto(
        @SerialName("id")
        val id: String,
        @SerialName("created_at")
        val created_at: String,
        @SerialName("vibes")
        val vibes: Int,
    )
    data class DemoRow(
        val id: String,
        val created_at: String,
        val vibes: Int,
    )
    suspend fun loginplz(){
        try {
            val result = supabase.auth.signInWith(Email) {
                email = "happyeenoddy@gmail.com"
                password = "123456"
            }
            Log.d("MainActivity", "Sign-in successful: $result")
        } catch (e: Exception) {
            Log.e("MainActivity", "Sign-in failed", e)
        }
        val user = supabase.auth.retrieveUserForCurrentSession(updateSession = true)
        val session = supabase.auth.currentSessionOrNull()
        Log.d("LoginTest", user.toString())
        Log.d("LoginTest", session.toString())
        val d1 = supabase.from("demo1").select().decodeSingle<DemoRowDto>()
        Log.d("DBTest",d1.toString())
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
                supabase.auth.signInWith(Email) {
                    this.email = emailInput;
                    this.password = passwordInput;
                }

                val goToMain = Intent(this, MainActivity::class.java)

                Log.d("MainActivity", "Sign-in successful: $result")
            } catch (e: Exception) {
                Log.e("MainActivity", "Sign-in failed", e)
            }

        }}

        lifecycleScope.launch {
            loginplz()
            //do something like email missing, password missing
        }

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
