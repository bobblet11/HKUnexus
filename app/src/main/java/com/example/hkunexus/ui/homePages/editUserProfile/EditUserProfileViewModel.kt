package com.example.hkunexus.ui.homePages.editUserProfile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.hkunexus.data.RegexRule
import com.example.hkunexus.data.SupabaseSingleton
import com.example.hkunexus.data.UserSingleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.util.UUID

class EditUserProfileViewModel : ViewModel() {

    data class UiState(
        var firstName: String,
        var lastName: String,
        var username: String,
        var profilePicture: Uri? = null,
        var hasProfileImageChanged: Boolean = false
    )

    var imageFile: File? = File.createTempFile("lol","jpg")
    val BUCKET_URL_PREFIX = "https://ctiaasznssbnyizmglhv.supabase.co/storage/v1/object/public/"

    private val _uiState = MutableStateFlow(UiState("", "", ""))
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var mediaArg: String = ""

    fun initializeValues() {
        uiState.value.firstName = UserSingleton.first_name
        uiState.value.lastName = UserSingleton.last_name
        uiState.value.username = UserSingleton.display_name
        mediaArg = UserSingleton.userPfp
    }

    fun setFirstName(string: String) {
        uiState.value.firstName = string
    }

    fun setLastName(string: String) {
        uiState.value.lastName = string
    }

    fun setUsername(string: String) {
        uiState.value.username = string
    }

    fun setProfilePicture(uri: Uri) {
        uiState.value.profilePicture = uri
        uiState.value.hasProfileImageChanged = true
    }

    fun isFirstNameValid(): Boolean {
        return RegexRule.usernameRegex.matcher(uiState.value.firstName).matches()
    }

    fun isLastNameValid(): Boolean {
        return RegexRule.usernameRegex.matcher(uiState.value.lastName).matches()
    }

    fun isUsernameValid(): Boolean {
        return RegexRule.usernameRegex.matcher(uiState.value.username).matches()
    }

    fun isValid(): Boolean {
        return isFirstNameValid() && isLastNameValid() && isUsernameValid()
    }

    fun updateProfile(): Boolean {
        if (isValid()) {
            if (uiState.value.hasProfileImageChanged) {
                val success = uploadProfilePicture()
                if (!success) {
                    return false
                }
            }

            val userProfile = SupabaseSingleton.updateUserProfile(
                UserSingleton.userID,
                uiState.value.firstName,
                uiState.value.lastName,
                uiState.value.username,
                mediaArg
            )

//            return (userProfile != null)
            return true
        }
        return false
    }

    private fun uploadProfilePicture(): Boolean {
        val bannerImage = uiState.value.profilePicture
        if (bannerImage != null) {
            val postIdArg = UUID.randomUUID().toString()
            try {
                Log.d("POST", imageFile.toString())
                val result = SupabaseSingleton.uploadImageToBucket(
                    imageFile!!,
                    "user_profiles",
                    filepathArg = "images/attachment_$postIdArg.jpg",
                    register = true
                )
                Log.d("POST", result.toString())
                mediaArg = BUCKET_URL_PREFIX + result
                return true
            } catch (ex : Exception){
                Log.e("POST", ex.stackTraceToString())
            }
        }
        return false
    }

}