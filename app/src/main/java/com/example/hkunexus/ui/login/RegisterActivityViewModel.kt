package com.example.hkunexus.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.hkunexus.data.SupabaseSingleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.regex.Pattern

data class RegisterUiState(
    val isFirstNameValid: Boolean = true,
    val isLastNameValid: Boolean = true,
    val isEmailValid: Boolean = true,
    val isPasswordValid: Boolean = true,
    val isPasswordVerified: Boolean = true,
    val isUsernameValid: Boolean = true,
)


class RegisterActivityViewModel() : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private fun setValidationResult(isFirstNameValid: Boolean, isLastNameValid: Boolean, isEmailValid: Boolean, isPasswordValid : Boolean,isPasswordVerified: Boolean, isUsernameTaken : Boolean){
        _uiState.update {
            it.copy(
                isFirstNameValid = isFirstNameValid,
                isLastNameValid = isLastNameValid,
                isEmailValid = isEmailValid,
                isPasswordValid = isPasswordValid,
                isPasswordVerified = isPasswordVerified,
                isUsernameValid = isUsernameTaken)
        }
    }

    public fun attemptRegister(firstNameInput: String, lastNameInput: String, emailInput: String, passwordInput: String, verifiedPasswordInput: String, usernameInput : String): Boolean{
        if (!validateRegistration(firstNameInput, lastNameInput, emailInput, passwordInput, verifiedPasswordInput, usernameInput)){
            Log.d("RegisterActivityViewModel", "registration validation failed $firstNameInput, $lastNameInput, $emailInput, $passwordInput, $verifiedPasswordInput, $usernameInput")
            return false
        }

        if (!SupabaseSingleton.isEmailAvailable("$emailInput@connect.hku.hk")){
            Log.d("RegisterActivityViewModel", "email account is already taken")
            return false
        }

        if (!SupabaseSingleton.isUsernameAvailable(usernameInput)){
            Log.d("RegisterActivityViewModel", "username is already taken")
            return false
        }

        Log.d("RegisterActivityViewModel", "all registration information is valid and available")

        if (!SupabaseSingleton.register(firstNameInput, lastNameInput, "$emailInput@connect.hku.hk", passwordInput, usernameInput)){
            Log.d("RegisterActivityViewModel", "could not register with backend")
            return false
        }

        Log.d("RegisterActivityViewModel", "registration success")
        return true
    }


    private fun validateRegistration(firstNameInput: String, lastNameInput: String, emailInput: String, passwordInput: String, verifiedPasswordInput: String, usernameInput : String): Boolean {

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
        )

        val isPasswordValid = passwordREGEX.matcher(passwordInput).matches()
        val isPasswordVerified = passwordInput == verifiedPasswordInput

        val emailREGEX = Pattern.compile(
            "^" +                // Start of the string
                    "(?!.*\\s)" +       // No whitespace
                    "(?!.*@)" +         // No @ character
                    "[\\S]+" +          // One or more non-whitespace characters
                    "$"                 // End of the string
        )
        val isEmailValid = emailREGEX.matcher(emailInput).matches()

        val usernameREGEX = Pattern.compile(
            "^" +                // Start of the string
                    "(?!.*\\s)" +       // No whitespace
                    "(?!.*@)" +         // No @ character
                    "[\\S]+" +          // One or more non-whitespace characters
                    "$"                 // End of the string
        )
        val isUsernameValid = usernameREGEX.matcher(usernameInput).matches()
        val isFirstNameValid = usernameREGEX.matcher(firstNameInput).matches()
        val isLastNameValid = usernameREGEX.matcher(lastNameInput).matches()

        Log.d("RegisterActivityViewModel", "$isFirstNameValid, $isLastNameValid, $isEmailValid, $isPasswordValid, $isPasswordVerified, $isUsernameValid")
        setValidationResult(isFirstNameValid, isLastNameValid, isEmailValid, isPasswordValid, isPasswordVerified, isUsernameValid)
        return (isFirstNameValid && isLastNameValid && isEmailValid && isPasswordValid && isUsernameValid && isPasswordVerified)
    }

}