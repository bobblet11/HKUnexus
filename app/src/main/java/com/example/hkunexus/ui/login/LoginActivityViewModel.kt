package com.example.hkunexus.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.hkunexus.data.SupabaseSingleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.regex.Pattern

data class LoginUiState(
    //when you login_register and use invalid email/password, highlight the input fields that are invalid.
    //wont send to supabase if invalid.
    val isEmailValid: Boolean = true,
    val isPasswordValid: Boolean = true,
)


class LoginActivityViewModel() : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()


    fun attemptLogin(emailInput: String, passwordInput: String): Boolean{
        if (!validateLogin(emailInput, passwordInput)){
            Log.d("LoginActivityViewModel", "login_register validation failed $emailInput, $passwordInput")
            return false
        }

        if (!authenticateLogin("$emailInput@connect.hku.hk", passwordInput)){
            Log.d("LoginActivityViewModel", "login_register authentication failed")
            return false
        }
        Log.d("LoginActivityViewModel", "login_register success")
        return true
    }

    private fun setValidationResult(isEmailValid: Boolean, isPasswordValid : Boolean){
        _uiState.update {
            it.copy(isEmailValid = isEmailValid,
                isPasswordValid = isPasswordValid)
        }
    }

    private fun authenticateLogin(emailInput: String, passwordInput: String): Boolean{
        return SupabaseSingleton.login(emailInput, passwordInput)
    }

    private fun validateLogin(emailInput: String, passwordInput: String): Boolean {

        val passwordREGEX = Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[^A-Za-z0-9])" +  //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$"
        )

        val isPasswordValid = passwordREGEX.matcher(passwordInput).matches()

        val emailREGEX = Pattern.compile(
            "^" +                // Start of the string
                    "(?!.*\\s)" +       // No whitespace
                    "(?!.*@)" +         // No @ character
                    "[\\S]+" +          // One or more non-whitespace characters
                    "$"                 // End of the string
        )

        val isEmailValid = emailREGEX.matcher(emailInput).matches()

        setValidationResult(isEmailValid, isPasswordValid)
        return (isEmailValid && isPasswordValid)
    }

}