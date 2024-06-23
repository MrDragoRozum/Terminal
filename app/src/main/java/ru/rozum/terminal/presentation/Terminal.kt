package ru.rozum.terminal.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.onSizeChanged
import ru.rozum.terminal.data.Bar
import kotlin.math.roundToInt

private const val MIN_VISIBLE_BARS_COUNT = 20

@Composable
fun Terminal(bars: List<Bar>) {

    var terminalState by rememberTerminalState(bars = bars)

    val transformableState = TransformableState { zoomChange, panChange, _ ->

        val visibleBarsCount = (terminalState.visibleBarsCount / zoomChange).roundToInt().coerceIn(
            MIN_VISIBLE_BARS_COUNT, bars.size
        )

        val scrolledBy = (terminalState.scrolledBy + panChange.x).coerceIn(
            0f, bars.size * terminalState.barWidth - terminalState.terminalWidth
        )

        terminalState = terminalState.copy(
            visibleBarsCount = visibleBarsCount,
            scrolledBy = scrolledBy
        )
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .transformable(transformableState)
            .onSizeChanged {
                terminalState = terminalState.copy(terminalWidth = it.width.toFloat())
            }
    ) {
        val maxPrice = terminalState.visibleBars.maxOf { it.high }
        val minPrice = terminalState.visibleBars.minOf { it.low }
        val pxPerPoint = size.height / (maxPrice - minPrice)
        translate(left = terminalState.scrolledBy) {
            bars.forEachIndexed { index, bar ->
                val offsetX = size.width - (terminalState.barWidth * index)
                drawLine(
                    color = Color.White,
                    start = Offset(offsetX, size.height - ((bar.low - minPrice) * pxPerPoint)),
                    end = Offset(offsetX, size.height - ((bar.high - minPrice) * pxPerPoint)),
                )

                drawLine(
                    color = if (bar.open < bar.close) Color.Green else Color.Red,
                    start = Offset(offsetX, size.height - ((bar.open - minPrice) * pxPerPoint)),
                    end = Offset(offsetX, size.height - ((bar.close - minPrice) * pxPerPoint)),
                    strokeWidth = terminalState.barWidth / 2
                )
            }
        }
    }
}