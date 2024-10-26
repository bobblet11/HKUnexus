package com.example.hkunexus.data

import android.content.Intent
import android.util.Log
import android.widget.EditText
import com.example.hkunexus.MainActivity
import com.example.hkunexus.R
import com.google.gson.Gson
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.SignOutScope
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.encode
import io.github.jan.supabase.encodeToJsonElement
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.request.RpcRequestBuilder
import io.ktor.utils.io.core.toByteArray
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject
import org.json.JSONObject
import java.util.UUID
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.system.exitProcess



object SupabaseSingleton{

    private var url = "https://ctiaasznssbnyizmglhv.supabase.co"
    private var key = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImN0aWFhc3puc3Nibnlpem1nbGh2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3Mjg1NDQ3NzEsImV4cCI6MjA0NDEyMDc3MX0.t0-AHECeFc0PWItTVJ-X0BGGclh_LEbFhFOtBi9rNd4"
    private var accessToken: String? = null
    private var session: UserSession? = null
    private var currentUser: UserInfo? = null

    init {
        println("attempting to create supabase client using $url, $key")
    }

    private var client: SupabaseClient? = null

    init {
        getSupabase()
    }

    private fun getSupabase() {
        Log.e("SupabaseSingleton", "attempting to connect with supabase")
        try {
            this.client = createSupabaseClient(
                supabaseUrl = this.url,
                supabaseKey = this.key
            ) {
                install(Postgrest)
                install(Auth)
            }
        }catch (e: Exception) {
            Log.e("SupabaseSingleton", "could not create supabase client\nClosing app", e)
            exitProcess(0);
        }
        Log.e("SupabaseSingleton", "successfully connected with supabase")
    }

    public fun login(email: String, password: String):Boolean{

        return runBlocking {
            try {
                val result = client!!.auth.signInWith(Email) {
                    this.email = email;
                    this.password = password;
                }

                currentUser = client!!.auth.retrieveUserForCurrentSession(updateSession = true)
                session = client!!.auth.currentSessionOrNull()
                accessToken = session!!.accessToken

                Log.d("SupabaseSingleton", "Sign-in successful: $result")

                return@runBlocking true

            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Sign-in failed: $e")
                currentUser = null
                session = null
                accessToken = null
                client = null
                return@runBlocking false
            }
        }
    }

    public fun isEmailAvailable(email: String):Boolean{
        //query public user table
        return runBlocking {
            try{
//                Log.d("SupabaseSingletone", UUID.nameUUIDFromBytes(email.toByteArray()).toString())
                val jsonParams = buildJsonObject {
                    put("em", email)
                }
//                client?.auth?.signInAnonymously()
                val result = client?.postgrest?.rpc("check_email_exists", jsonParams)
                val str = result?.data.toString()
                Log.d("SupabaseSingleton", str)
//                client?.auth?.signOut(SignOutScope.GLOBAL)
                return@runBlocking true
            }catch(e: Exception){
                Log.d("SupabaseSingleton", e.toString())
                return@runBlocking false
            }
        }
    }


    public fun isUsernameAvailable(username: String):Boolean{
        //query public user table
        return runBlocking {
            try{
                val jsonParams = buildJsonObject {
                    put("name", username)
                }
//                client?.auth?.signInAnonymously()
                val result = client?.postgrest?.rpc("check_username_exists", jsonParams)
                val str = result?.data.toString()
                Log.d("SupabaseSingleton", str)

//                client?.auth?.signOut(SignOutScope.GLOBAL)
                return@runBlocking true
            }catch(e: Exception){
                Log.d("SupabaseSingleton", e.toString())
                return@runBlocking false
            }
        }

    }


    public fun register(firstName: String, lastName: String, email: String, password: String, username : String):Boolean{
        return runBlocking {
            try {
                val user = client!!.auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                    data = buildJsonObject {
                        put("firstName", firstName)
                        put("lastName", lastName)
                        put("username", username)
                    }
                }

                //will be anonymous user?
                //check docs for this.
                currentUser = client!!.auth.retrieveUserForCurrentSession(updateSession = true)
                session = client!!.auth.currentSessionOrNull()
                accessToken = session!!.accessToken

                Log.d("SupabaseSingleton", "Sign-up successful: $user")

                return@runBlocking true

            } catch (e: Exception) {
                Log.d("SupabaseSingleton", "Sign-in failed: $e")
                currentUser = null
                session = null
                accessToken = null
                client = null
                return@runBlocking false
            }

        }
    }

    public fun getAccessToken():String{
        try{
            if (this.accessToken!!.isNotEmpty()){
                Log.d("SupabaseSingleton", "retrieved access token")
                return this.accessToken!!
            }
        }catch (e: Exception) {
            Log.d("SupabaseSingleton", "no access token available, sign-in again")
        }
        return ""
    }

    public fun authenticateOtp(otpInput: String):Boolean{




        return true
    }

}