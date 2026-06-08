package com.hussein.varview.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hussein.varview.domain.model.AvatarDimensions

@Composable
fun BodyProfilePanel(
    dimensions: AvatarDimensions,
    onDimensionsChange: (AvatarDimensions) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Body Profile",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            DimensionSlider(
                label = "Height",
                value = dimensions.height,
                range = 1.4f..2.1f,
                unit = "m",
                onValueChange = { onDimensionsChange(dimensions.copy(height = it)) }
            )

            DimensionSlider(
                label = "Shoulder Width",
                value = dimensions.shoulderWidth,
                range = 0.3f..0.7f,
                unit = "m",
                onValueChange = { onDimensionsChange(dimensions.copy(shoulderWidth = it)) }
            )

            DimensionSlider(
                label = "Waist Width",
                value = dimensions.waistWidth,
                range = 0.25f..0.6f,
                unit = "m",
                onValueChange = { onDimensionsChange(dimensions.copy(waistWidth = it)) }
            )

            DimensionSlider(
                label = "Chest Width",
                value = dimensions.chestWidth,
                range = 0.3f..0.65f,
                unit = "m",
                onValueChange = { onDimensionsChange(dimensions.copy(chestWidth = it)) }
            )
        }
    }
}

@Composable
private fun DimensionSlider(
    label: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    unit: String,
    onValueChange: (Float) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 13.sp,
                color = Color.DarkGray
            )
            Text(
                text = "%.2f %s".format(value, unit),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
