package com.hussein.varview.presentation.components

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
import io.github.sceneview.rememberModelInstance
import io.github.sceneview.rememberModelLoader

/**
 * 3D Avatar renderer using SceneView (Filament engine).
 *
 * Displays a default humanoid avatar model loaded from app assets.
 * Selected clothing items are loaded and positioned on the avatar body.
 * User can orbit/zoom the model via touch gestures (cameraManipulator).
 *
 * Place your avatar .glb file at: assets/models/avatar.glb
 * Place clothing .glb files at: assets/models/<name>.glb
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
    val cameraManipulator = rememberCameraManipulator()
    val environment = rememberEnvironment(engine)

    // Calculate body scale from dimensions (normalized to defaults)
    val scaleX = dimensions.shoulderWidth / 0.5f
    val scaleY = dimensions.height / 1.7f
    val scaleZ = dimensions.waistWidth / 0.4f

    // Load user photo bitmap if provided
    val textureBitmap = remember(avatarTexture?.uri) {
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
            .background(Color(0xFFF0F4F8))
    ) {
        SceneView(
            modifier = Modifier.fillMaxSize(),
            engine = engine,
            modelLoader = modelLoader,
            cameraManipulator = cameraManipulator,
            environment = environment
        ) {
            // Root node with body dimension scaling
            Node(scale = Scale(scaleX, scaleY, scaleZ)) {

                // Default avatar model (bundled in assets/models/avatar.glb)
                rememberModelInstance(modelLoader, "models/avatar.glb")?.let { avatarInstance ->
                    ModelNode(
                        modelInstance = avatarInstance,
                        scaleToUnits = 1.7f,
                        autoAnimate = true
                    )
                }

                // User photo displayed on avatar face/body
                if (textureBitmap != null && avatarTexture != null) {
                    Node(
                        position = when (avatarTexture.target) {
                            TextureTarget.FACE -> Position(0f, 1.65f, 0.12f)
                            TextureTarget.BODY -> Position(0f, 1.1f, 0.08f)
                        }
                    ) {
                        ImageNode(
                            bitmap = textureBitmap,
                            size = when (avatarTexture.target) {
                                TextureTarget.FACE -> Size(0.2f, 0.25f)
                                TextureTarget.BODY -> Size(0.4f, 0.5f)
                            }
                        )
                    }
                }

                // Clothing items placed on the avatar body
                selectedItems.values.forEach { item ->
                    rememberModelInstance(modelLoader, item.modelUrl)?.let { clothingInstance ->
                        Node(
                            position = when (item.category) {
                                Category.SHIRTS, Category.T_SHIRTS, Category.OUTERWEAR ->
                                    Position(0f, 1.2f, 0f)
                                Category.TROUSERS ->
                                    Position(0f, 0.6f, 0f)
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
