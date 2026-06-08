package com.hussein.varview.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.ar.core.Anchor
import com.google.ar.core.Frame
import com.google.ar.core.Plane
import com.google.ar.core.Session
import com.google.ar.core.TrackingState
import com.hussein.varview.domain.model.AvatarDimensions
import com.hussein.varview.domain.model.Category
import com.hussein.varview.domain.model.ClothingItem
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.math.Position
import io.github.sceneview.math.Scale
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelInstance
import io.github.sceneview.rememberModelLoader

/**
 * Full AR View — places the avatar with selected clothing on a detected floor plane.
 * Uses ARCore through SceneView 4.x ARSceneView composable.
 *
 * Plane detected → anchor set via state → Compose recomposes → model appears.
 */
@Composable
fun FullARView(
    selectedItems: Map<Category, ClothingItem>,
    dimensions: AvatarDimensions,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)

    var anchor by remember { mutableStateOf<Anchor?>(null) }

    // Calculate scale from dimensions
    val scaleX = dimensions.shoulderWidth / 0.5f
    val scaleY = dimensions.height / 1.7f
    val scaleZ = dimensions.waistWidth / 0.4f

    Box(modifier = modifier.fillMaxSize()) {
        ARSceneView(
            modifier = Modifier.fillMaxSize(),
            engine = engine,
            modelLoader = modelLoader,
            planeRenderer = true,
            onSessionUpdated = { session: Session, frame: Frame ->
                // Auto-anchor to the first detected horizontal plane
                if (anchor == null) {
                    frame.getUpdatedTrackables(Plane::class.java)
                        .firstOrNull { plane ->
                            plane.type == Plane.Type.HORIZONTAL_UPWARD_FACING &&
                                plane.trackingState == TrackingState.TRACKING
                        }
                        ?.let { plane ->
                            anchor = plane.createAnchor(plane.centerPose)
                        }
                }
            }
        ) {
            // When anchor is placed, show the avatar with clothes
            anchor?.let { placedAnchor ->
                AnchorNode(anchor = placedAnchor) {
                    // Scale wrapper node for body dimensions
                    Node(scale = Scale(scaleX, scaleY, scaleZ)) {
                        // Avatar model
                        rememberModelInstance(modelLoader, "models/avatar.glb")?.let { avatarInstance ->
                            ModelNode(
                                modelInstance = avatarInstance,
                                scaleToUnits = 0.5f,
                                autoAnimate = true
                            )
                        }

                        // Clothing items
                        selectedItems.values.forEach { item ->
                            rememberModelInstance(modelLoader, item.modelUrl)?.let { clothingInstance ->
                                Node(
                                    position = when (item.category) {
                                        Category.SHIRTS, Category.T_SHIRTS, Category.OUTERWEAR ->
                                            Position(0f, 0.35f, 0f)
                                        Category.TROUSERS ->
                                            Position(0f, 0.18f, 0f)
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

        // Close button
        FloatingActionButton(
            onClick = onDismiss,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.errorContainer
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Exit AR"
            )
        }

        // Instructional overlay (shown when no anchor yet)
        if (anchor == null) {
            Text(
                text = "Point camera at floor to detect surface...",
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 48.dp)
            )
        }
    }
}
