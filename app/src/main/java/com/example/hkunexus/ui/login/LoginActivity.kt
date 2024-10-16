package com.example.hkunexus.ui.login
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.hkunexus.MainActivity
import com.example.hkunexus.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email = findViewById<EditText>(R.id.loginEmail)
        val password = findViewById<EditText>(R.id.loginPassword)

        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)

        loginButton.setOnClickListener {
            val emailInput = email.text.toString()
            val passwordInput = password.text.toString()

            if (validateLogin(emailInput, passwordInput)) {
                val goToMain = Intent(this, MainActivity::class.java)
                startActivity(goToMain)
            }
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
