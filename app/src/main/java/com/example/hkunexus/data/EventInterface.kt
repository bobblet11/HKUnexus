package com.example.hkunexus.data

import android.util.Log

class EventInterface {

    companion object {
        fun joinEvent(eventId: String) {
            // TODO: Supabase
//        SupabaseSingleton.joinEvent(eventId)\
            Log.d("eventInterface", "join " + eventId)
        }

        fun leaveEvent(eventId: String) {
//        SupabaseSingleton.leaveEvent(eventId)
            Log.d("eventInterface", "leave " + eventId)
        }

        fun hasJoinedEvent(eventId: String): Boolean {
            Log.d("eventInterface", "check " + eventId)
            return false
        }

    }

}