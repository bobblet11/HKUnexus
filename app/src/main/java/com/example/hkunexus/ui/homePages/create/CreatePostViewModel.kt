package com.example.hkunexus.ui.homePages.create

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hkunexus.data.SupabaseSingleton
import com.example.hkunexus.data.SupabaseSingleton.getAllJoinedClubs
import com.example.hkunexus.data.UserSingleton
import com.example.hkunexus.data.model.dto.ClubDto
import com.example.hkunexus.data.model.dto.EventDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

data class MyGroupsUiState(
    val selectedClub: ClubDto? = null,
    val isClubSelected: Boolean = false,
    val isEventPost: Boolean = false,
    val isEventSelected: Boolean = false,
    val selectedEvent: EventDto? = null,
    val isPostValid: Boolean = false,
    val postTitle: String = "",
    val postBody: String = "",

)

val MIN_TITLE_LENGTH = 10
val MAX_TITLE_LENGTH = 20

val MIN_BODY_LENGTH = 20
val MAX_BODY_LENGTH = 100

class CreatePostViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(MyGroupsUiState())
    val uiState: StateFlow<MyGroupsUiState> = _uiState.asStateFlow()

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

    public fun createPost(){
        if (!uiState.value.isEventPost){
            val result = SupabaseSingleton.insertOrUpdatePost(UUID.randomUUID().toString(), UserSingleton.userID, uiState.value.selectedClub!!.clubId!!, uiState.value.postTitle,
                uiState.value.postBody, "")
            Log.d("POST", result.toString())
        }
    }
}
