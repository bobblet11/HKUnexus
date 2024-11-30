package com.example.hkunexus.ui.homePages.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.data.SupabaseSingleton
import com.example.hkunexus.data.TempData
import com.example.hkunexus.data.model.dto.ClubDto
import com.example.hkunexus.data.model.dto.PostDto
import com.example.hkunexus.data.model.dto.Tag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


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
        var tempList: Array<ClubDto>? = arrayOf()
        val id = selectedTagID

        viewModelScope.launch {
            tempList = if (id == null) {
                SupabaseSingleton.searchClubsByLikeNameAsync(query)?.toTypedArray()
            } else {
                SupabaseSingleton.searchClubsAsync(arrayOf(id), "")?.toTypedArray()
            }

            updateClubList(tempList!!)
        }
    }

    fun fetchTags() {
        viewModelScope.launch {
            var tempList: Array<Tag>? = SupabaseSingleton.searchTagsAsync("")?.toTypedArray()
            if (tempList == null){
                tempList = TempData.tags
            }
            val tags: MutableList<Tag> = tempList.toMutableList()
            tags.add(0, Tag(null, "Any"))

            updateTagList(tags.toTypedArray())
        }
    }

}