package com.example.matrix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.matrix.ui.matrix.MatrixRainOverlay
import com.example.matrix.ui.theme.MatrixTheme
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay

private const val TAPS_TO_TRIGGER_EASTER_EGG = 10
private val EASTER_EGG_DURATION = 10.seconds

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MatrixTheme {
                MatrixApp()
            }
        }
    }
}

@Composable
fun MatrixApp() {
    var tapCount by remember { mutableIntStateOf(0) }
    var showEasterEgg by remember { mutableStateOf(false) }

    LaunchedEffect(showEasterEgg) {
        if (showEasterEgg) {
            delay(EASTER_EGG_DURATION)
            showEasterEgg = false
            tapCount = 0
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Greeting(
                name = "Android",
                onTap = {
                    if (!showEasterEgg) {
                        tapCount++
                        if (tapCount >= TAPS_TO_TRIGGER_EASTER_EGG) {
                            showEasterEgg = true
                        }
                    }
                },
            )

            AnimatedVisibility(
                visible = showEasterEgg,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                MatrixRainOverlay(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, onTap: () -> Unit = {}) {
    Text(
        text = "Hello $name!",
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onTap,
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MatrixTheme {
        Greeting("Android")
    }
}
