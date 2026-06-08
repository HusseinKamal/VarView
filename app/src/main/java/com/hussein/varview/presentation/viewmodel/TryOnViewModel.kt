package com.hussein.varview.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.hussein.varview.data.repository.AvatarRepositoryImpl
import com.hussein.varview.data.repository.WardrobeRepositoryImpl
import com.hussein.varview.domain.model.AvatarDimensions
import com.hussein.varview.domain.model.Category
import com.hussein.varview.domain.model.ClothingItem
import com.hussein.varview.domain.repository.WardrobeRepository
import com.hussein.varview.domain.usecase.GetWardrobeItemsUseCase
import com.hussein.varview.domain.usecase.UpdateAvatarDimensionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TryOnViewModel : ViewModel() {

    private val wardrobeRepository: WardrobeRepository = WardrobeRepositoryImpl()
    private val avatarRepository = AvatarRepositoryImpl()

    private val getWardrobeItemsUseCase = GetWardrobeItemsUseCase(wardrobeRepository)
    private val updateAvatarDimensionsUseCase = UpdateAvatarDimensionsUseCase(avatarRepository)

    private val _selectedItems = MutableStateFlow<Map<Category, ClothingItem>>(emptyMap())
    val selectedItems: StateFlow<Map<Category, ClothingItem>> = _selectedItems.asStateFlow()

    private val _avatarDimensions = MutableStateFlow(AvatarDimensions())
    val avatarDimensions: StateFlow<AvatarDimensions> = _avatarDimensions.asStateFlow()

    private val _activeCategory = MutableStateFlow(Category.SHIRTS)
    val activeCategory: StateFlow<Category> = _activeCategory.asStateFlow()

    private val _wardrobeItems = MutableStateFlow<List<ClothingItem>>(emptyList())
    val wardrobeItems: StateFlow<List<ClothingItem>> = _wardrobeItems.asStateFlow()

    private val _isARMode = MutableStateFlow(false)
    val isARMode: StateFlow<Boolean> = _isARMode.asStateFlow()

    init {
        loadWardrobeItems()
    }

    fun selectItem(item: ClothingItem) {
        _selectedItems.value = _selectedItems.value + (item.category to item)
    }

    fun removeItem(category: Category) {
        _selectedItems.value = _selectedItems.value - category
    }

    fun updateDimensions(newDimensions: AvatarDimensions) {
        _avatarDimensions.value = newDimensions
        updateAvatarDimensionsUseCase(newDimensions)
    }

    fun setCategory(category: Category) {
        _activeCategory.value = category
        loadWardrobeItems()
    }

    fun toggleARMode() {
        _isARMode.value = !_isARMode.value
    }

    fun setARMode(enabled: Boolean) {
        _isARMode.value = enabled
    }

    private fun loadWardrobeItems() {
        _wardrobeItems.value = getWardrobeItemsUseCase(_activeCategory.value)
    }
}
