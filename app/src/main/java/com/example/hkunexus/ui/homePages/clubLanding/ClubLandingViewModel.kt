package com.example.hkunexus.ui.homePages.clubLanding

import androidx.lifecycle.ViewModel
import com.example.hkunexus.data.TempData
import com.example.hkunexus.data.model.Club
import com.example.hkunexus.data.model.Event
import com.example.hkunexus.data.model.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class GroupLandingUiState(
    val posts: Array<Post> = arrayOf(),
)

// Taken from MyEventsViewModel
class ClubLandingViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GroupLandingUiState())


    val uiState: StateFlow<GroupLandingUiState> = _uiState.asStateFlow()

    private val MAX_NUM_CHAR_IN_EVENT_CARD_DESCRIPTION = 80;

    init {
        fetchPosts()
    }

    // Placeholder value
    var club: Club = Club("name", "desc", false, arrayOf())

    fun fetchClubData(clubId: Int) {
        // TODO: Fetch using Supabase
        club = TempData.clubs[clubId]
    }

    private fun fetchPosts() {
        //FETCH USING SUPABASE
        //USE USER ID HERE FROM SINGLETON

        val tempList: Array<Post> = TempData.clubPosts

        for (item: Post in tempList) {

            if (item.postText.length >= MAX_NUM_CHAR_IN_EVENT_CARD_DESCRIPTION) {
                val newPostText =
                    item.postText.substring(0, MAX_NUM_CHAR_IN_EVENT_CARD_DESCRIPTION - 4) + "..."
                item.postText = newPostText
            }

            _uiState.update {
                it.copy(
                    posts = tempList
                )
            }
        }
    }
}