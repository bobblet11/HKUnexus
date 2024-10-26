package com.example.hkunexus.ui.homePages.myevents

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.data.model.Club
import com.example.hkunexus.data.model.Event
import com.example.hkunexus.ui.homePages.explore.ClubListAdapter
import com.example.hkunexus.ui.homePages.explore.ExploreUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MyEventsUiState(
    val listOfEventsToDisplay: Array<Event> = arrayOf(),
    )

class MyEventsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MyEventsUiState())
    val uiState: StateFlow<MyEventsUiState> = _uiState.asStateFlow()

    private final val MAX_NUM_CHAR_IN_EVENT_CARD_DESCRIPTION = 80;

    init {
        fetchMyEvents()
    }

    private fun fetchMyEvents() {
        //FETCH USING SUPABASE
        //USE USER ID HERE FROM SINGLETON

        val tempList = arrayOf(
            Event(
                postersUsername = "john_doe",
                postersProfileImage = "https://example.com/images/john.jpg",
                postImage = "https://example.com/images/event1.jpg",
                postText = "Excited to announce our upcoming community cleanup event!",
                timeSincePosted = "4 days ago"
            ),
            Event(
                postersUsername = "jane_smith",
                postersProfileImage = "https://example.com/images/jane.jpg",
                postImage = "https://example.com/images/event2.jpg",
                postText = "Join us for a fun day of hiking this Saturday!",
                timeSincePosted = "9 days ago"
            ),
            Event(
                postersUsername = "mark_taylor",
                postersProfileImage = "https://example.com/images/mark.jpg",
                postImage = "https://example.com/images/event3.jpg",
                postText = "Don't miss our charity bake sale next week!",
                timeSincePosted = "17 days ago"
            ),
            Event(
                postersUsername = "lisa_white",
                postersProfileImage = "https://example.com/images/lisa.jpg",
                postImage = "https://example.com/images/event4.jpg",
                postText = "We're hosting a movie night under the stars! Bring your blankets!",
                timeSincePosted = "19 days ago"
            )
        )

        for (item: Event in tempList) {

            if (item.postText.length >= MAX_NUM_CHAR_IN_EVENT_CARD_DESCRIPTION) {
                val newPostText =
                    item.postText.substring(0, MAX_NUM_CHAR_IN_EVENT_CARD_DESCRIPTION - 4) + "..."
                item.postText = newPostText
            }

            _uiState.update {
                it.copy(
                    listOfEventsToDisplay = tempList
                )
            }
        }
    }
}