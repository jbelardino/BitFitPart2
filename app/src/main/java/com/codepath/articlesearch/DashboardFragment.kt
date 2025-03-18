package com.codepath.articlesearch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "DashboardFragment"
class DashboardFragment : Fragment() {
    private val foodItems = mutableListOf<FoodItem>()
    private lateinit var foodItemAdapter: FoodItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val context = view.context

        val averageCalories = view.findViewById<TextView>(R.id.averageCalories)
        val minimumCalories = view.findViewById<TextView>(R.id.minimumCalories)
        val maximumCalories = view.findViewById<TextView>(R.id.maximumCalories)

        foodItemAdapter = FoodItemAdapter(context, foodItems)

        val application = requireActivity().application

        lifecycleScope.launch {
            (application as FoodApplication).db.FoodItemDao().getAll().collect { databaseList ->
                databaseList.map { entity ->
                    FoodItem(
                        entity.name,
                        entity.calories
                    )
                }.also { mappedList ->
                    foodItems.clear()
                    foodItems.addAll(mappedList)
                    if(foodItems.size > 0) {
                        updateDashboard(averageCalories, minimumCalories, maximumCalories)
                    }
                }
            }
        }

        //Clear button
        val clearDataButton = view.findViewById<Button>(R.id.clearDataButton)
        clearDataButton.setOnClickListener {
            Log.d(TAG, "Clearing data")
            lifecycleScope.launch(Dispatchers.IO) {
                (application as FoodApplication).db.FoodItemDao().deleteAll()
            }
            averageCalories.text = "0"
            minimumCalories.text = "0"
            maximumCalories.text = "0"
        }

        return view
    }

    fun updateDashboard(averageCaloriesView: TextView, minimumCaloriesView: TextView, maximumCaloriesView: TextView){
        var averageCalories = 0.0
        var minimumCalories = foodItems[0].calories?.toInt()
        var maximumCalories = foodItems[0].calories?.toInt()

        for (item in foodItems) {
            val calories = item.calories?.toInt()
            if (calories != null) {
                averageCalories += calories
                if(calories < minimumCalories!!){
                    minimumCalories = calories
                }
                if(calories > maximumCalories!!){
                    maximumCalories = calories
                }
            }
        }
        averageCalories /= foodItems.size
        val formattedAverage = String.format("%.2f", averageCalories)

        averageCaloriesView.text = formattedAverage
        minimumCaloriesView.text = minimumCalories.toString()
        maximumCaloriesView.text = maximumCalories.toString()
    }
}