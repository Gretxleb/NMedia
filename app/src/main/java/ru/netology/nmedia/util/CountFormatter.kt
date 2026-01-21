package ru.netology.nmedia.util

import java.math.RoundingMode
import java.text.DecimalFormat

object CountFormatter {
    fun format(count: Int): String {
        return when {
            count < 1000 -> count.toString()
            count < 10_000 -> {
                val value = count / 1000.0
                val df = DecimalFormat("#.#")
                df.roundingMode = RoundingMode.DOWN
                "${df.format(value)}K"
            }
            count < 1_000_000 -> "${count / 1000}K"
            else -> {
                val value = count / 1_000_000.0
                val df = DecimalFormat("#.#")
                df.roundingMode = RoundingMode.DOWN
                "${df.format(value)}M"
            }
        }
    }
}