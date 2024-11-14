package com.example.hkunexus.data

import android.util.Log
import android.view.View
import android.widget.Button
import com.example.hkunexus.R

class EventInterface {

    companion object {
        fun joinEvent(eventId: String) {
            Log.d("eventInterface", "join " + eventId)
            SupabaseSingleton.insertOrUpdateCurrentUserToEvent(eventId)
        }

        fun leaveEvent(eventId: String) {
            Log.d("eventInterface", "leave " + eventId)
            SupabaseSingleton.removeCurrentUserFromEvent(eventId)
        }

        fun hasJoinedEvent(eventId: String): Boolean {
            Log.d("eventInterface", "check " + eventId)
            val list = SupabaseSingleton.getCurrentUserToEventByIds(eventId)
            Log.d("eventInterface", "check " + list.toString())
            if (list != null) {
                return list.isNotEmpty()
            }
            return false
        }

        fun attachListenersAndUpdatersToEventJoiningButtons(rootView: View, eventId: String?): () -> Unit {
            if (eventId == null) {
                return {}
            }

            val outerView = rootView.findViewById<View>(R.id.event_action_buttons)
            val joinButton: Button = outerView.findViewById<Button>(R.id.join_event_button)
            val leaveButton: Button = outerView.findViewById<Button>(R.id.leave_event_button)

            val updateButtonVisibility = {
                val joined = hasJoinedEvent(eventId)

                if (joined) {
                    joinButton.visibility = View.GONE
                    leaveButton.visibility = View.VISIBLE
                } else {
                    joinButton.visibility = View.VISIBLE
                    leaveButton.visibility = View.GONE
                }
            }

            joinButton.setOnClickListener {
                joinEvent(eventId)
                updateButtonVisibility()
            }

            leaveButton.setOnClickListener {
                leaveEvent(eventId)
                updateButtonVisibility()
            }

            updateButtonVisibility()

            return updateButtonVisibility
        }

    }

}
