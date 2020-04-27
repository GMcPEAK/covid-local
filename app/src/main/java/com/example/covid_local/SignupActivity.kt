package com.example.covid_local

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class SignupActivity : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var location: AutoCompleteTextView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var enter: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        email = findViewById(R.id.enter_email)
        password = findViewById(R.id.enter_password)
        enter = findViewById(R.id.go_button)
        firebaseAuth = FirebaseAuth.getInstance()

        val preferences: SharedPreferences = getSharedPreferences(
            "covid-local",
            Context.MODE_PRIVATE
        )

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

        enter.setOnClickListener {
            val inputtedEmail: String = email.text.toString().trim()
            val inputtedPassword:String = password.text.toString().trim()
            val inputtedLocation:String = location.text.toString().trim()
            firebaseAuth.createUserWithEmailAndPassword(
                inputtedEmail, inputtedPassword
            ).addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    preferences
                        .edit()
                        .putString("email", inputtedEmail)
                        .putString("password", inputtedPassword)
                        .putString("location", inputtedLocation)
                        .apply()
                    //add the user's info to the database
                    var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
                    val fixedEmail = inputtedEmail.replace(".", "")
                    val reference = firebaseDatabase.getReference("users/$fixedEmail")
                    reference.setValue(inputtedLocation).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Added to DB",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val exception: Exception = it.exception!!
                            Toast.makeText(
                                this,
                                "DB add failed: $exception",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                    val user = firebaseAuth.currentUser
                    Toast.makeText(
                        this,
                        "Registered successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    //send user to next screen
                    val intent = Intent(this@SignupActivity, NewsActivity::class.java)
                    startActivity(intent)
                } else {
                    val exception: Exception = task.exception!!
                    Toast.makeText(
                        this,
                        "Registration failed: $exception",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}