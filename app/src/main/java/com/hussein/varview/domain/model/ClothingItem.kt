package com.hussein.varview.domain.model

data class ClothingItem(
    val id: String,
    val name: String,
    val category: Category,
    val modelUrl: String, // Path to .glb or .gltf file
    val thumbnail: Int // Drawable resource ID
)

enum class Category {
    SHIRTS,
    T_SHIRTS,
    TROUSERS,
    OUTERWEAR
}
