package com.example.hkunexus.ui.homePages.myevents

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hkunexus.data.SupabaseSingleton
import com.example.hkunexus.data.model.dto.EventDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MyEventsUiState(
    val listOfEventsToDisplay: ArrayList<EventDto> = arrayListOf(),
    )

class MyEventsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MyEventsUiState())
    val uiState: StateFlow<MyEventsUiState> = _uiState.asStateFlow()

    init {
        fetchMyEvents()
    }

    fun fetchMyEvents() {

        viewModelScope.launch {
            val tempList: List<EventDto> = SupabaseSingleton.getAllJoinedEventsFromRecentAsync()
            Log.d("new list", tempList.toString())
            _uiState.update {
                it.copy(
                    listOfEventsToDisplay = tempList.toCollection(ArrayList())
                )
            }


        }

    }
}