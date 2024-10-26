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

data class MyEventsUiState(
    val listOfEventsToDisplay: Array<Event> = arrayOf(),
    val eventListAdapter: ClubListAdapter? = null,
    val recyclerView: RecyclerView? = null,
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
        val tempList: Array<Event> = arrayOf()

        for (item: Event in tempList) {

            if (item.postText.length >= MAX_NUM_CHAR_IN_EVENT_CARD_DESCRIPTION) {
                val newPostText =
                    item.postText.substring(0, MAX_NUM_CHAR_IN_EVENT_CARD_DESCRIPTION - 4) + "..."
                item.postText = newPostText
            }
        }
    }
}