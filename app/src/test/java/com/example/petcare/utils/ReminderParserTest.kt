package com.example.petcare.utils

import org.junit.Assert.*
import org.junit.Test

class ReminderParserTest {
    private val minute: Long = 1 * 1000 * 60
    private val hour: Long = 1 * 1000 * 60 * 60
    private val days: Long = 1 * 1000 * 60 * 60 * 24

    private val minuteTest15 = "15 Minutes"
    private val minuteTest30 = "30 Minutes"

    private val hourTest1 = "1 Hours"
    private val hourTest4 = "4 Hours"
    private val dayTest1 = "1 Days"

    @Test
    fun `parse 15 minutes`() {
        val actual = 15 * minute
        val expected = ReminderParser.getAdditionalTime(minuteTest15)

        assertEquals(actual, expected)
    }

    @Test
    fun `parse 30 minutes`() {
        assertEquals(ReminderParser.getAdditionalTime(minuteTest30), 30 * minute)
    }

    @Test
    fun `parse 1 hours`() {
        assertEquals(ReminderParser.getAdditionalTime(hourTest1), 1 * hour)
    }

    @Test
    fun `parse 4 hours`() {
        assertEquals(ReminderParser.getAdditionalTime(hourTest4), 4 * hour)
    }

    @Test
    fun `parse 1 days`(){
        assertEquals(ReminderParser.getAdditionalTime(dayTest1), 1 * days)
    }
}