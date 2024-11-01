package com.example.hkunexus.ui.homePages.myevents

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.hkunexus.data.SupabaseSingleton
import com.example.hkunexus.data.TempData
import com.example.hkunexus.data.model.Event
import com.example.hkunexus.data.model.EventPost
import com.example.hkunexus.data.model.GenericPost
import com.example.hkunexus.data.model.Post
import com.example.hkunexus.data.model.UserProfile
import com.example.hkunexus.data.model.dto.EventDto
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MyEventsUiState(
    val listOfEventsToDisplay: List<EventDto> = listOf(),
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

        val tempList = SupabaseSingleton.getEventFromUser()

        for (item: EventDto in tempList) {

            _uiState.update {
                it.copy(
                    listOfEventsToDisplay = tempList
                )
            }
        }
    }
}