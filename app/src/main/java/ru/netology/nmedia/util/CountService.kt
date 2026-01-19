package ru.netology.nmedia.util

fun formatCount(count: Int): String {
    return when {
        count < 1000 -> count.toString()
        count < 1100 -> "1K"
        count < 10000 -> {
            val thousands = count / 1000
            val hundreds = (count % 1000) / 100
            if (hundreds == 0) "${thousands}K" else "${thousands}.${hundreds}K"
        }
        count < 1000000 -> "${count / 1000}K"
        else -> {
            val millions = count / 1000000
            val thousands = (count % 1000000) / 100000
            if (thousands == 0) "${millions}M" else "${millions}.${thousands}M"
        }
    }
}