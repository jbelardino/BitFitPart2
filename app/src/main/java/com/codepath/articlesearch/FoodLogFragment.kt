package com.codepath.articlesearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

private const val TAG = "FoodLogFragment"
class FoodLogFragment : Fragment() {
    private val foodItems = mutableListOf<FoodItem>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var foodItemAdapter: FoodItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_foodlog, container, false)
        val context = view.context

        recyclerView = view.findViewById(R.id.foodItemList)
        foodItemAdapter = FoodItemAdapter(context, foodItems)
        recyclerView.adapter = foodItemAdapter

        recyclerView.layoutManager = LinearLayoutManager(context).also {
            val dividerItemDecoration = DividerItemDecoration(context, it.orientation)
            recyclerView.addItemDecoration(dividerItemDecoration)
        }
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
                    foodItemAdapter.notifyDataSetChanged()
                }
            }
        }

        return view
    }
}