package com.example.blackgammonapp.data

data class Status(
    val dice1: Int,
    val dice2: Int,
    val dice3: Int,
    val dice4: Int,
    val double_count: Int,
    val last_change: String,
    val p_turn: String,
    val result: Any,
    val score: Int,
    val start_dice_b: Int,
    val start_dice_w: Int,
    val status: String
)