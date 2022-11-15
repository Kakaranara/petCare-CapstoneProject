package com.example.petcare

/**
 * Experiment kotlin functions here
 */
fun main() {
    val a = Menu.values()
    val b = a.map { it.str }.toTypedArray()
    val c = b[0]
    val d = Menu.MenuO.str
}

enum class Menu(val str: String) {
    MenuO("Oke"),
    MenuG("Nope")
}