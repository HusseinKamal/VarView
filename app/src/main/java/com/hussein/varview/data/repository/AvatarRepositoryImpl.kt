package com.hussein.varview.data.repository

import com.hussein.varview.domain.model.AvatarDimensions
import com.hussein.varview.domain.repository.AvatarRepository

class AvatarRepositoryImpl : AvatarRepository {

    private var currentDimensions = AvatarDimensions()

    override fun getDimensions(): AvatarDimensions = currentDimensions

    override fun saveDimensions(dimensions: AvatarDimensions) {
        currentDimensions = dimensions
    }
}
