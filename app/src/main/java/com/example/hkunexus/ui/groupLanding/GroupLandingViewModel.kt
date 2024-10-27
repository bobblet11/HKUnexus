package com.example.hkunexus.ui.groupLanding

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.hkunexus.data.TempClubList
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
class GroupLandingViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GroupLandingUiState())

    var club: Club = Club("name", "desc")

    val uiState: StateFlow<GroupLandingUiState> = _uiState.asStateFlow()

    private final val MAX_NUM_CHAR_IN_EVENT_CARD_DESCRIPTION = 80;

    init {
        fetchPosts()
    }

    fun fetchClubData(clubId: Int) {
        // TODO: Fetch using Supabase
        club = TempClubList.clubs[clubId]
    }

    private fun fetchPosts() {
        //FETCH USING SUPABASE
        //USE USER ID HERE FROM SINGLETON

        val tempList: Array<Post> = arrayOf(
            Event(
                postTitle = "Community Cleanup Event",
                posterUsername = "john_doe",
                postersProfileImage = "https://example.com/images/john.jpg",
                postImage = "https://example.com/images/event1.jpg",
                postText = "Excited to announce our upcoming community cleanup event!",
                timeSincePosted = "4 days ago"
            )
        )

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