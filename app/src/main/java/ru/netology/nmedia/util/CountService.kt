package ru.netology.nmedia.util

import java.util.Locale

fun formatCount(count: Int): String {
    return when {
        count < 1000 -> count.toString()
        count < 10000 -> {
            val value = count / 1000.0
            if (count % 1000 < 100) "${value.toInt()}K" 
            else String.format(Locale.US, "%.1fK", value)
        }
        count < 1000000 -> "${count / 1000}K"
        else -> {
            val value = count / 1000000.0
            if (count % 1000000 < 100000) "${value.toInt()}M" 
            else String.format(Locale.US, "%.1fM", value)
        }
    }
}
