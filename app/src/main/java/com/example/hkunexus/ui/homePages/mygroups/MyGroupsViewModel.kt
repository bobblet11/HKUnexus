package com.example.hkunexus.ui.homePages.myevents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hkunexus.data.SupabaseSingleton
import com.example.hkunexus.data.model.dto.ClubDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MyGroupsUiState(
    val listOfGroupsToDisplay: List<ClubDto> = arrayListOf(),
)

class MyGroupsViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(MyGroupsUiState())
    val uiState: StateFlow<MyGroupsUiState> = _uiState.asStateFlow()

    init {
        fetchMyGroups()
    }

    fun fetchMyGroups() {
        viewModelScope.launch {
            val tempList = SupabaseSingleton.getAllJoinedClubsAsync()

            _uiState.update {
                it.copy(
                    listOfGroupsToDisplay = tempList
                )
            }
        }

    }


}