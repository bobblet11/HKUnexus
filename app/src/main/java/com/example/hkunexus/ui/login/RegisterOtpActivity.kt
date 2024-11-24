package com.example.hkunexus.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.hkunexus.MainActivity
import com.example.hkunexus.R


class RegisterOtpActivity : AppCompatActivity() {

    private val viewModel: RegisterOTPActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_otp)

        val submitOTP = findViewById<Button>(R.id.submitOTP)
        val resendOTP = findViewById<Button>(R.id.resendOTP)
        val OTPcode = findViewById<EditText>(R.id.OtpCodeInput)
        val bundle:Bundle? = getIntent().getExtras();
        var email : String? = null
        if (bundle != null) {
            email = bundle.getString("email")
        }
        submitOTP.setOnClickListener {

            if(viewModel.attemptOTPAuthentication(OTPcode.text.toString(),email.toString()) ){
                val goToLogin = Intent(this, LoginActivity::class.java)
                startActivity(goToLogin)
            }
        }
    }
}