package com.example.blackgammonapp

data class StartingDice(
    val starter: String,
    val status: String,
    val you_rolled: Int,
    val wating_player: Boolean
)