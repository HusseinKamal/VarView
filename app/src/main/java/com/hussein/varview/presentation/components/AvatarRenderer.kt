package com.hussein.varview.presentation.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.hussein.varview.domain.model.AvatarDimensions
import com.hussein.varview.domain.model.AvatarTexture
import com.hussein.varview.domain.model.Category
import com.hussein.varview.domain.model.ClothingItem
import com.hussein.varview.domain.model.TextureTarget
import io.github.sceneview.SceneView
import io.github.sceneview.math.Position
import io.github.sceneview.math.Scale
import io.github.sceneview.math.Size
import io.github.sceneview.node.ImageNode
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironment
import io.github.sceneview.rememberMainLightNode
import io.github.sceneview.rememberModelInstance
import io.github.sceneview.rememberModelLoader

/**
 * 3D Avatar renderer — full body human avatar centered in view.
 * Dark gray background like a studio setting.
 * Avatar fits the full height of the section, scaled to unit = 1.
 */
@Composable
fun AvatarRenderer(
    selectedItems: Map<Category, ClothingItem>,
    dimensions: AvatarDimensions,
    avatarTexture: AvatarTexture? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)

    // Camera pulled back so avatar appears at natural size, not oversized
    val cameraManipulator = rememberCameraManipulator(
        orbitHomePosition = Position(0f, 0.5f, 2.5f),
        targetPosition = Position(0f, 0.5f, 0f)
    )
    val environment = rememberEnvironment(engine)
    val mainLightNode = rememberMainLightNode(engine) {
        intensity = 150_000.0f
    }

    // Body dimension scale factors
    val scaleX = dimensions.shoulderWidth / 0.5f
    val scaleY = dimensions.height / 1.7f
    val scaleZ = dimensions.waistWidth / 0.4f

    // Load user photo bitmap
    val textureBitmap: Bitmap? = remember(avatarTexture?.uri) {
        avatarTexture?.uri?.let { uri ->
            try {
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    BitmapFactory.decodeStream(stream)
                }
            } catch (_: Exception) {
                null
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF2D2D2D)) // Dark gray studio background
    ) {
        SceneView(
            modifier = Modifier.fillMaxSize(),
            engine = engine,
            modelLoader = modelLoader,
            cameraManipulator = cameraManipulator,
            environment = environment,
            mainLightNode = mainLightNode
        ) {
            // Root node with body dimension scaling
            Node(scale = Scale(scaleX, scaleY, scaleZ)) {

                // Avatar model — compact size, not filling entire view
                rememberModelInstance(modelLoader, "models/avatar.glb")?.let { instance ->
                    ModelNode(
                        modelInstance = instance,
                        scaleToUnits = 0.8f,
                        autoAnimate = true
                    )
                }

                // User's photo on avatar
                if (textureBitmap != null && avatarTexture != null) {
                    when (avatarTexture.target) {
                        TextureTarget.FACE -> {
                            val faceWidth = 0.12f * scaleX
                            val faceHeight = faceWidth * (textureBitmap.height.toFloat() / textureBitmap.width)
                            Node(position = Position(0f, 0.88f, 0.07f)) {
                                ImageNode(
                                    bitmap = textureBitmap,
                                    size = Size(faceWidth, faceHeight)
                                )
                            }
                        }
                        TextureTarget.BODY -> {
                            val bodyWidth = 0.25f * scaleX
                            val bodyHeight = bodyWidth * (textureBitmap.height.toFloat() / textureBitmap.width)
                            Node(position = Position(0f, 0.6f, 0.06f)) {
                                ImageNode(
                                    bitmap = textureBitmap,
                                    size = Size(bodyWidth, bodyHeight)
                                )
                            }
                        }
                    }
                }

                // Clothing items on the body
                selectedItems.values.forEach { item ->
                    rememberModelInstance(modelLoader, item.modelUrl)?.let { clothingInstance ->
                        Node(
                            position = when (item.category) {
                                Category.SHIRTS, Category.T_SHIRTS, Category.OUTERWEAR ->
                                    Position(0f, 0.65f, 0f)
                                Category.TROUSERS ->
                                    Position(0f, 0.35f, 0f)
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
