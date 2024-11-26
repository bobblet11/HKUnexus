package com.example.hkunexus.ui.login

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.hkunexus.data.RegexRule
import com.example.hkunexus.data.SupabaseSingleton
import com.example.hkunexus.data.model.dto.ClubDto
import com.example.hkunexus.data.model.dto.EventDto
import com.example.hkunexus.ui.homePages.create.BUCKET_URL_PREFIX
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import java.util.UUID
import java.util.regex.Pattern


data class RegisterUiState(
    val isFirstNameValid: Boolean = true,
    val isLastNameValid: Boolean = true,
    val isEmailValid: Boolean = true,
    val isPasswordValid: Boolean = true,
    val isPasswordVerified: Boolean = true,
    val isUsernameValid: Boolean = true,

    val postImage: Uri? = null
)



class RegisterActivityViewModel() : ViewModel() {
    var imageFile: File? = File.createTempFile("lol","jpg");
    val BUCKET_URL_PREFIX = "https://ctiaasznssbnyizmglhv.supabase.co/storage/v1/object/public/"

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

    fun attemptRegister(firstNameInput: String, lastNameInput: String, emailInput: String, passwordInput: String, verifiedPasswordInput: String, usernameInput : String): Boolean{
        if (!validateRegistration(firstNameInput, lastNameInput, emailInput, passwordInput, verifiedPasswordInput, usernameInput)){
            Log.d("RegisterActivityViewModel", "registration validation failed $firstNameInput, $lastNameInput, $emailInput, $passwordInput, $verifiedPasswordInput, $usernameInput")
            return false
        }

        if (uiState.value.postImage == null){
            return false
        }

        if (!SupabaseSingleton.isEmailAvailable(emailInput)){
            Log.d("RegisterActivityViewModel", "email account is already taken")
            return false
        }

        if (!SupabaseSingleton.isDisplayNameAvailable(usernameInput)){
            Log.d("RegisterActivityViewModel", "username is already taken")
            return false
        }

        Log.d("RegisterActivityViewModel", "all registration information is valid and available")

        val postIdArg = UUID.randomUUID().toString()
        var mediaArg = ""
        if (hasPostImage()){
            try {
                Log.d("POST", imageFile.toString())
                val result = SupabaseSingleton.uploadImageToBucket(
                    imageFile!!,
                    "user_profiles",
                    filepathArg = "images/attachment_$postIdArg.jpg",
                    register = true
                )
                Log.d("POST", result.toString())
                mediaArg = (BUCKET_URL_PREFIX + result) ?: "";

            } catch (ex : Exception){
                Log.e("POST", ex.stackTraceToString())

            }

        }

        if (!SupabaseSingleton.register(firstNameInput, lastNameInput, "$emailInput@connect.hku.hk", passwordInput, usernameInput, mediaArg)){
            Log.d("RegisterActivityViewModel", "could not register with backend")
            return false
        }

        Log.d("RegisterActivityViewModel", "registration success")
        return true
    }

    fun setPostImage(uri: Uri?) {
        _uiState.update {
            it.copy(
                postImage = uri
            )
        }
    }

    fun hasPostImage(): Boolean {
        return uiState.value.postImage != null
    }

    private fun validateRegistration(firstNameInput: String, lastNameInput: String, emailInput: String, passwordInput: String, verifiedPasswordInput: String, usernameInput : String): Boolean {
        val isPasswordValid = RegexRule.passwordRegex.matcher(passwordInput).matches()
        val isPasswordVerified = passwordInput == verifiedPasswordInput

        val isEmailValid = RegexRule.emailRegex.matcher(emailInput).matches()

        val usernameRegex = RegexRule.usernameRegex

        val isUsernameValid = usernameRegex.matcher(usernameInput).matches()
        val isFirstNameValid = usernameRegex.matcher(firstNameInput).matches()
        val isLastNameValid = usernameRegex.matcher(lastNameInput).matches()

        Log.d("RegisterActivityViewModel", "$isFirstNameValid, $isLastNameValid, $isEmailValid, $isPasswordValid, $isPasswordVerified, $isUsernameValid")
        setValidationResult(isFirstNameValid, isLastNameValid, isEmailValid, isPasswordValid, isPasswordVerified, isUsernameValid)
        return (isFirstNameValid && isLastNameValid && isEmailValid && isPasswordValid && isUsernameValid && isPasswordVerified)
    }

}