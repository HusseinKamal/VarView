package com.hussein.varview.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.hussein.varview.domain.model.AvatarDimensions
import com.hussein.varview.domain.model.Category
import com.hussein.varview.domain.model.ClothingItem
import io.github.sceneview.SceneView
import io.github.sceneview.math.Position
import io.github.sceneview.math.Scale
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironment
import io.github.sceneview.rememberModelInstance
import io.github.sceneview.rememberModelLoader

/**
 * 3D Avatar renderer using SceneView 4.x Compose API.
 * Displays the avatar with dynamically applied dimensions and
 * equipped clothing items as child model nodes.
 *
 * Nodes are composable functions inside the SceneView DSL.
 * The Compose runtime handles recomposition when selected items or dimensions change.
 */
@Composable
fun AvatarRenderer(
    selectedItems: Map<Category, ClothingItem>,
    dimensions: AvatarDimensions,
    modifier: Modifier = Modifier
) {
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val cameraManipulator = rememberCameraManipulator()
    val environment = rememberEnvironment(engine)

    // Calculate scale from dimensions (normalized to defaults)
    val scaleX = dimensions.shoulderWidth / 0.5f
    val scaleY = dimensions.height / 1.7f
    val scaleZ = dimensions.waistWidth / 0.4f

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F8))
    ) {
        SceneView(
            modifier = Modifier.fillMaxSize(),
            engine = engine,
            modelLoader = modelLoader,
            cameraManipulator = cameraManipulator,
            environment = environment
        ) {
            // Wrap avatar + clothes in a parent Node that handles scaling
            Node(scale = Scale(scaleX, scaleY, scaleZ)) {
                // Load and display avatar base model
                rememberModelInstance(modelLoader, "models/avatar.glb")?.let { avatarInstance ->
                    ModelNode(
                        modelInstance = avatarInstance,
                        scaleToUnits = 1.7f,
                        autoAnimate = true
                    )
                }

                // Load and display each selected clothing item
                selectedItems.values.forEach { item ->
                    rememberModelInstance(modelLoader, item.modelUrl)?.let { clothingInstance ->
                        // Position clothing based on category using a parent Node
                        Node(
                            position = when (item.category) {
                                Category.SHIRTS, Category.T_SHIRTS, Category.OUTERWEAR ->
                                    Position(0f, 1.2f, 0f) // Upper body (chest)
                                Category.TROUSERS ->
                                    Position(0f, 0.6f, 0f) // Lower body (hips)
                            }
                        ) {
                            ModelNode(
                                modelInstance = clothingInstance,
                                autoAnimate = true
                            )
                        }
                    }
                }
            }
        }
    }
}
