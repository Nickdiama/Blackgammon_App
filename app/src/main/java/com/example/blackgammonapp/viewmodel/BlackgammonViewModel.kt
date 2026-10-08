package com.example.blackgammonapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blackgammonapp.RetrofitClient
import com.example.blackgammonapp.data.Board
import com.example.blackgammonapp.data.BlackgammonApi
import com.example.blackgammonapp.data.UserLoginRequest
import com.example.blackgammonapp.data.UserLoginResponse
import com.example.blackgammonapp.data.Users
import com.example.blackgammonapp.data.MovePiece
import com.example.blackgammonapp.data.PlayingDice
import com.example.blackgammonapp.data.StartingDice
import com.example.blackgammonapp.data.Status
import kotlinx.coroutines.launch
import retrofit2.HttpException

class BlackgammonViewModel : ViewModel() {
    var authToken by mutableStateOf("")
        private set

    var boardState by mutableStateOf<List<Board>>(emptyList())
        private set

    var allUsers by mutableStateOf<List<Users>>(emptyList())
        private set

    var status by mutableStateOf<List<Status>>(emptyList())
        private set

    var startingDice by mutableStateOf<List<StartingDice>>(emptyList())
        private set

    var playingDice by mutableStateOf<List<PlayingDice>>(emptyList())
        private set

    var errorMessage by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    // User Login
    fun userLogin(username: String, color: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try{
                val request = UserLoginRequest(username = username)
                val response = RetrofitClient.api.login(pieceColor = color, request = request)

                authToken = response.token
                onSuccess()
            }catch (e: HttpException){
                val errorBody = e.response()?.errorBody()?.string()
                errorMessage = "Σφάλμα σύνδεσης: $errorBody"
            }catch (e: Exception){
                errorMessage = "Σφάλμα δικτύου: ${e.localizedMessage}"
            }finally {
                isLoading = false
            }
        }
    }

    // Show Users
    fun users() {
        viewModelScope.launch {
            errorMessage = ""
            try {
                allUsers = RetrofitClient.api.showUser()
            }catch (e: Exception){
                errorMessage = "Σφάλμα δικτύου: ${e.localizedMessage}"
            }
        }
    }

    // Show Board
    fun getBoard(){
        viewModelScope.launch {
            errorMessage = ""
            try {
                boardState = RetrofitClient.api.showBoard()
            }catch (e: Exception){
                errorMessage = "Σφάλμα δικτύου: ${e.localizedMessage}"
            }
        }
    }

    // Reset Board
    fun postBoard(){
        viewModelScope.launch {
            errorMessage = ""
            try {
                boardState = RetrofitClient.api.resetBoard()
            }catch (e: Exception){
                errorMessage = "Σφάλμα δικτύου: ${e.localizedMessage}"
            }
        }
    }

    // Status
    fun getStatus() {
        viewModelScope.launch {
            errorMessage = ""
            try {
                status = RetrofitClient.api.status()
            }catch (e: Exception){
                errorMessage = "Σφάλμα δικτύου: ${e.localizedMessage}"
            }
        }
    }

    // Starting Dice
    fun startingDice(){
        if (authToken.isEmpty()) return

        viewModelScope.launch {
            errorMessage = ""
            try {
                val tokenHolder = authToken
                startingDice = RetrofitClient.api.startDice(tokenHolder)
            }catch (e: HttpException){
                val errorBody = e.response()?.errorBody()?.string()
                errorMessage = "Σφάλμα: $errorBody"
            }catch (e: Exception){
                errorMessage = "Σφάλμα δικτύου: ${e.localizedMessage}"
            }
        }
    }

    // Playing Dice
    fun playingDice(){
        if (authToken.isEmpty()) return

        viewModelScope.launch {
            errorMessage = ""
            try {
                val tokenHolder = authToken
                playingDice = RetrofitClient.api.playingDice(tokenHolder)
            }catch (e: HttpException){
                val errorBody = e.response()?.errorBody()?.string()
                errorMessage = "Σφάλμα: $errorBody"
            }catch (e: Exception){
                errorMessage = "Σφάλμα δικτύου: ${e.localizedMessage}"
            }
        }
    }

    fun playMove(from: Int, to: Int){
        if (authToken.isEmpty()) return

        viewModelScope.launch {
            errorMessage = ""
            try {
                val tokenHolder = authToken
                boardState = RetrofitClient.api.movePiece(from, to, tokenHolder)
            }catch (e: HttpException){
                val errorBody = e.response()?.errorBody()?.string()
                errorMessage = "Σφάλμα: $errorBody"
            }catch (e: Exception){
                errorMessage = "Σφάλμα δικτύου: ${e.localizedMessage}"
            }
        }
    }
}