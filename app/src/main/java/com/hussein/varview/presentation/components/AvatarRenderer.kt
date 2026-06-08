package com.hussein.varview.presentation.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
 * 3D Avatar renderer with dimension indicators.
 * Shows height, shoulder width, waist width as overlay labels.
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

    val cameraManipulator = rememberCameraManipulator(
        orbitHomePosition = Position(0f, 0.45f, 3.5f),
        targetPosition = Position(0f, 0.45f, 0f)
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
            .background(Color(0xFF2D2D2D))
    ) {
        // 3D Scene
        SceneView(
            modifier = Modifier.fillMaxSize(),
            engine = engine,
            modelLoader = modelLoader,
            cameraManipulator = cameraManipulator,
            environment = environment,
            mainLightNode = mainLightNode
        ) {
            Node(scale = Scale(scaleX, scaleY, scaleZ)) {

                rememberModelInstance(modelLoader, "models/avatar.glb")?.let { instance ->
                    ModelNode(
                        modelInstance = instance,
                        scaleToUnits = 0.6f,
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

                // Clothing items
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

        // Height indicator (left side)
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight()
                .width(40.dp)
                .padding(start = 8.dp, top = 24.dp, bottom = 24.dp)
        ) {
            // Vertical dashed line
            Canvas(modifier = Modifier.fillMaxSize()) {
                val dash = PathEffect.dashPathEffect(floatArrayOf(10f, 8f), 0f)
                drawLine(
                    color = Color(0xFF4FC3F7),
                    start = Offset(size.width / 2, 0f),
                    end = Offset(size.width / 2, size.height),
                    strokeWidth = 2f,
                    pathEffect = dash
                )
                // Top arrow
                drawLine(Color(0xFF4FC3F7), Offset(size.width / 2 - 8, 12f), Offset(size.width / 2, 0f), 2f)
                drawLine(Color(0xFF4FC3F7), Offset(size.width / 2 + 8, 12f), Offset(size.width / 2, 0f), 2f)
                // Bottom arrow
                drawLine(Color(0xFF4FC3F7), Offset(size.width / 2 - 8, size.height - 12), Offset(size.width / 2, size.height), 2f)
                drawLine(Color(0xFF4FC3F7), Offset(size.width / 2 + 8, size.height - 12), Offset(size.width / 2, size.height), 2f)
            }
            // Height label
            Text(
                text = "%.0f cm".format(dimensions.height * 100),
                color = Color(0xFF4FC3F7),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Dimension labels (right side)
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp),
            horizontalAlignment = Alignment.End
        ) {
            DimensionLabel("Shoulders", "%.0f cm".format(dimensions.shoulderWidth * 100))
            DimensionLabel("Chest", "%.0f cm".format(dimensions.chestWidth * 100))
            DimensionLabel("Waist", "%.0f cm".format(dimensions.waistWidth * 100))
            DimensionLabel("Hips", "%.0f cm".format(dimensions.hipWidth * 100))
        }
    }
}

@Composable
private fun DimensionLabel(label: String, value: String) {
    Column(
        modifier = Modifier.padding(vertical = 4.dp),
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = label,
            color = Color(0xFFBDBDBD),
            fontSize = 9.sp
        )
        Text(
            text = value,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
