package com.example.hkunexus.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.hkunexus.data.SupabaseSingleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.regex.Pattern

data class RegisterOTPUiState(
    val noOfAttempts: Int = 0,
    val isOTPValid: Boolean = false,
)


class RegisterOTPActivityViewModel() : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterOTPUiState())
    val uiState: StateFlow<RegisterOTPUiState> = _uiState.asStateFlow()

    public fun attemptOTPAuthentication(otpInput: String, email:String): Boolean{

        if(!validateOTP(otpInput)){
            Log.d("Supabase Singleton","failed to validate OTP")
            return false
        }

        if(!SupabaseSingleton.authenticateOtp(otpInput, email)){
            Log.d("Supabase Singleton","failed to authenticate OTP")
            return false
        }

        return true
    }

    private fun validateOTP(otpInput: String): Boolean{
        return true
        val otpREGEX = Pattern.compile(
            "^" +
                    "\\d{6}" +  //is 6 digits
                    "$"
        )

        val isOtpValid = otpREGEX.matcher(otpInput).matches()
        return isOtpValid
    }


}