package com.codepath.articlesearch

import java.io.Serializable

data class FoodItem(
    val name: String?,
    val calories: String?
) : Serializable