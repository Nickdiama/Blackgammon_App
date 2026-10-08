package com.example.blackgammonapp.data

import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BlackgammonApi {
    // User Login
    @PUT("tavli/plakoto.php/player/{piece_color}")
    suspend fun login(
        @Path("piece_color") pieceColor: String,
        @Body request: UserLoginRequest
    ): UserLoginResponse

    // Show User
    @GET("tavli/plakoto.php/player")
    suspend fun showUser() : List<Users>

    // Show Board
    @GET("tavli/plakoto.php/board")
    suspend fun showBoard() : List<Board>

    // Reset Board
    @POST("tavli/plakoto.php/board")
    suspend fun resetBoard() : List<Board>

    // Status
    @GET("tavli/plakoto.php/status")
    suspend fun status() : List<Status>

    // Starting Dice
    @GET("tavli/plakoto.php/dice/start")
    suspend fun startDice(
        @Header("Token") token: String
    ) : List<StartingDice>

    // Playing Dice
    @GET("tavli/plakoto.php/dice/roll")
    suspend fun playingDice(
        @Header("Token") token: String
    ) : List<PlayingDice>

    // Move Piece
    @PUT("tavli/plakoto.php/board/piece/{from}/{to}")
    suspend fun movePiece(
        @Path("from") from: Int,
        @Path("to") to: Int,
        @Header("Token") token: String
    ) : List<Board>
}
