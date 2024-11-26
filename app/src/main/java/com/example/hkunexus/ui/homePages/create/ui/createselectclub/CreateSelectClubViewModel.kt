package com.example.hkunexus.ui.homePages.create.ui.createselectclub

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hkunexus.data.SupabaseSingleton
import com.example.hkunexus.data.model.dto.ClubDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CreateSelectClubUiState(
    val listOfGroupsToDisplay: ArrayList<ClubDto> = arrayListOf()
)


class CreateSelectClubViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CreateSelectClubUiState())
    val uiState: StateFlow<CreateSelectClubUiState> = _uiState.asStateFlow()

    init {
        fetchMyGroups()
    }



    private fun fetchMyGroups() {
        viewModelScope.launch {
            val tempList = SupabaseSingleton.getAllJoinedClubsAsyncAdmin()

            _uiState.update {
                it.copy(
                    listOfGroupsToDisplay = tempList.toCollection(ArrayList())
                )
            }
        }
    }
}