package com.example.bmicalculator

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class NutritionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nutrition)

        findViewById<MaterialToolbar>(R.id.toolbar_nutrition).setNavigationOnClickListener { finish() }

        val h = intent.getFloatExtra("HEIGHT", 0f)
        val w = intent.getFloatExtra("WEIGHT", 0f)
        val a = intent.getIntExtra("AGE", 0)
        val s = intent.getStringExtra("SEX") ?: "Male"
        val act = intent.getStringExtra("ACTIVITY") ?: "Moderate"

        val calc = HealthCalculator(h, w, a, s, act)
        val tdee = calc.calculateMaintenanceCalories()
        val data = calc.getNutrientRequirements(tdee)

        findViewById<TextView>(R.id.tv_n_protein).text = "${data.protein} g"
        findViewById<TextView>(R.id.tv_n_carbs).text = "${data.carbs} g"
        findViewById<TextView>(R.id.tv_n_fats).text = "${data.fats} g"
        findViewById<TextView>(R.id.tv_n_zinc).text = "%.1f mg".format(data.zinc)
        findViewById<TextView>(R.id.tv_n_iron).text = "%.1f mg".format(data.iron)
    }
}