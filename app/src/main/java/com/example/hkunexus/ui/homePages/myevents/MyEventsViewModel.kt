package com.example.hkunexus.ui.homePages.myevents

import androidx.lifecycle.ViewModel
import com.example.hkunexus.data.TempData
import com.example.hkunexus.data.model.Event
import com.example.hkunexus.data.model.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MyEventsUiState(
    val listOfEventsToDisplay: Array<Post> = arrayOf(),
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

        val tempList = TempData.events

        for (item: Post in tempList) {

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