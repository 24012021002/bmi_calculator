package com.example.bmicalculator

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.MaterialToolbar

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // Setup Toolbar with Back Button
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar_result)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        // Get Data from Intent
        val h = intent.getFloatExtra("HEIGHT", 0f)
        val w = intent.getFloatExtra("WEIGHT", 0f)
        val a = intent.getIntExtra("AGE", 0)
        val s = intent.getStringExtra("SEX") ?: "Male"
        val act = intent.getStringExtra("ACTIVITY") ?: "Moderate"

        // Use HealthCalculator
        val calc = HealthCalculator(h, w, a, s, act)
        val bmi = calc.calculateBmi()
        val range = calc.getHealthyWeightRange()
        val mainCal = calc.calculateMaintenanceCalories()
        val nutrients = calc.getNutrientRequirements(mainCal)

        // 1. Update BMI
        val tvBmi = findViewById<TextView>(R.id.tv_bmi_score)
        val tvCat = findViewById<TextView>(R.id.tv_bmi_category)
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

        // 2. Update Calories
        findViewById<TextView>(R.id.tv_maintenance_calories).text = "%d kcal".format(mainCal)
        findViewById<TextView>(R.id.tv_mild_loss).text = "%d kcal".format(mainCal - 250)
        findViewById<TextView>(R.id.tv_weight_loss).text = "%d kcal".format(mainCal - 500)
        findViewById<TextView>(R.id.tv_extreme_loss).text = "%d kcal".format(mainCal - 1000)

        // 3. Update Nutrients
        findViewById<TextView>(R.id.tv_protein).text = "%d g".format(nutrients.protein)
        findViewById<TextView>(R.id.tv_carbs).text = "%d g".format(nutrients.carbs)
        findViewById<TextView>(R.id.tv_fats).text = "%d g".format(nutrients.fats)
        findViewById<TextView>(R.id.tv_zinc).text = "%.1f mg".format(nutrients.zinc)
        findViewById<TextView>(R.id.tv_iron).text = "%.1f mg".format(nutrients.iron)

        // 4. Update Range
        findViewById<TextView>(R.id.tv_healthy_weight_range).text = "%.1f - %.1f kg".format(range.first, range.second)
        val tvMsg = findViewById<TextView>(R.id.tv_weight_comparison)
        if (w > range.second) {
            tvMsg.text = "You are above healthy range."
            tvMsg.setTextColor(ContextCompat.getColor(this, R.color.accent_pink))
        } else if (w < range.first) {
            tvMsg.text = "You are below healthy range."
            tvMsg.setTextColor(ContextCompat.getColor(this, R.color.accent_blue))
        } else {
            tvMsg.text = "You are in healthy range!"
            tvMsg.setTextColor(ContextCompat.getColor(this, R.color.accent_green))
        }
    }
}