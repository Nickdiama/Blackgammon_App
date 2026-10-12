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
import com.example.blackgammonapp.viewmodel.BlackgammonViewModel

@Composable
fun MainScreen(
    viewModel: BlackgammonViewModel,
    modifier: Modifier = Modifier,
    onNavigateToBoard: () -> Unit = {} // Συνάρτηση για να αλλάζει οθόνη και να πηγαίνει στο ταμπλό
) {
    // Κατάστασεις για το αν είναι ανοιχτά τα Dialogs
    var showLoginDialog by remember { mutableStateOf(false) }
    var showStatusDialog by remember { mutableStateOf(false) }

    // Τοπικά πεδία για το Dialog του Login
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

                // --- 2. ΚΟΥΜΠΙ LOGIN (Ανοίγει Dialog) ---
                OutlinedButton(
                    onClick = { showLoginDialog = true },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(text = "Σύνδεση", fontSize = 16.sp)
                }

                // --- 1. ΜΕΓΑΛΟ ΠΡΑΣΙΝΟ ΚΟΥΜΠΙ ΓΙΑ ΤΟ ΤΑΜΠΛΟ ---
                Button(
                    onClick = { onNavigateToBoard() },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)) // Σκούρο πράσινο
                ) {
                    Text(text = "Παίξε ", fontSize = 20.sp, color = Color.White)
                }

                // --- 3. ΚΟΥΜΠΙ STATUS (Ανοίγει Dialog) ---
                OutlinedButton(
                    onClick = {
                        viewModel.getStatus() // Φέρνει τα δεδομένα status από τον server
                        showStatusDialog = true
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
                        color = Color(0xFF2E7D32),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }

    // ==========================================
    // DIALOG 1: LOGIN POPUP
    // ==========================================
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
                            showLoginDialog = false // Κλείνει το παράθυρο αν πετύχει
                        }
                    },
                    enabled = !viewModel.isLoading && usernameInput.isNotBlank()
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
}
