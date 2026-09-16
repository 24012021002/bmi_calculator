package com.example.bmicalculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import java.util.Locale

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
        // Handle Window Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0) // keep bottom zero for seamless nav bar
            insets
        }

        // Initialize Main Screens / Views
        val layoutHome = findViewById<View>(R.id.layout_home)
        val layoutReport = findViewById<View>(R.id.layout_report)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val btnBackReport = findViewById<TextView>(R.id.btn_back_report)

        // Initialize BMI Components
        val etHeight = findViewById<TextInputEditText>(R.id.et_height)
        val etWeight = findViewById<TextInputEditText>(R.id.et_weight)
        val btnCalculateBmi = findViewById<Button>(R.id.btn_calculate_bmi)
        val tvBmiResult = findViewById<TextView>(R.id.tv_bmi_result)

        // Bottom Navigation Controller Setup
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    layoutHome.visibility = View.VISIBLE
                    layoutReport.visibility = View.GONE
                    true
                }
                R.id.nav_report -> {
                    layoutHome.visibility = View.GONE
                    layoutReport.visibility = View.VISIBLE
                    true
                }
                R.id.nav_schedule, R.id.nav_notification, R.id.nav_profile -> {
                    Toast.makeText(this, "${item.title} Dashboard clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        // Back action from Report Screen to Dashboard
        btnBackReport.setOnClickListener {
            layoutHome.visibility = View.VISIBLE
            layoutReport.visibility = View.GONE
            bottomNavigation.selectedItemId = R.id.nav_home
        }

        // BMI Calculation logic
        btnCalculateBmi.setOnClickListener {
            val heightStr = etHeight.text?.toString()?.trim()
            val weightStr = etWeight.text?.toString()?.trim()

            if (heightStr.isNullOrEmpty() || weightStr.isNullOrEmpty()) {
                Toast.makeText(this, "Please enter valid height and weight inputs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val heightCm = heightStr.toFloat()
                val weightKg = weightStr.toFloat()

                if (heightCm <= 0 || weightKg <= 0) {
                    Toast.makeText(this, "Values must be greater than zero", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Compute Body Mass Index
                val heightMeters = heightCm / 100f
                val bmiScore = weightKg / (heightMeters * heightMeters)

                // Format score and determine health category status
                val resultText: String
                val textColorRes: Int

                when {
                    bmiScore < 18.5f -> {
                        resultText = String.format(Locale.getDefault(), "%.1f (Underweight)", bmiScore)
                        textColorRes = R.color.accent_blue
                    }
                    bmiScore in 18.5f..24.9f -> {
                        resultText = String.format(Locale.getDefault(), "%.1f (Normal)", bmiScore)
                        textColorRes = R.color.accent_green
                    }
                    bmiScore in 25.0f..29.9f -> {
                        resultText = String.format(Locale.getDefault(), "%.1f (Overweight)", bmiScore)
                        textColorRes = R.color.accent_orange
                    }
                    else -> {
                        resultText = String.format(Locale.getDefault(), "%.1f (Obese)", bmiScore)
                        textColorRes = R.color.accent_pink
                    }
                }

                tvBmiResult.text = resultText
                tvBmiResult.setTextColor(ContextCompat.getColor(this, textColorRes))

            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Invalid numeric input format", Toast.LENGTH_SHORT).show()
            }
        }
    }
}