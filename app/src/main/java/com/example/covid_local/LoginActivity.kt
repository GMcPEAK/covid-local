package com.example.covid_local

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


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
                    preferences
                        .edit()
                        .putString("email", inputtedEmail)
                        .putString("password", inputtedPassword)
                            //TODO: putString country from Firebase DB
                        .apply()
                    val user = firebaseAuth.currentUser
                    Toast.makeText(
                        this,
                        "Login successful",
                        Toast.LENGTH_SHORT
                    ).show()
                    //TODO: send user to next screen
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