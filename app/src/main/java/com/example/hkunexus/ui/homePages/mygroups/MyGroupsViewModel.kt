package com.example.hkunexus.ui.homePages.myevents

import androidx.lifecycle.ViewModel
import com.example.hkunexus.data.SupabaseSingleton
import com.example.hkunexus.data.SupabaseSingleton.getAllJoinedClubs
import com.example.hkunexus.data.model.dto.ClubDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MyGroupsUiState(
    val listOfGroupsToDisplay: List<ClubDto> = getAllJoinedClubs(),
)

class MyGroupsViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(MyGroupsUiState())
    val uiState: StateFlow<MyGroupsUiState> = _uiState.asStateFlow()

    init {
        fetchMyEvents()
    }

    private fun fetchMyEvents() {
        val tempList = SupabaseSingleton.getAllJoinedClubs()

        for (item: ClubDto in tempList) {

            _uiState.update {
                it.copy(
                    listOfGroupsToDisplay = tempList
                )
            }
        }
    }
}