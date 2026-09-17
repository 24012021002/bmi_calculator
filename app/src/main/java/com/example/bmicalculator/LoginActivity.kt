package com.example.bmicalculator

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.btn_login).setOnClickListener {
            val email = findViewById<EditText>(R.id.et_email).text.toString()
            val pass = findViewById<EditText>(R.id.et_password).text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                startActivity(Intent(this, OnboardingActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Please enter credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }
}