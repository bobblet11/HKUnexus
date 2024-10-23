package com.example.hkunexus.ui.login

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import com.example.hkunexus.MainActivity
import com.example.hkunexus.data.SupabaseSingleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.regex.Pattern

data class LoginUiState(
    //when you login and use invalid email/password, highlight the input fields that are invalid.
    //wont send to supabase if invalid.
    val isEmailValid: Boolean = true,
    val isPasswordValid: Boolean = true,
)


class LoginActivityViewModel() : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private fun setValidationResult(isEmailValid: Boolean, isPasswordValid : Boolean){
        _uiState.update {
            it.copy(isEmailValid = isEmailValid,
                isPasswordValid = isPasswordValid)
        }
    }

    public fun authenticateLogin(emailInput: String, passwordInput: String): Boolean{
        return !SupabaseSingleton.login(emailInput, passwordInput)
    }

    public fun validateLogin(emailInput: String, passwordInput: String): Boolean {

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

        val isPasswordValid = passwordREGEX.matcher(passwordInput).matches()

        val emailREGEX = Pattern.compile(
            "^" +                // Start of the string
                    "(?!.*\\s)" +       // No whitespace
                    "(?!.*@)" +         // No @ character
                    "[\\S]+" +          // One or more non-whitespace characters
                    "$"                 // End of the string
        );

        val isEmailValid = emailREGEX.matcher(emailInput).matches()

        setValidationResult(isEmailValid, isPasswordValid)
        return (isEmailValid && isPasswordValid)
    }

}