package com.example.matrix.ui.matrix

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

private const val MATRIX_GLYPHS =
    "アカサタナハマヤャラワガザダバパイキシチニヒミリヰギジヂビピウクスツヌフムユュルグズブヅプエケセテネヘメレヱゲゼデベペオコソトノホモヨョロヲゴゾドボポヴッン0123456789"

private val MatrixGreen = Color(0xFF00FF41)

val MatrixQuotes = listOf(
    "Wake up, Neo...",
    "The Matrix has you.",
    "Follow the white rabbit.",
    "There is no spoon.",
    "I know kung fu.",
    "What is real? How do you define real?",
    "Free your mind.",
    "Welcome to the desert of the real.",
    "Never send a human to do a machine's job.",
    "You take the blue pill, the story ends.",
    "Unfortunately, no one can be told what the Matrix is.",
    "Ignorance is bliss.",
)

private class RainColumn(y: Float, val speed: Float, val length: Int) {
    var y = y
}

/**
 * Full-screen digital-rain easter egg. Renders on top of the app content
 * and is expected to be shown/hidden by the caller (e.g. inside AnimatedVisibility).
 */
@Composable
fun MatrixRainOverlay(modifier: Modifier = Modifier) {
    val textMeasurer = rememberTextMeasurer()
    val quote = remember { MatrixQuotes.random() }
    var frameTick by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis { }
            frameTick++
        }
    }

    val quoteAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1200, easing = LinearEasing),
        label = "quoteAlpha",
    )

    val charSizePx = with(LocalDensity.current) { 14.sp.toPx() }
    val columns = remember { mutableListOf<RainColumn>() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Reading frameTick here ties this draw phase to the frame clock.
            @Suppress("UNUSED_EXPRESSION") frameTick

            val colCount = (size.width / charSizePx).toInt().coerceAtLeast(1)
            if (columns.size != colCount) {
                columns.clear()
                columns.addAll(
                    List(colCount) {
                        RainColumn(
                            y = Random.nextFloat() * -size.height,
                            speed = Random.nextFloat() * 12f + 10f,
                            length = Random.nextInt(16, 42),
                        )
                    },
                )
            }

            columns.forEachIndexed { index, column ->
                column.y += column.speed
                if (column.y - column.length * charSizePx > size.height) {
                    column.y = Random.nextFloat() * -120f
                }

                val x = index * charSizePx
                for (i in 0 until column.length) {
                    val charY = column.y - i * charSizePx
                    if (charY < 0f || charY > size.height) continue

                    val fade = 1f - (i / column.length.toFloat())
                    val glyph = MATRIX_GLYPHS.random().toString()
                    val color = if (i == 0) {
                        Color.White.copy(alpha = fade)
                    } else {
                        MatrixGreen.copy(alpha = fade)
                    }

                    drawText(
                        textMeasurer = textMeasurer,
                        text = glyph,
                        topLeft = Offset(x, charY),
                        style = TextStyle(
                            color = color,
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace,
                        ),
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.35f)),
        )

        Text(
            text = quote,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 32.dp),
            color = MatrixGreen.copy(alpha = quoteAlpha),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center,
            style = TextStyle(
                shadow = Shadow(
                    color = MatrixGreen,
                    offset = Offset.Zero,
                    blurRadius = 24f,
                ),
            ),
        )
    }
}
