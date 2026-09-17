package com.example.bmicalculator

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class WorkoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)

        findViewById<MaterialToolbar>(R.id.toolbar_workout).setNavigationOnClickListener { finish() }

        val h = intent.getFloatExtra("HEIGHT", 0f)
        val w = intent.getFloatExtra("WEIGHT", 0f)
        val a = intent.getIntExtra("AGE", 0)
        val s = intent.getStringExtra("SEX") ?: "Male"
        val act = intent.getStringExtra("ACTIVITY") ?: "Moderate"

        val calc = HealthCalculator(h, w, a, s, act)
        val bmi = calc.calculateBmi()

        val tvRec = findViewById<TextView>(R.id.tv_workout_recommendation)

        when {
            bmi < 18.5 -> {
                tvRec.text = "Focus on muscle-building (Hypertrophy) to reach a healthy weight. Try bodyweight exercises like push-ups and squats, and ensure a high-protein diet."
            }
            bmi < 25.0 -> {
                tvRec.text = "Your weight is healthy! Focus on maintaining fitness with a mix of cardio and strength training 3-4 times a week."
            }
            bmi < 30.0 -> {
                tvRec.text = "Focus on consistent cardio (walking, swimming) and moderate intensity interval training (HIIT) to reduce body fat while maintaining muscle."
            }
            else -> {
                tvRec.text = "Prioritize low-impact activities like brisk walking or swimming to protect your joints while consistently burning calories."
            }
        }
    }
}