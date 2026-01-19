package ru.netology.nmedia.util

import org.junit.Assert.assertEquals
import org.junit.Test

class CountServiceTest {

    @Test
    fun formatCount_below1000() {
        assertEquals("999", formatCount(999))
    }

    @Test
    fun formatCount_1000to1099() {
        assertEquals("1K", formatCount(1000))
        assertEquals("1K", formatCount(1099))
    }

    @Test
    fun formatCount_1100to9999() {
        assertEquals("1.1K", formatCount(1100))
        assertEquals("9.9K", formatCount(9999))
    }

    @Test
    fun formatCount_10000to999999() {
        assertEquals("10K", formatCount(10000))
        assertEquals("999K", formatCount(999999))
    }

    @Test
    fun formatCount_millions() {
        assertEquals("1M", formatCount(1000000))
        assertEquals("1.1M", formatCount(1100000))
    }
}