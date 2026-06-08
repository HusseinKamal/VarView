package com.hussein.varview.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hussein.varview.presentation.components.AvatarRenderer
import com.hussein.varview.presentation.components.BodyProfilePanel
import com.hussein.varview.presentation.components.FullARView
import com.hussein.varview.presentation.components.WardrobePanel
import com.hussein.varview.presentation.viewmodel.TryOnViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VFitScreen(viewModel: TryOnViewModel = viewModel()) {
    val selectedItems by viewModel.selectedItems.collectAsState()
    val dimensions by viewModel.avatarDimensions.collectAsState()
    val activeCategory by viewModel.activeCategory.collectAsState()
    val wardrobeItems by viewModel.wardrobeItems.collectAsState()
    val isARMode by viewModel.isARMode.collectAsState()

    // Panel tab: 0 = Wardrobe, 1 = Body Profile
    var activeTab by remember { mutableIntStateOf(0) }
    // Bottom nav selection
    var bottomNavIndex by remember { mutableIntStateOf(0) }

    // Full AR mode
    if (isARMode) {
        FullARView(
            selectedItems = selectedItems,
            dimensions = dimensions,
            onDismiss = { viewModel.setARMode(false) }
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "VFit",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = bottomNavIndex == 0,
                    onClick = { bottomNavIndex = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Checkroom, contentDescription = "Wardrobe") },
                    label = { Text("Wardrobe") },
                    selected = bottomNavIndex == 1,
                    onClick = { bottomNavIndex = 1 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = bottomNavIndex == 2,
                    onClick = { bottomNavIndex = 2 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.CameraAlt, contentDescription = "AR") },
                    label = { Text("AR") },
                    selected = bottomNavIndex == 3,
                    onClick = {
                        bottomNavIndex = 3
                        viewModel.setARMode(true)
                    }
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 1. 3D Avatar Rendering (Background)
            AvatarRenderer(
                selectedItems = selectedItems,
                dimensions = dimensions,
                modifier = Modifier.fillMaxSize()
            )

            // 2. Overlay UI at bottom
            Column(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                // Full AR View button
                Button(
                    onClick = { viewModel.setARMode(true) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.ViewInAr,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Full AR View")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Tab switcher between Wardrobe and Body Profile
                TabRow(
                    selectedTabIndex = activeTab,
                    containerColor = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Tab(
                        selected = activeTab == 0,
                        onClick = { activeTab = 0 },
                        text = { Text("Wardrobe") }
                    )
                    Tab(
                        selected = activeTab == 1,
                        onClick = { activeTab = 1 },
                        text = { Text("Body Profile") }
                    )
                }

                // Animated panel content
                AnimatedVisibility(
                    visible = activeTab == 0,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it })
                ) {
                    WardrobePanel(
                        activeCategory = activeCategory,
                        wardrobeItems = wardrobeItems,
                        selectedItems = selectedItems,
                        onCategoryChange = { viewModel.setCategory(it) },
                        onItemClick = { viewModel.selectItem(it) }
                    )
                }

                AnimatedVisibility(
                    visible = activeTab == 1,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it })
                ) {
                    BodyProfilePanel(
                        dimensions = dimensions,
                        onDimensionsChange = { viewModel.updateDimensions(it) }
                    )
                }
            }
        }
    }
}
