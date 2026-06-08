package com.hussein.varview.domain.usecase

import com.hussein.varview.domain.model.AvatarDimensions
import com.hussein.varview.domain.repository.AvatarRepository

class UpdateAvatarDimensionsUseCase(
    private val repository: AvatarRepository
) {
    operator fun invoke(dimensions: AvatarDimensions) {
        repository.saveDimensions(dimensions)
    }
}
