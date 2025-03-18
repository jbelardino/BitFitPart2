package com.codepath.articlesearch

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "AddFoodActivity"
class AddFoodActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_food)

        var enterFood = findViewById<EditText>(R.id.enterFood)
        var enterCalories = findViewById<EditText>(R.id.enterCalories)
        var recordFoodButton = findViewById<Button>(R.id.recordFoodButton)

        recordFoodButton.setOnClickListener {
            Log.d(TAG, "ADDING FOOD: " + enterFood.text.toString())
            val resultIntent = Intent()
            resultIntent.putExtra("name", enterFood.text.toString())
            resultIntent.putExtra("calories", enterCalories.text.toString())
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}