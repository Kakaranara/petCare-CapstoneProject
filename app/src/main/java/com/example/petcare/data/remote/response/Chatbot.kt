package com.example.petcare.data.remote.response

data class BotQuestion(
    val id: String,
    val question: String
)

data class BotDisease(
    val id: String,
    val opening: String,
    val explanation: String,
    val medication: String,
)

data class Message(
    val message: String,
    val id: String,
    val time: String)