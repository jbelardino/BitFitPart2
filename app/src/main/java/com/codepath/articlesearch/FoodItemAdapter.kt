package com.codepath.articlesearch

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "FoodItemAdapter"
class FoodItemAdapter(private val context: Context, private val foodItems: List<FoodItem>) :
    RecyclerView.Adapter<FoodItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.food_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Get the individual foodItem and bind to holder
        val foodItem = foodItems[position]
        holder.bind(foodItem)
    }

    override fun getItemCount() = foodItems.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val foodItemNameTextView = itemView.findViewById<TextView>(R.id.foodItemName)
        private val foodItemCaloriesTextView = itemView.findViewById<TextView>(R.id.foodItemCalories)

        //Write a helper method to help set up the onBindViewHolder method
        fun bind(foodItem: FoodItem) {
            foodItemNameTextView.text = foodItem.name
            foodItemCaloriesTextView.text = foodItem.calories
        }
    }
}