package com.example.hkunexus.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hkunexus.data.RegexRule
import com.example.hkunexus.data.SupabaseSingleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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


    fun attemptLogin(emailInput: String, passwordInput: String, callback: (Boolean)->Unit): Boolean{
        viewModelScope.launch {
            //val isSuccess = validateLogin(emailInput, passwordInput) && SupabaseSingleton.login("$emailInput@connect.hku.hk", passwordInput)
            val isSuccess = SupabaseSingleton.login("$emailInput@connect.hku.hk", passwordInput)
            callback(isSuccess)

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

//    private fun validateLogin(emailInput: String, passwordInput: String): Boolean {
//        val isPasswordValid = RegexRule.passwordRegex.matcher(passwordInput).matches()
//        val isEmailValid = RegexRule.emailRegex.matcher(emailInput).matches()
//
//        setValidationResult(isEmailValid, isPasswordValid)
//        return (isEmailValid && isPasswordValid)
//    }

}