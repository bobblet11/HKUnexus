package com.example.hkunexus.ui.homePages.myevents

import androidx.lifecycle.ViewModel
import com.example.hkunexus.data.SupabaseSingleton
import com.example.hkunexus.data.model.dto.EventDto
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

    init {
        fetchMyEvents()
    }

    fun fetchMyEvents() {

        val tempList = SupabaseSingleton.getAllJoinedEvents()

        for (item: EventDto in tempList) {

            _uiState.update {
                it.copy(
                    listOfEventsToDisplay = tempList
                )
            }
        }

    }
}