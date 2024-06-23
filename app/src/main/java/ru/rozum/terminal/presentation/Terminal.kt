package ru.rozum.terminal.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import ru.rozum.terminal.data.Bar
import kotlin.math.roundToInt

private const val MIN_VISIBLE_BARS_COUNT = 20

@Composable
fun Terminal(bars: List<Bar>) {

    var visibleBarsCount by remember {
        mutableIntStateOf(100)
    }

    var scrolledBy by remember {
        mutableFloatStateOf(0f)
    }

    var terminalWidth by remember {
        mutableFloatStateOf(0f)
    }

    val barWidth by remember {
        derivedStateOf {
            terminalWidth / visibleBarsCount
        }
    }

    val visibleBars by remember {
        derivedStateOf {
            val startIndex = (scrolledBy / barWidth).roundToInt().coerceAtLeast(0)
            val endIndex = (startIndex + visibleBarsCount).coerceAtMost(bars.size)
            bars.subList(startIndex, endIndex)
        }
    }

    val transformableState = TransformableState { zoomChange, panChange, _ ->

        visibleBarsCount = (visibleBarsCount / zoomChange).roundToInt().coerceIn(
            MIN_VISIBLE_BARS_COUNT, bars.size
        )


        scrolledBy = (scrolledBy + panChange.x).coerceIn(
            0f, bars.size * barWidth - terminalWidth
        )

    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .transformable(transformableState)
    ) {
        terminalWidth = size.width
        val maxPrice = visibleBars.maxOf { it.high }
        val minPrice = visibleBars.minOf { it.low }
        val pxPerPoint = size.height / (maxPrice - minPrice)
        translate(left = scrolledBy) {
            bars.forEachIndexed { index, bar ->
                val offsetX = size.width - (barWidth * index)
                drawLine(
                    color = Color.White,
                    start = Offset(offsetX, size.height - ((bar.low - minPrice) * pxPerPoint)),
                    end = Offset(offsetX, size.height - ((bar.high - minPrice) * pxPerPoint)),
                )

                drawLine(
                    color = if (bar.open < bar.close) Color.Green else Color.Red,
                    start = Offset(offsetX, size.height - ((bar.open - minPrice) * pxPerPoint)),
                    end = Offset(offsetX, size.height - ((bar.close - minPrice) * pxPerPoint)),
                    strokeWidth = barWidth / 2
                )
            }
        }
    }
}