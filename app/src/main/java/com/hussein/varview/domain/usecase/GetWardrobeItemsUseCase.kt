package com.hussein.varview.domain.usecase

import com.hussein.varview.domain.model.Category
import com.hussein.varview.domain.model.ClothingItem
import com.hussein.varview.domain.repository.WardrobeRepository

class GetWardrobeItemsUseCase(
    private val repository: WardrobeRepository
) {
    operator fun invoke(category: Category? = null): List<ClothingItem> {
        return if (category != null) {
            repository.getItemsByCategory(category)
        } else {
            repository.getAllItems()
        }
    }
}
