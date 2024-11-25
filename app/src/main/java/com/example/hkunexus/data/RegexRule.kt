package com.example.hkunexus.data

import java.util.regex.Pattern

class RegexRule {
    companion object {
        val emailRegex: Pattern = Pattern.compile(
            "^" +                // Start of the string
                    "(?!.*\\s)" +       // No whitespace
                    "(?!.*@)" +         // No @ character
                    "[\\S]+" +          // One or more non-whitespace characters
                    "$"                 // End of the string
        )

        val usernameRegex: Pattern = Pattern.compile(
            "^" +                // Start of the string
                    "(?!.*\\s)" +       // No whitespace
                    "(?!.*@)" +         // No @ character
                    "[\\S]+" +          // One or more non-whitespace characters
                    "$"                 // End of the string
        )

        val passwordRegex: Pattern = Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[^A-Za-z0-9])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$"
        )

    }
}