package com.example.hkunexus.ui.homePages.explore

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.data.SupabaseSingleton
import com.example.hkunexus.data.TempData
import com.example.hkunexus.data.model.dto.ClubDto
import com.example.hkunexus.data.model.dto.Tag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class ExploreUiState(
    val listOfClubsToDisplay: Array<ClubDto> = arrayOf(),
    val listOfTags: Array<Tag> = arrayOf(),

    val clubListAdapter: ExploreListAdapter? = null,
    val recyclerView: RecyclerView? = null,
)

class ExploreViewModel : ViewModel() {

    private val MAX_NUM_CHAR_IN_CLUB_TITLE: Int = 30
    private val MAX_NUM_CHAR_IN_CLUB_DESCRIPTION: Int =80

    private val _uiState = MutableStateFlow(ExploreUiState())
    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()

    private var keyword: String = ""
    private var selectedTagID: String? = null

    init {
        fetchTags()
        fetchClubs()
    }

    private fun updateClubList(newClubs: Array<ClubDto>){
        _uiState.update {
            it.copy(
                listOfClubsToDisplay = newClubs
            )
        }
    }

    private fun updateTagList( newTags: Array<Tag>){
        _uiState.update {
            it.copy(
                listOfTags = newTags
            )
        }
    }

    fun setKeyword(keyword: String) {
        this.keyword = keyword
    }

    fun setSelectedTagID(tag_id: String?) {
        selectedTagID = tag_id
    }

    fun fetchClubs(){
        val query = keyword.trim().lowercase()
        var tempList: Array<ClubDto>?
        val id = selectedTagID

        tempList = if (id == null) {
            if (query == "") {
                SupabaseSingleton.getRandomClubs()?.toTypedArray()
            } else {
                SupabaseSingleton.searchClubsByLikeName(query)?.toTypedArray()
            }
        } else {
            SupabaseSingleton.searchClubs(arrayOf(id), "")?.toTypedArray()
        }

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
        var tempList: Array<Tag>? = SupabaseSingleton.searchTags("")?.toTypedArray()

        if (tempList == null){
            tempList = TempData.tags
        }

        val tags: MutableList<Tag> = tempList.toMutableList()
        tags.add(0, Tag(null, "Any"))

        updateTagList(tags.toTypedArray())
    }

}