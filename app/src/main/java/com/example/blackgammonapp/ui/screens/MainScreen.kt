package com.example.blackgammonapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.blackgammonapp.RetrofitClient
import com.example.blackgammonapp.ui.theme.Green
import com.example.blackgammonapp.viewmodel.BlackgammonViewModel

@Composable
fun MainScreen(
    viewModel: BlackgammonViewModel,
    modifier: Modifier = Modifier,
    onNavigateToBoard: () -> Unit = {} // Screen changing function
) {
    // Dialog state
    var showLoginDialog by remember { mutableStateOf(false) }
    var showStatusDialog by remember { mutableStateOf(false) }
    var showUsersDialog by remember { mutableStateOf(false) }

    // Variables for login
    var usernameInput by remember { mutableStateOf("") }
    var colorInput by remember { mutableStateOf("W") } // "W" ή "B"

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Τίτλος παιχνιδιού
                Text(
                    text = "Τάβλι Πλακωτό",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Login button
                OutlinedButton(
                    onClick = { showLoginDialog = true } /*Opens dialog*/,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(text = "Σύνδεση", fontSize = 16.sp)
                }

                // Board button
                Button(
                    onClick = { onNavigateToBoard() },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Green)
                ) {
                    Text(text = "Παίξε ", fontSize = 20.sp, color = Color.White)
                }

                // Users Button
                OutlinedButton(
                    onClick = {
                        viewModel.users()
                        showUsersDialog = true /*Opens dialog*/
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(text = "Παίζουν τώρα...", fontSize = 16.sp)
                }

                // Status button
                OutlinedButton(
                    onClick = {
                        viewModel.getStatus()
                        showStatusDialog = true /*Opens dialog*/
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(text = "Κατάσταση Παιχνιδιού", fontSize = 16.sp)
                }

                // Εμφάνιση ένδειξης αν υπάρχει ενεργό token
                if (viewModel.authToken.isNotBlank()) {
                    Text(
                        text = "✓ Συνδεδεμένος",
                        color = Green,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }

    // Login dialog
    if (showLoginDialog) {
        AlertDialog(
            onDismissRequest = { showLoginDialog = false },
            title = { Text("Σύνδεση Χρήστη") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = usernameInput,
                        onValueChange = { usernameInput = it },
                        label = { Text("Όνομα Χρήστη") },
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = colorInput,
                        onValueChange = { colorInput = it },
                        label = { Text("Χρώμα (W ή B)") },
                        singleLine = true
                    )

                    if (viewModel.errorMessage.isNotBlank()) {
                        Text(
                            text = viewModel.errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.userLogin(username = usernameInput, color = colorInput) {
                            showLoginDialog = false // Closes the dialog
                        }
                    }
                ) {
                    if (viewModel.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White
                        )
                    } else {
                        Text("OK")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showLoginDialog = false }) {
                    Text("Άκυρο")
                }
            }
        )
    }

    // Status dialog
    if (showStatusDialog) {
        AlertDialog(
            onDismissRequest = { showStatusDialog = false },
            title = { Text("Κατάσταση Παιχνιδιού") },
            text = {
                Column {
                    if (viewModel.status.isEmpty()) {
                        Text("Φόρτωση ή κενά δεδομένα...")
                    } else {
                        for (st in viewModel.status) {
                            Text("State: ${st.status ?: "N/A"}")
                            Text("Turn: ${st.p_turn} ")
                            Text("Winner: ${st.result ?: "None"}")
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showStatusDialog = false }) {
                    Text("Κλείσιμο")
                }
            },

        )
    }

    // Users dialog
    if (showUsersDialog) {
        AlertDialog(
            onDismissRequest = { showUsersDialog = false },
            title = { Text("Παίζουν τώρα...") },
            text = {
                Column {
                    for (us in viewModel.allUsers){
                        Text("Player: ${us.username}, Color: ${us.piece_color}")
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showUsersDialog = false }) {
                    Text("Κλείσιμο")
                }
            }
        )
    }
}
