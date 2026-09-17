package com.example.bmicalculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class HydrationActivity : AppCompatActivity() {
    private var currentIntake = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hydration)

        findViewById<MaterialToolbar>(R.id.toolbar_hydration).setNavigationOnClickListener { finish() }

        val tvGoal = findViewById<TextView>(R.id.tv_water_goal)
        
        findViewById<Button>(R.id.btn_add_glass).setOnClickListener {
            currentIntake += 250
            updateUI(tvGoal)
        }

        findViewById<Button>(R.id.btn_add_bottle).setOnClickListener {
            currentIntake += 500
            updateUI(tvGoal)
        }
    }

    private fun updateUI(tv: TextView) {
        val liters = currentIntake / 1000f
        tv.text = "%.2f Liters".format(liters)
        if (currentIntake >= 2500) {
            Toast.makeText(this, "Daily Goal Reached!", Toast.LENGTH_SHORT).show()
        }
    }
}