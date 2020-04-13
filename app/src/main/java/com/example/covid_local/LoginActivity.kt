package com.example.covid_local

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class LoginActivity : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var enter: Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        email = findViewById(R.id.enter_email)
        password = findViewById(R.id.enter_password)
        enter = findViewById(R.id.go_button)
        firebaseAuth = FirebaseAuth.getInstance()

        val preferences: SharedPreferences = getSharedPreferences(
            "covid-local",
            Context.MODE_PRIVATE
        )

        enter.setOnClickListener {
            val inputtedEmail: String = email.text.toString().trim()
            val inputtedPassword:String = password.text.toString().trim()
            firebaseAuth.signInWithEmailAndPassword(
                inputtedEmail, inputtedPassword
            ).addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
                    val fixedEmail = inputtedEmail.replace(".", "")
                    var country: String = ""
                    var reference = FirebaseDatabase
                        .getInstance()
                        .reference
                        .child("users/$fixedEmail")

                    reference.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            country = dataSnapshot.value as String
                            Log.d("ON DATA CHANGE", "LINE REACHED")
                            Log.d("COUNTRY IS", country)
                            if (country == "") {
                                Log.d("FIREBASE DB ERROR", "COUNTRY NOT INITIALIZED")
                            } else {
                                preferences
                                    .edit()
                                    .putString("email", inputtedEmail)
                                    .putString("password", inputtedPassword)
                                    .putString("country", country)
                                    .apply()
                                Log.d("FIREBASE SUCCESS", "COUNTRY INITIALIZED SUCCESSFULLY")
                            }
                            val user = firebaseAuth.currentUser
                            Toast.makeText(
                                this@LoginActivity,
                                "Login successful",
                                Toast.LENGTH_SHORT
                            ).show()
                            //send user to next screen
                            val intent = Intent(this@LoginActivity, NewsActivity::class.java)
                            startActivity(intent)
                        }
                    })
                } else {
                    val exception: Exception = task.exception!!
                    Toast.makeText(
                        this,
                        "Login failed: $exception",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}