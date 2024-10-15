package com.example.hkunexus.ui.login
import android.content.Intent
import android.os.Bundle

import com.example.hkunexus.R
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigInteger
import java.util.UUID


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email = findViewById<EditText>(R.id.loginEmail)
        val password = findViewById<EditText>(R.id.loginPassword)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.loginRegisterButton)

        loginButton.setOnClickListener {
            val emailInput = email.text.toString()
            val passwordInput = password.text.toString()

            if (validateLogin(emailInput, passwordInput)) {
                val switchToLogin = Intent(this, MainActivity::class.java)
                startActivity(switchToLogin)
            }

            //do something like email missing, password missing
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
