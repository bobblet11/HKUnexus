package com.example.hkunexus.ui.homePages.mygroups

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyGroupsViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is mygroups Fragment"
    }
    val text: LiveData<String> = _text
}