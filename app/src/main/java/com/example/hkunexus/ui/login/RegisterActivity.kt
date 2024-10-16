package com.example.hkunexus.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.hkunexus.R


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val OtpButton = findViewById<Button>(R.id.goToOTP)

        val firstName = findViewById<EditText>(R.id.firstname)
        val lastname = findViewById<EditText>(R.id.lastname)
        val username = findViewById<EditText>(R.id.registerUsername)
        val email = findViewById<EditText>(R.id.registrationEmail)
        val password = findViewById<EditText>(R.id.registrationPassword)
        val retryPassword = findViewById<EditText>(R.id.registerVerifyPassword)



        OtpButton.setOnClickListener {
            val goToOTP = Intent(this, RegisterOtpActivity::class.java)
            startActivity(goToOTP)
        }
    }
}