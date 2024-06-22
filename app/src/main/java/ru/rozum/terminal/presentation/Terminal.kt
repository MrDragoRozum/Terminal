package ru.rozum.terminal.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.rozum.terminal.data.Bar
import kotlin.math.roundToInt

private const val MIN_VISIBLE_BARS_COUNT = 20

@Composable
fun Terminal(bars: List<Bar>) {

    var visibleBarsCount by remember {
        mutableIntStateOf(100)
    }

    val transformableState = TransformableState { zoomChange, _, _ ->

        visibleBarsCount = (visibleBarsCount / zoomChange).roundToInt().coerceIn(
            MIN_VISIBLE_BARS_COUNT, bars.size
        )
        println("zoomChange: $zoomChange")
        println("visibleBarsCount: $visibleBarsCount")
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .transformable(transformableState)
    ) {
        val maxPrice = bars.maxOf { it.high }
        val minPrice = bars.minOf { it.low }
        val barWidth = size.width / visibleBarsCount
        val pxPerPoint = size.height / (maxPrice - minPrice)
        bars.take(visibleBarsCount).forEachIndexed { index, bar ->
            val offsetX = size.width - (barWidth * index)
            drawLine(
                color = Color.White,
                start = Offset(offsetX, size.height - ((bar.low - minPrice) * pxPerPoint)),
                end = Offset(offsetX, size.height - ((bar.high - minPrice) * pxPerPoint)),
                strokeWidth = 1.dp.toPx()
            )
        }
    }
}