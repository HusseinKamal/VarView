package com.hussein.varview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hussein.varview.presentation.screen.VFitScreen
import com.hussein.varview.ui.theme.VarViewTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VarViewTheme {
                VFitScreen()
            }
        }
    }
}
