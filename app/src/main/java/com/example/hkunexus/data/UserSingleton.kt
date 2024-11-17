package com.example.hkunexus.data

import com.example.hkunexus.data.model.dto.UserProfileDto


object UserSingleton{
    public var userID = ""
    public var email=""
    public var display_name=""

    public fun clear(){
        userID = ""
        email=""
        display_name=""
    }

}