package com.example.hkunexus.ui.homePages.clubLanding.users

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hkunexus.data.SupabaseSingleton
import com.example.hkunexus.data.UserSingleton
import com.example.hkunexus.data.model.dto.UserProfileWithRoleDto
import com.example.hkunexus.ui.homePages.clubLanding.ClubLandingUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class uiState(
    val members: ArrayList<UserProfileWithRoleDto> = arrayListOf(),
    val isAdmin: Boolean = false,
    val clubID: String? = "",
)

class UserListViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(uiState())
    val uiState: StateFlow<uiState> = _uiState.asStateFlow()

    fun fetchMembers(adapter: UserListListAdapter){
        viewModelScope.launch {
            val members = SupabaseSingleton.getMembersAsync(uiState.value.clubID!!)

            for (member in members){
                if (member.id == UserSingleton.userID){
                    Log.d("dawa", member.role)
                    updateIsAdmin(member.role == "Admin")
                    adapter.setIsAdmin(uiState.value.isAdmin)
                }
            }

            updateMembers(members)
        }
    }

    private fun updateMembers(members: ArrayList<UserProfileWithRoleDto>){
        _uiState.update {
            it.copy(
                members = members
            )
        }
    }

    private fun updateIsAdmin(isAdmin: Boolean){
        _uiState.update {
            it.copy(
                isAdmin = isAdmin
            )
        }
    }

    fun updateClubID(clubId: String?){
        _uiState.update {
            it.copy(
                clubID = clubId
            )
        }
    }


}