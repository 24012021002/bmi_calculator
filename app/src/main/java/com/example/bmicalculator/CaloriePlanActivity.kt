package com.example.bmicalculator

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class CaloriePlanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calorie_plan)

        findViewById<MaterialToolbar>(R.id.toolbar_calories).setNavigationOnClickListener { finish() }

        val h = intent.getFloatExtra("HEIGHT", 0f)
        val w = intent.getFloatExtra("WEIGHT", 0f)
        val a = intent.getIntExtra("AGE", 0)
        val s = intent.getStringExtra("SEX") ?: "Male"
        val act = intent.getStringExtra("ACTIVITY") ?: "Moderate"

        val calc = HealthCalculator(h, w, a, s, act)
        val cal = calc.calculateMaintenanceCalories()

        findViewById<TextView>(R.id.tv_plan_maintain).text = "$cal kcal"
        findViewById<TextView>(R.id.tv_plan_mild).text = "${cal - 250} kcal"
        findViewById<TextView>(R.id.tv_plan_standard).text = "${cal - 500} kcal"
        findViewById<TextView>(R.id.tv_plan_extreme).text = "${cal - 1000} kcal"
    }
}