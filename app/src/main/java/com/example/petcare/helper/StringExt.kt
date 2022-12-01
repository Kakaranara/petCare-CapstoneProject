package com.example.petcare.helper

fun String.capitalizeWords(): String = split(" ").map { it.capitalize() }.joinToString(" ")