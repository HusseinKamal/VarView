package com.hussein.varview.data.repository

import com.hussein.varview.R
import com.hussein.varview.domain.model.Category
import com.hussein.varview.domain.model.ClothingItem
import com.hussein.varview.domain.repository.WardrobeRepository

class WardrobeRepositoryImpl : WardrobeRepository {

    // Sample wardrobe data — replace modelUrl with actual .glb asset paths
    private val wardrobeItems = listOf(
        ClothingItem(
            id = "shirt_1",
            name = "Classic White Shirt",
            category = Category.SHIRTS,
            modelUrl = "models/shirt_white.glb",
            thumbnail = R.drawable.ic_shirt_white
        ),
        ClothingItem(
            id = "shirt_2",
            name = "Blue Formal Shirt",
            category = Category.SHIRTS,
            modelUrl = "models/shirt_blue.glb",
            thumbnail = R.drawable.ic_shirt_blue
        ),
        ClothingItem(
            id = "tshirt_1",
            name = "Black T-Shirt",
            category = Category.T_SHIRTS,
            modelUrl = "models/tshirt_black.glb",
            thumbnail = R.drawable.ic_tshirt_black
        ),
        ClothingItem(
            id = "tshirt_2",
            name = "Red Graphic Tee",
            category = Category.T_SHIRTS,
            modelUrl = "models/tshirt_red.glb",
            thumbnail = R.drawable.ic_tshirt_red
        ),
        ClothingItem(
            id = "trousers_1",
            name = "Dark Jeans",
            category = Category.TROUSERS,
            modelUrl = "models/jeans_dark.glb",
            thumbnail = R.drawable.ic_jeans_dark
        ),
        ClothingItem(
            id = "trousers_2",
            name = "Khaki Chinos",
            category = Category.TROUSERS,
            modelUrl = "models/chinos_khaki.glb",
            thumbnail = R.drawable.ic_chinos_khaki
        ),
        ClothingItem(
            id = "outerwear_1",
            name = "Leather Jacket",
            category = Category.OUTERWEAR,
            modelUrl = "models/jacket_leather.glb",
            thumbnail = R.drawable.ic_jacket_leather
        ),
        ClothingItem(
            id = "outerwear_2",
            name = "Hoodie Grey",
            category = Category.OUTERWEAR,
            modelUrl = "models/hoodie_grey.glb",
            thumbnail = R.drawable.ic_hoodie_grey
        )
    )

    override fun getAllItems(): List<ClothingItem> = wardrobeItems

    override fun getItemsByCategory(category: Category): List<ClothingItem> =
        wardrobeItems.filter { it.category == category }

    override fun getItemById(id: String): ClothingItem? =
        wardrobeItems.find { it.id == id }
}
