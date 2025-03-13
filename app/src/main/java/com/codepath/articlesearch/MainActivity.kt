package com.codepath.articlesearch

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.articlesearch.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    private val foodItems = mutableListOf<FoodItem>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: ActivityMainBinding
    private lateinit var foodItemAdapter: FoodItemAdapter

    private val getResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            // Handle the data returned
            val name = result.data?.getStringExtra("name")
            val calories = result.data?.getStringExtra("calories")
            val newFoodItem = FoodItem(name, calories)

            foodItems.add(newFoodItem)
            //Log.d(TAG, "foodItems: " + foodItems)

            //save the items
            foodItems.let { list ->
                lifecycleScope.launch(IO) {
                    (application as FoodApplication).db.FoodItemDao().deleteAll()
                    (application as FoodApplication).db.FoodItemDao().insertAll(list.map {
                        FoodEntity(
                            name = it.name,
                            calories = it.calories
                        )
                    })
                }
            }
            //foodItemAdapter.notifyDataSetChanged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        //setting up recycler view
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        recyclerView = findViewById(R.id.foodItemList)
        foodItemAdapter = FoodItemAdapter(this, foodItems)
        recyclerView.adapter = foodItemAdapter

        recyclerView.layoutManager = LinearLayoutManager(this).also {
            val dividerItemDecoration = DividerItemDecoration(this, it.orientation)
            recyclerView.addItemDecoration(dividerItemDecoration)
        }

        //setting up the database
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
                    Log.d("DATABASE", foodItems.toString())
                    foodItemAdapter.notifyDataSetChanged()
                }
            }
        }

        //button
        val addFoodButton = findViewById<Button>(R.id.addFoodButton)
        addFoodButton.setOnClickListener {
            Log.d(TAG, "Adding a new food")
            val intent = Intent(this, AddFoodActivity::class.java)
            getResultLauncher.launch(intent)
            //foodItemAdapter.notifyDataSetChanged()
        }
    }
}