package com.example.blackgammonapp.data

data class UserLoginResponse(
    val username: String,
    val piece_color: String,
    val token: String,
    val last_action: String
)

