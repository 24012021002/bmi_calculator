package com.example.bmicalculator

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        findViewById<Button>(R.id.btn_get_started).setOnClickListener {
            val name = findViewById<EditText>(R.id.et_user_name).text.toString()

            if (name.isNotEmpty()) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("USER_NAME", name)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            }
        }
    }
}