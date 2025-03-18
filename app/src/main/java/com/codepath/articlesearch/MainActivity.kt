package com.codepath.articlesearch

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.articlesearch.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    private val foodItems = mutableListOf<FoodItem>()
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
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        foodItemAdapter = FoodItemAdapter(this, foodItems)

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
                    //Log.d("DATABASE", foodItems.toString())
                    foodItemAdapter.notifyDataSetChanged()
                }
            }
        }

        //button
        val addFoodButton = view.findViewById<Button>(R.id.addFoodButton)
        addFoodButton.setOnClickListener {
            Log.d(TAG, "Adding a new food")
            val intent = Intent(this, AddFoodActivity::class.java)
            getResultLauncher.launch(intent)
        }

        //Fragment handling
        val foodLogFragment: Fragment = FoodLogFragment()
        val dashboardFragment: Fragment = DashboardFragment()
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            lateinit var fragment: Fragment
            when (item.itemId) {
                R.id.nav_foodLog -> fragment = foodLogFragment
                R.id.nav_dashboard -> fragment = dashboardFragment
            }
            replaceFragment(fragment)
            true
        }

        // Set default selection
        bottomNavigationView.selectedItemId = R.id.nav_foodLog
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}