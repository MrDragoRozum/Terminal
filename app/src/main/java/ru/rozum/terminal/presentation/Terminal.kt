package ru.rozum.terminal.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import ru.rozum.terminal.data.Bar

@Composable
fun Terminal(bars: List<Bar>) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        val maxPrice = bars.maxOf { it.high }
        val minPrice = bars.minOf { it.low }
        val barWidth = size.width / bars.size
        val pxPerPoint = size.height / (maxPrice - minPrice)
        bars.forEachIndexed { index, bar ->
            val offsetX = barWidth * index
            drawLine(
                color = Color.White,
                start = Offset(offsetX, size.height - ((bar.low - minPrice) * pxPerPoint)),
                end = Offset(offsetX, size.height - ((bar.high - minPrice) * pxPerPoint)),
                strokeWidth = 1.dp.toPx()
            )
        }
    }
}