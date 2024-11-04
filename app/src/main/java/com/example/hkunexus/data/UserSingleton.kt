package com.example.hkunexus.data

import android.util.Log
import com.example.hkunexus.data.model.Club
import com.example.hkunexus.data.model.dto.ClubDto
import com.example.hkunexus.data.model.dto.Tag
import com.example.hkunexus.data.model.dto.UserProfileDto
import com.example.hkunexus.data.model.dto.UserToClubDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.system.exitProcess

import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.putJsonObject
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.putJsonArray
import org.json.JSONArray
import java.util.UUID
import kotlin.uuid.Uuid


object UserSingleton{
    public var userID = ""
    public var userProfile: UserProfileDto? = null


    public fun clear(){
        userID = ""
        userProfile = null
    }

}