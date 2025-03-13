package com.codepath.articlesearch

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.articlesearch.databinding.ActivityMainBinding

private const val TAG = "AddFoodActivity"

class AddFoodActivity: AppCompatActivity(){
//    private val foodItems = mutableListOf<FoodItem>()
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_food)

        var enterFood = findViewById<EditText>(R.id.enterFood)
        var enterCalories = findViewById<EditText>(R.id.enterCalories)
        var recordFoodButton = findViewById<Button>(R.id.recordFoodButton)

        recordFoodButton.setOnClickListener {
            Log.d(TAG, "ADD FOOD: " + enterFood.text.toString())

            //val newFoodItem = FoodItem(name, calories)
            //foodItems.add(newFoodItem)


//            binding = ActivityMainBinding.inflate(layoutInflater)
//            val view = binding.root
//            setContentView(view)
//
//            recyclerView = findViewById(R.id.foodItemList)
//
//            //Set up FoodItemAdapter with articles
//            val foodItemAdapter = FoodItemAdapter(this, foodItems)
//            recyclerView.adapter = foodItemAdapter
//
//            recyclerView.layoutManager = LinearLayoutManager(this).also {
//                val dividerItemDecoration = DividerItemDecoration(this, it.orientation)
//                recyclerView.addItemDecoration(dividerItemDecoration)
//            }
//
//
//
//
//            foodItemAdapter.notifyDataSetChanged()




            val resultIntent = Intent()
            resultIntent.putExtra("name", enterFood.text.toString())
            resultIntent.putExtra("calories", enterCalories.text.toString())
            setResult(RESULT_OK, resultIntent)
            finish()
        }




    }
}