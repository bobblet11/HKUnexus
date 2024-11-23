package com.example.hkunexus.ui.homePages.create

import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import androidx.lifecycle.ViewModel
import com.example.hkunexus.data.SupabaseSingleton
import com.example.hkunexus.data.UserSingleton
import com.example.hkunexus.data.model.dto.ClubDto
import com.example.hkunexus.data.model.dto.EventDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import java.io.InputStream
import java.util.UUID


val MIN_TITLE_LENGTH = 5
val MAX_TITLE_LENGTH = 100

val MIN_BODY_LENGTH = 5
val MAX_BODY_LENGTH = 500

val BUCKET_URL_PREFIX = "https://ctiaasznssbnyizmglhv.supabase.co/storage/v1/object/public/"

class CreatePostViewModel: ViewModel() {
    data class MyGroupsUiState(
        val selectedClub: ClubDto? = null,
        val isClubSelected: Boolean = false,
        val isEventPost: Boolean = false,
        val isEventSelected: Boolean = false,
        val selectedEvent: EventDto? = null,
        val isPostValid: Boolean = false,
        val postTitle: String = "",
        val postBody: String = "",
        val postImage: Uri? = null
    )

    private val _uiState = MutableStateFlow(MyGroupsUiState())
    val uiState: StateFlow<MyGroupsUiState> = _uiState.asStateFlow()
    var imageFile: File? = File.createTempFile("lol","jpg");

    fun setSelectedClub(club: ClubDto?) {

        _uiState.update {
            it.copy(
                selectedClub = club
            )
        }

    }

    fun setIsEventSelected(bool: Boolean) {

        _uiState.update {
            it.copy(
                isEventSelected = bool
            )
        }

    }

    fun setIsClubSelected(bool: Boolean) {

        _uiState.update {
            it.copy(
                isClubSelected = bool
            )
        }

    }

    fun setPostTitle(title: String) {

        _uiState.update {
            it.copy(
                postTitle = title
            )
        }
        checkPostValid()
    }

    private fun checkPostValid(){
        val titleValid = (uiState.value.postTitle.length in MIN_TITLE_LENGTH..MAX_TITLE_LENGTH)

        val bodyValid = (uiState.value.postBody.length in MIN_BODY_LENGTH..MAX_BODY_LENGTH)

        setIsPostValid(titleValid&&bodyValid)
    }

    fun setPostBody(body: String) {

        _uiState.update {
            it.copy(
                postBody = body
            )
        }

        checkPostValid()

    }


    fun setSelectedEvent(event: EventDto?) {

        _uiState.update {
            it.copy(
                selectedEvent = event
            )
        }

    }


    fun setIsEventPost(bool: Boolean) {

        _uiState.update {
            it.copy(
                isEventPost = bool
            )
        }

    }

    private fun setIsPostValid(bool: Boolean) {
        _uiState.update {
            it.copy(
                isPostValid = bool
            )
        }
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


    fun createPost(): Boolean {


        if (uiState.value.isPostValid) {
            val postIdArg = UUID.randomUUID().toString()
            var mediaArg = ""
            if (hasPostImage()){
                try {
                    Log.d("POST", imageFile.toString())
                    val result = SupabaseSingleton.uploadImageToBucket(
                        imageFile!!,
                        "post_images",
                        filepathArg = "images/attachment_$postIdArg.jpg"
                    )
                    Log.d("POST", result.toString())
                    mediaArg = (BUCKET_URL_PREFIX + result) ?: "";

                } catch (ex : Exception){
                    Log.e("POST", ex.stackTraceToString())

                }

            }

            // TODO: Do something with image upload

            val result = SupabaseSingleton.insertOrUpdatePost(
                postIdArg,
                UserSingleton.userID,
                uiState.value.selectedClub!!.clubId!!,
                uiState.value.postTitle,
                uiState.value.postBody,
                mediaArg
            )
            Log.d("POST", result.toString())
            if (uiState.value.isEventPost){
                val eventIdArg = uiState.value.selectedEvent!!.id!!
                val result2 = SupabaseSingleton.insertEventToPost(
                    eventIdArg,
                    postIdArg,
                )
                Log.d("POST", result2.toString())
            }
            return true
        }
        return false
    }

    fun reset() {
        _uiState.update {
            it.copy(
                selectedClub = null,
                isClubSelected = false,
                isEventPost = false,
                isEventSelected = false,
                selectedEvent = null,
                isPostValid = false,
                postTitle = "",
                postBody = "",
                postImage = null
            )
        }
    }
}

