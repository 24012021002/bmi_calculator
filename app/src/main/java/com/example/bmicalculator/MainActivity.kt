package com.example.bmicalculator

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var etHeight: TextInputEditText
    private lateinit var etWeight: TextInputEditText
    private lateinit var etAge: TextInputEditText
    private lateinit var spinnerSex: AutoCompleteTextView
    private lateinit var spinnerActivity: AutoCompleteTextView
    private lateinit var tvBmiScore: TextView
    private lateinit var tvBmiCategory: TextView
    private lateinit var tvMaintenanceCalories: TextView
    private lateinit var tvHealthyWeightRange: TextView
    private lateinit var tvWeightComparison: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Views
        etHeight = findViewById(R.id.et_height)
        etWeight = findViewById(R.id.et_weight)
        etAge = findViewById(R.id.et_age)
        spinnerSex = findViewById(R.id.spinner_sex)
        spinnerActivity = findViewById(R.id.spinner_activity)
        tvBmiScore = findViewById(R.id.tv_bmi_score)
        tvBmiCategory = findViewById(R.id.tv_bmi_category)
        tvMaintenanceCalories = findViewById(R.id.tv_maintenance_calories)
        tvHealthyWeightRange = findViewById(R.id.tv_healthy_weight_range)
        tvWeightComparison = findViewById(R.id.tv_weight_comparison)
        val btnCalculate = findViewById<Button>(R.id.btn_calculate)

        setupDropdowns()
        setupListeners()

        btnCalculate.setOnClickListener {
            performCalculations()
        }
    }

    private fun setupListeners() {
        val textWatcher = object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                performCalculations(silent = true)
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        }

        etHeight.addTextChangedListener(textWatcher)
        etWeight.addTextChangedListener(textWatcher)
        etAge.addTextChangedListener(textWatcher)

        spinnerSex.setOnItemClickListener { _, _, _, _ -> performCalculations(silent = true) }
        spinnerActivity.setOnItemClickListener { _, _, _, _ -> performCalculations(silent = true) }
    }

    private fun setupDropdowns() {
        val sexOptions = arrayOf("Male", "Female")
        val sexAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, sexOptions)
        spinnerSex.setAdapter(sexAdapter)

        val activityOptions = arrayOf(
            "Sedentary (little or no exercise)",
            "Lightly active (exercise 1-3 days/week)",
            "Moderately active (exercise 3-5 days/week)",
            "Very active (hard exercise 6-7 days/week)",
            "Extra active (very hard exercise & physical job)"
        )
        val activityAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, activityOptions)
        spinnerActivity.setAdapter(activityAdapter)
    }

    private fun performCalculations(silent: Boolean = false) {
        val heightStr = etHeight.text.toString().trim()
        val weightStr = etWeight.text.toString().trim()
        val ageStr = etAge.text.toString().trim()
        val sex = spinnerSex.text.toString()
        val activity = spinnerActivity.text.toString()

        if (heightStr.isEmpty() || weightStr.isEmpty() || ageStr.isEmpty() || sex.isEmpty() || activity.isEmpty()) {
            if (!silent) Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            resetUi()
            return
        }

        val height = heightStr.toFloatOrNull() ?: 0f
        val weight = weightStr.toFloatOrNull() ?: 0f
        val age = ageStr.toIntOrNull() ?: 0

        if (height <= 50 || height > 250 || weight <= 2 || weight > 300 || age <= 0 || age > 120) {
            if (!silent) Toast.makeText(this, "Please enter realistic values", Toast.LENGTH_SHORT).show()
            resetUi()
            return
        }

        // 1. BMI Calculation
        val heightInMeters = height / 100f
        val bmi = weight / (heightInMeters * heightInMeters)
        displayBmi(bmi)

        // 2. Maintenance Calories (TDEE)
        val bmr = if (sex == "Male") {
            (10 * weight) + (6.25f * height) - (5 * age) + 5
        } else {
            (10 * weight) + (6.25f * height) - (5 * age) - 161
        }

        val activityFactor = when {
            activity.contains("Sedentary") -> 1.2f
            activity.contains("Lightly") -> 1.375f
            activity.contains("Moderately") -> 1.55f
            activity.contains("Very active") -> 1.725f
            else -> 1.9f
        }

        val tdee = bmr * activityFactor
        tvMaintenanceCalories.text = String.format(Locale.getDefault(), "%.0f kcal/day", tdee)

        // 3. Healthy Weight Range based on standard or customized Indian thresholds
        val minHealthyWeight: Float
        val maxHealthyWeight: Float

        val roundedHeight = Math.round(height)
        if (sex == "Female") {
            // Ideal Weight Chart for Indian Women
            when {
                roundedHeight <= 147 -> { minHealthyWeight = 40.0f; maxHealthyWeight = 44.6f + 4.9f } // 40.0 - 49.5 kg
                roundedHeight <= 150 -> { minHealthyWeight = 41.6f; maxHealthyWeight = 51.5f }
                roundedHeight <= 152 -> { minHealthyWeight = 42.8f; maxHealthyWeight = 52.9f }
                roundedHeight <= 155 -> { minHealthyWeight = 44.4f; maxHealthyWeight = 55.0f }
                roundedHeight <= 157 -> { minHealthyWeight = 45.6f; maxHealthyWeight = 56.5f }
                roundedHeight <= 160 -> { minHealthyWeight = 47.4f; maxHealthyWeight = 58.6f }
                roundedHeight <= 163 -> { minHealthyWeight = 49.1f; maxHealthyWeight = 60.8f }
                roundedHeight <= 165 -> { minHealthyWeight = 50.4f; maxHealthyWeight = 62.4f }
                roundedHeight <= 168 -> { minHealthyWeight = 52.2f; maxHealthyWeight = 64.6f }
                roundedHeight <= 170 -> { minHealthyWeight = 53.5f; maxHealthyWeight = 66.2f }
                roundedHeight <= 173 -> { minHealthyWeight = 55.4f; maxHealthyWeight = 68.5f }
                else -> { minHealthyWeight = 56.7f; maxHealthyWeight = 70.2f } // 175cm+
            }
        } else {
            // Ideal Weight Chart for Men / General Indian Thresholds
            when {
                roundedHeight <= 152 -> { minHealthyWeight = 42.8f; maxHealthyWeight = 52.9f }
                roundedHeight <= 155 -> { minHealthyWeight = 44.4f; maxHealthyWeight = 55.0f }
                roundedHeight <= 157 -> { minHealthyWeight = 45.6f; maxHealthyWeight = 56.5f }
                roundedHeight <= 160 -> { minHealthyWeight = 47.4f; maxHealthyWeight = 58.6f }
                roundedHeight <= 163 -> { minHealthyWeight = 49.1f; maxHealthyWeight = 60.8f }
                roundedHeight <= 165 -> { minHealthyWeight = 50.4f; maxHealthyWeight = 62.4f }
                roundedHeight <= 168 -> { minHealthyWeight = 52.2f; maxHealthyWeight = 64.6f }
                roundedHeight <= 170 -> { minHealthyWeight = 53.5f; maxHealthyWeight = 66.2f }
                roundedHeight <= 173 -> { minHealthyWeight = 55.4f; maxHealthyWeight = 68.5f }
                roundedHeight <= 175 -> { minHealthyWeight = 56.7f; maxHealthyWeight = 70.2f }
                roundedHeight <= 178 -> { minHealthyWeight = 58.6f; maxHealthyWeight = 72.5f }
                roundedHeight <= 180 -> { minHealthyWeight = 59.9f; maxHealthyWeight = 74.2f }
                roundedHeight <= 183 -> { minHealthyWeight = 62.0f; maxHealthyWeight = 76.7f }
                roundedHeight <= 185 -> { minHealthyWeight = 63.4f; maxHealthyWeight = 78.4f }
                else -> { minHealthyWeight = 65.4f; maxHealthyWeight = 81.0f } // 188cm+
            }
        }
        
        tvHealthyWeightRange.text = String.format(Locale.getDefault(), "%.1f kg - %.1f kg", minHealthyWeight, maxHealthyWeight)

        // 4. Weight Comparison
        when {
            weight < minHealthyWeight -> {
                val diff = minHealthyWeight - weight
                tvWeightComparison.text = String.format(Locale.getDefault(), "You are %.1f kg below the healthy range.", diff)
                tvWeightComparison.setTextColor(ContextCompat.getColor(this, R.color.accent_blue))
            }
            weight > maxHealthyWeight -> {
                val diff = weight - maxHealthyWeight
                tvWeightComparison.text = String.format(Locale.getDefault(), "You are %.1f kg above the healthy range.", diff)
                tvWeightComparison.setTextColor(ContextCompat.getColor(this, R.color.accent_pink))
            }
            else -> {
                tvWeightComparison.text = "You are within a healthy weight range."
                tvWeightComparison.setTextColor(ContextCompat.getColor(this, R.color.accent_green))
            }
        }

    }

    private fun resetUi() {
        tvBmiScore.text = "--"
        tvBmiCategory.text = "Enter data"
        tvBmiCategory.setTextColor(ContextCompat.getColor(this, R.color.text_secondary))
        tvMaintenanceCalories.text = "-- kcal/day"
        tvHealthyWeightRange.text = "-- kg"
        tvWeightComparison.text = ""
    }

    private fun displayBmi(bmi: Float) {
        tvBmiScore.text = String.format(Locale.getDefault(), "%.1f", bmi)
        
        val category: String
        val colorRes: Int

        when {
            bmi < 18.5f -> {
                category = "Underweight"
                colorRes = R.color.accent_blue
            }
            bmi < 25.0f -> {
                category = "Normal Weight"
                colorRes = R.color.accent_green
            }
            bmi < 30.0f -> {
                category = "Overweight"
                colorRes = R.color.accent_orange
            }
            else -> {
                category = "Obese"
                colorRes = R.color.accent_pink
            }
        }

        tvBmiCategory.text = category
        tvBmiCategory.setTextColor(ContextCompat.getColor(this, colorRes))
    }
}