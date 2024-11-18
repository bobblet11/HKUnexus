package com.example.hkunexus

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.example.hkunexus.data.SupabaseSingleton

fun getEditDisplayNameDialog(context: Context): AlertDialog{

    val builder = AlertDialog.Builder(context)
    builder.setTitle("Change Display Name")

    val editTextField : EditText = EditText(context)

    builder.setView(editTextField)

    builder.setPositiveButton("Save Change") { dialog, which ->

        }
    builder.setNegativeButton("Cancel") { dialog, which ->

        }

    return builder.create()

}
fun getEditProfileDialog(context: Context): AlertDialog{
    val builder = AlertDialog.Builder(context)
    builder.setTitle("Edit User Profile")

// add a list
    val settingOption = arrayOf(
        "Change Display Name",
        "Change First Name",
        "Change Last Name",
        "Change Profile Picture",
        "Return")
    builder.setItems(settingOption) { dialog, which ->
        when (which) {
            0 -> { getEditDisplayNameDialog(context).show()}
            1 -> { /* cow   */ }
            2 -> { /* camel */ }
            3 -> { /* sheep */ }
            4 -> { /* goat  */ }
        }
    }

// create and show the alert dialog
    return builder.create()
}