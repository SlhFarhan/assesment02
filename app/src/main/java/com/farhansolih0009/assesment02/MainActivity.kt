package com.farhansolih0009.assesment02

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.farhansolih0009.assesment02.navigation.SetupNavGraph
import com.farhansolih0009.assesment02.ui.theme.Assesment02Theme
import com.farhansolih0009.assesment02.navigation.SetupNavGraph
import com.farhansolih0009.assesment02.ui.theme.Assesment02Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assesment02Theme {
                SetupNavGraph()
            }
        }
    }
}