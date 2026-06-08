package com.hussein.varview.domain.repository

import com.hussein.varview.domain.model.Category
import com.hussein.varview.domain.model.ClothingItem

interface WardrobeRepository {
    fun getAllItems(): List<ClothingItem>
    fun getItemsByCategory(category: Category): List<ClothingItem>
    fun getItemById(id: String): ClothingItem?
}
