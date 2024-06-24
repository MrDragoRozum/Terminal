package ru.rozum.terminal.presentation

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.parcelize.Parcelize
import ru.rozum.terminal.data.Bar
import kotlin.math.roundToInt

@Parcelize
data class TerminalState(
    val barList: List<Bar>,
    val visibleBarsCount: Int = 100,
    val terminalWidth: Float = 1f,
    val terminalHeight: Float = 1f,
    val scrolledBy: Float = 0f
) : Parcelable {
    val barWidth: Float
        get() = terminalWidth / visibleBarsCount

    private val visibleBars: List<Bar>
        get() {
            val startIndex = (scrolledBy / barWidth).roundToInt().coerceAtLeast(0)
            val endIndex = (startIndex + visibleBarsCount).coerceAtMost(barList.size)
            return barList.subList(startIndex, endIndex)

        }

    val maxPrice: Float get() = visibleBars.maxOf { it.high }
    val minPrice: Float get() = visibleBars.minOf { it.low }
    val pxPerPoint: Float get() = terminalHeight / (maxPrice - minPrice)
}

@Composable
fun rememberTerminalState(bars: List<Bar>): MutableState<TerminalState> = rememberSaveable {
    mutableStateOf(TerminalState(barList = bars))
}



