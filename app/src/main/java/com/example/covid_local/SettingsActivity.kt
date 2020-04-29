package com.example.covid_local

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception

class SettingsActivity : AppCompatActivity(){

    private lateinit var oldPass: EditText
    private lateinit var newPass1: EditText
    private lateinit var newPass2: EditText
    private lateinit var location: AutoCompleteTextView
    private lateinit var enterPass: Button
    private lateinit var enterLocation: Button
    private lateinit var logoutButton: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private var user: FirebaseUser? = null
    private lateinit var currLoc: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val preferences: SharedPreferences = getSharedPreferences(
            "covid-local",
            Context.MODE_PRIVATE
        )
        firebaseAuth = FirebaseAuth.getInstance()
        user = firebaseAuth.currentUser;
        oldPass = findViewById(R.id.oldPass)
        newPass1 = findViewById(R.id.newPass1)
        newPass2 = findViewById(R.id.newPass2)
        enterPass = findViewById(R.id.submit_pass_change)
        enterLocation = findViewById(R.id.submit_location_change)
        logoutButton = findViewById(R.id.logout_button)
        currLoc = findViewById(R.id.currLoc)
        currLoc.text = ("Current location: "+preferences.getString("location", ""))



        val countries = arrayOf(
            "United States",
            "Alabama",
            "Alaska",
            "Arkansas",
            "California",
            "Colorado",
            "Connecticut",
            "Delaware",
            "Florida",
            "Georgia",
            "Hawaii",
            "Idaho",
            "Illinois",
            "Indiana",
            "Iowa",
            "Kansas",
            "Kentucky",
            "Louisiana",
            "Maine",
            "Maryland",
            "Massachusetts",
            "Michigan",
            "Minnesota",
            "Mississippi",
            "Missouri",
            "Montana",
            "Nebraska",
            "Nevada",
            "New Hampshire",
            "New Jersey",
            "New Mexico",
            "New York",
            "North Carolina",
            "North Dakota",
            "Ohio",
            "Oklahoma",
            "Oregon",
            "Pennsylvania",
            "Rhode Island",
            "South Carolina",
            "South Dakota",
            "Tennessee",
            "Texas",
            "Utah",
            "Vermont",
            "Virginia",
            "Washington",
            "West Virginia",
            "Wisconsin",
            "Wyoming",
            "Virgin Islands",
            "Guam",
            "American Samoa",
            "Puerto Rico",
            "DC"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line, countries
        )
        location = findViewById<AutoCompleteTextView>(R.id.enter_location)
        location.setAdapter(adapter)

        enterPass.setOnClickListener {
            if (newPass1.text.toString() == newPass2.text.toString() && newPass1.text.toString() != oldPass.text.toString() && oldPass.text.toString() == preferences.getString("password", "")) {
                try {
                    user?.updatePassword(newPass1.text.toString())
                    preferences.edit().putString("password", newPass1.text.toString()).apply()
                    Toast.makeText(
                        this,
                        "Password update succeeded",
                        Toast.LENGTH_LONG
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(
                        this,
                        "Password update failed: $e",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                when {
                    oldPass.text.toString() != preferences.getString("password", "") -> {
                        Toast.makeText(
                            this,
                            "Password update failed: Old password is incorrect",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    newPass1.text.toString() == oldPass.toString() -> {
                        Toast.makeText(
                            this,
                            "Password update failed: New password is the same as the old password",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    newPass1.text.toString() != newPass2.toString() -> {
                        Toast.makeText(
                            this,
                            "Password update failed: New password and Confirm new password do not match",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        enterLocation.setOnClickListener {
            var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
            val fixedEmail = user?.email?.replace(".", "")
            val reference = firebaseDatabase.getReference("users/$fixedEmail")
            reference.setValue(location.text.toString().trim()).addOnCompleteListener {
                if (it.isSuccessful) {
                    preferences.edit().putString("location", location.text.toString().trim()).apply()
                    Toast.makeText(
                        this,
                        "Added to DB",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@SettingsActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    val exception: Exception = it.exception!!
                    Toast.makeText(
                        this,
                        "DB add failed: $exception",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
        }

        logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(this@SettingsActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}