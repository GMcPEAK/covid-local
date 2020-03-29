package com.example.covid_local

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.MultiAutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity


class SignupActivity : AppCompatActivity() {
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var location: MultiAutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val countries = arrayOf(
            "Belgium", "France", "Italy", "Germany", "Spain"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line, countries
        )
        location = findViewById<MultiAutoCompleteTextView>(R.id.enter_location)
        location.setAdapter(adapter)
        location.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
    }
}