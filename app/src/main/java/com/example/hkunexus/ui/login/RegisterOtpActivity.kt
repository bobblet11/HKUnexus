package com.example.hkunexus.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.hkunexus.MainActivity
import com.example.hkunexus.R


class RegisterOtpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_otp)

        val submitOTP = findViewById<Button>(R.id.submitOTP)
        val resendOTP = findViewById<Button>(R.id.resendOTP)
        val OTPcode = findViewById<EditText>(R.id.OtpCodeInput)

        submitOTP.setOnClickListener {
            val goToMain = Intent(this, MainActivity::class.java)
            startActivity(goToMain)
        }
    }
}