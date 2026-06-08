package com.hussein.varview.domain.repository

import com.hussein.varview.domain.model.AvatarDimensions

interface AvatarRepository {
    fun getDimensions(): AvatarDimensions
    fun saveDimensions(dimensions: AvatarDimensions)
}
