package com.example.bmicalculator

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.MaterialToolbar

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        findViewById<MaterialToolbar>(R.id.toolbar_summary).setNavigationOnClickListener { finish() }

        // Get data from MainActivity
        val h = intent.getFloatExtra("HEIGHT", 0f)
        val w = intent.getFloatExtra("WEIGHT", 0f)
        val a = intent.getIntExtra("AGE", 0)
        val s = intent.getStringExtra("SEX") ?: "Male"
        val act = intent.getStringExtra("ACTIVITY") ?: "Moderate"

        val calc = HealthCalculator(h, w, a, s, act)
        val bmi = calc.calculateBmi()

        val tvBmi = findViewById<TextView>(R.id.tv_summary_bmi)
        val tvCat = findViewById<TextView>(R.id.tv_summary_cat)
        
        tvBmi.text = "%.1f".format(bmi)
        if (bmi < 18.5) {
            tvCat.text = "Underweight"
            tvCat.setTextColor(ContextCompat.getColor(this, R.color.accent_blue))
        } else if (bmi < 25.0) {
            tvCat.text = "Normal"
            tvCat.setTextColor(ContextCompat.getColor(this, R.color.accent_green))
        } else if (bmi < 30.0) {
            tvCat.text = "Overweight"
            tvCat.setTextColor(ContextCompat.getColor(this, R.color.accent_orange))
        } else {
            tvCat.text = "Obese"
            tvCat.setTextColor(ContextCompat.getColor(this, R.color.accent_pink))
        }

        // Navigation to Page 4: Calorie Plan
        findViewById<Button>(R.id.btn_view_calories).setOnClickListener {
            val intent = Intent(this, CaloriePlanActivity::class.java).apply {
                putExtras(this@ResultActivity.intent.extras!!)
            }
            startActivity(intent)
        }

        // Navigation to Page 5: Nutrition Guide
        findViewById<Button>(R.id.btn_view_nutrients).setOnClickListener {
            val intent = Intent(this, NutritionActivity::class.java).apply {
                putExtras(this@ResultActivity.intent.extras!!)
            }
            startActivity(intent)
        }

        // Navigation to Page 6: Health Tips
        findViewById<Button>(R.id.btn_view_tips).setOnClickListener {
            startActivity(Intent(this, HealthTipsActivity::class.java))
        }
    }
}