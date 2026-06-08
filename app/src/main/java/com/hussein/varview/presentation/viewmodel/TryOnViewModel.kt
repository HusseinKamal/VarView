package com.hussein.varview.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.hussein.varview.data.repository.AvatarRepositoryImpl
import com.hussein.varview.data.repository.WardrobeRepositoryImpl
import com.hussein.varview.domain.model.AvatarDimensions
import com.hussein.varview.domain.model.AvatarTexture
import com.hussein.varview.domain.model.Category
import com.hussein.varview.domain.model.ClothingItem
import com.hussein.varview.domain.model.TextureTarget
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

    // Avatar face/body texture from camera or gallery
    private val _avatarTexture = MutableStateFlow<AvatarTexture?>(null)
    val avatarTexture: StateFlow<AvatarTexture?> = _avatarTexture.asStateFlow()

    // Controls whether the image picker dialog is shown
    private val _showImagePicker = MutableStateFlow(false)
    val showImagePicker: StateFlow<Boolean> = _showImagePicker.asStateFlow()

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

    // Image capture / selection
    fun showImagePicker() {
        _showImagePicker.value = true
    }

    fun dismissImagePicker() {
        _showImagePicker.value = false
    }

    fun setAvatarImage(uri: Uri, target: TextureTarget = TextureTarget.FACE) {
        _avatarTexture.value = AvatarTexture(uri = uri, target = target)
        _showImagePicker.value = false
    }

    fun clearAvatarImage() {
        _avatarTexture.value = null
    }

    private fun loadWardrobeItems() {
        _wardrobeItems.value = getWardrobeItemsUseCase(_activeCategory.value)
    }
}
