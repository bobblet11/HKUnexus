package com.example.hkunexus.ui.homePages.explore

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.data.SupabaseSingleton
import com.example.hkunexus.data.TempData
import com.example.hkunexus.data.model.Club
import com.example.hkunexus.data.model.Post
import com.example.hkunexus.data.model.dto.ClubDto
import com.example.hkunexus.data.model.dto.Tag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class ExploreUiState(
    val listOfClubsToDisplay: Array<ClubDto> = arrayOf(),
    val listOfTags: Array<String> = arrayOf(),

    val clubListAdapter: ExploreListAdapter? = null,
    val recyclerView: RecyclerView? = null,
)

class ExploreViewModel : ViewModel() {

    private val MAX_NUM_CHAR_IN_CLUB_TITLE: Int = 30
    private val MAX_NUM_CHAR_IN_CLUB_DESCRIPTION: Int =80

    private val _uiState = MutableStateFlow(ExploreUiState())
    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()

    init {
        fetchClubs()
        fetchTags()
    }

    private fun updateClubList(newClubs: Array<ClubDto>){
        _uiState.update {
            it.copy(
                listOfClubsToDisplay = newClubs
            )
        }
    }

    private fun updateTagList( newTags: MutableList<String>){
        _uiState.update {
            it.copy(
                listOfTags = newTags.toTypedArray()
            )
        }
    }


    private fun fetchClubs(){
        // TODO: FETCH USING SUPABASE

        var tempList: Array<ClubDto>? = SupabaseSingleton.getRandomClubs()?.toTypedArray()

        if (tempList == null){
            tempList = TempData.clubs
        }

        for (item: ClubDto in tempList) {
            if (item.clubName!!.length >= MAX_NUM_CHAR_IN_CLUB_TITLE){
                val newName = item.clubName!!.substring(0,MAX_NUM_CHAR_IN_CLUB_TITLE-4) + "..."
                item.clubName = newName
            }

            if (item.clubDesc!!.length >= MAX_NUM_CHAR_IN_CLUB_DESCRIPTION){
                val newDescription = item.clubDesc!!.substring(0,MAX_NUM_CHAR_IN_CLUB_DESCRIPTION-4) + "..."
                item.clubDesc = newDescription
            }
        }

        updateClubList(tempList)
    }

    private fun fetchTags() {
        val tempList: Array<String> = TempData.tags.clone()

        val tags: MutableList<String> = tempList.toMutableList()
        tags.add(0, "Any")

        updateTagList(tags)
    }

}