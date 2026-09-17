package com.example.bmicalculator

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Dropdown setup
        val sex = findViewById<AutoCompleteTextView>(R.id.spinner_sex)
        sex.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, HealthConstants.SEX_OPTIONS))
        
        val act = findViewById<AutoCompleteTextView>(R.id.spinner_activity)
        act.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, HealthConstants.ACTIVITY_OPTIONS))

        findViewById<Button>(R.id.btn_calculate).setOnClickListener {
            val hStr = findViewById<EditText>(R.id.et_height).text.toString()
            val wStr = findViewById<EditText>(R.id.et_weight).text.toString()
            val aStr = findViewById<EditText>(R.id.et_age).text.toString()
            val s = sex.text.toString()
            val activity = act.text.toString()

            if (hStr.isEmpty() || wStr.isEmpty() || aStr.isEmpty() || s.isEmpty() || activity.isEmpty()) {
                Toast.makeText(this, "Please fill in all details", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val h = hStr.toFloat()
            val w = wStr.toFloat()
            val a = aStr.toInt()

            // Navigate to Result Page
            val intent = Intent(this, ResultActivity::class.java).apply {
                putExtra("HEIGHT", h)
                putExtra("WEIGHT", w)
                putExtra("AGE", a)
                putExtra("SEX", s)
                putExtra("ACTIVITY", activity)
            }
            startActivity(intent)
        }
    }
}