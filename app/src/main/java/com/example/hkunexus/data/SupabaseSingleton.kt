package com.example.hkunexus.data

import android.content.Intent
import android.util.Log
import android.widget.EditText
import com.example.hkunexus.MainActivity
import com.example.hkunexus.R
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
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

        try{

        }catch(e: Exception){

        }
        return true
    }


    public fun isUsernameAvailable(username: String):Boolean{
        //query public user table
        try{

        }catch(e: Exception){

        }
        return true
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