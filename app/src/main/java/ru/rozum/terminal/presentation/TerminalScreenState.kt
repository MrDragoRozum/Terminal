package ru.rozum.terminal.presentation

import ru.rozum.terminal.data.Bar

sealed class TerminalScreenState {
    data object Initial: TerminalScreenState()

    data class Content(val barList: List<Bar>): TerminalScreenState()
}