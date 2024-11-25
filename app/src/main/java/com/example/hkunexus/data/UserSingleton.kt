package com.example.hkunexus.data

import com.example.hkunexus.data.model.dto.UserProfileDto


object UserSingleton {
    var userID = ""
    var email = ""
    var display_name = ""
    var first_name = ""
    var last_name = ""
    var userPfp = ""

    fun clear(){
        userID = ""
        email=""
        display_name=""
        first_name=""
        last_name=""
        userPfp=""
    }

}