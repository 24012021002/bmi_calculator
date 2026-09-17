package com.example.bmicalculator

import kotlin.math.roundToInt

class HealthCalculator(
    private val height: Float,
    private val weight: Float,
    private val age: Int,
    private val sex: String,
    private val activity: String
) {
    // 1. BMI Calculation
    fun calculateBmi(): Float {
        val heightInMeters = height / 100
        return weight / (heightInMeters * heightInMeters)
    }

    // 2. Healthy Weight Range (BMI 18.5 - 24.9)
    fun getHealthyWeightRange(): Pair<Float, Float> {
        val hm = height / 100
        return Pair(18.5f * (hm * hm), 24.9f * (hm * hm))
    }

    // 3. Maintenance Calories (TDEE)
    fun calculateMaintenanceCalories(): Int {
        // Mifflin-St Jeor Equation
        val bmr = if (sex == "Male") {
            (10 * weight) + (6.25f * height) - (5 * age) + 5
        } else {
            (10 * weight) + (6.25f * height) - (5 * age) - 161
        }

        val factor = when {
            activity.contains("Sedentary") -> 1.2f
            activity.contains("Light") -> 1.375f
            activity.contains("Moderate") -> 1.55f
            activity.contains("Active") -> 1.725f
            else -> 1.9f
        }
        return (bmr * factor).roundToInt()
    }

    // 4. Macro & Micro Nutrients
    fun getNutrientRequirements(tdee: Int): NutrientData {
        val protein = (weight * 1.2f).roundToInt() // 1.2g per kg
        val fats = (tdee * 0.25f / 9f).roundToInt() // 25% of calories
        val carbs = ((tdee - (protein * 4) - (fats * 9)) / 4f).roundToInt()
        
        val zinc = if (sex == "Male") 11f else 8f // mg per day
        val iron = if (sex == "Male") 8f else 18f // mg per day
        
        return NutrientData(protein, carbs, fats, zinc, iron)
    }
}

data class NutrientData(
    val protein: Int,
    val carbs: Int,
    val fats: Int,
    val zinc: Float,
    val iron: Float
)