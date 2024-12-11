package com.iab.dailyhabit.screens.tareas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.iab.dailyhabit.R
import kotlinx.coroutines.launch


@Composable
fun EditarTareaScreen(
    navController: NavController,
    documentId: String,
    onTaskUpdated: () -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var numeroVeces by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var progresoActual by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    fun isValidTimeFormat(input: String): Boolean {
        val regex = Regex("^([01]\\d|2[0-3]):([0-5]\\d)$")
        return regex.matches(input)
    }
    LaunchedEffect(documentId) {
        FirebaseFirestore.getInstance()
            .collection("tasks")
            .document(documentId)
            .get()
            .addOnSuccessListener { document ->
                titulo = document.getString("titulo") ?: ""
                numeroVeces = document.getLong("numeroVeces")?.toString() ?: "0"
                hora = document.getString("hora") ?: ""
                progresoActual = document.getLong("progresoActual")?.toInt() ?: 0
                isLoading = false
            }
            .addOnFailureListener { exception ->
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Error al cargar los datos: ${exception.message}")
                }
                isLoading = false
            }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    } else {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Editando Tarea",
                        fontSize = 32.sp,
                        fontFamily = GagalinFontFamily,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .background(Color(0xFFF57C00), shape = RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            OutlinedTextField(
                                value = titulo,
                                onValueChange = { titulo = it },
                                label = {
                                    Text(
                                        "Título de la tarea", fontFamily = GagalinFontFamily,
                                        color = Color.Black
                                    )
                                },
                                textStyle = TextStyle(color = Color.Black),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp),
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Black,
                                    unfocusedIndicatorColor = Color.Black,
                                    focusedLabelColor = Color.Black,
                                    unfocusedLabelColor = Color.Black
                                )
                            )

                            OutlinedTextField(
                                value = numeroVeces,
                                onValueChange = { numeroVeces = it },
                                label = {
                                    Text(
                                        "Número de veces", fontFamily = GagalinFontFamily,
                                        color = Color.Black
                                    )
                                },
                                textStyle = TextStyle(color = Color.Black),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Black,
                                    unfocusedIndicatorColor = Color.Black,
                                    focusedLabelColor = Color.Black,
                                    unfocusedLabelColor = Color.Black
                                )
                            )

                            OutlinedTextField(
                                value = hora,
                                onValueChange = { hora = it },
                                label = {
                                    Text(
                                        "Hora (HH:mm)", fontFamily = GagalinFontFamily,
                                        color = Color.Black
                                    )
                                },
                                textStyle = TextStyle(color = Color.Black),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Black,
                                    unfocusedIndicatorColor = Color.Black,
                                    focusedLabelColor = Color.Black,
                                    unfocusedLabelColor = Color.Black
                                )
                            )

                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp)
                            ) {
                                Button(
                                    onClick = { navController.popBackStack() },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "Cancelar",
                                        fontFamily = GagalinFontFamily,
                                        color = Color.White
                                    )
                                }

                                Button(
                                    onClick = {
                                        when {
                                            titulo.isEmpty() -> {
                                                coroutineScope.launch {
                                                    snackbarHostState.showSnackbar("El título no puede estar vacío")
                                                }
                                            }
                                            numeroVeces.isEmpty() || numeroVeces.toIntOrNull() == null -> {
                                                coroutineScope.launch {
                                                    snackbarHostState.showSnackbar("Número de veces debe ser un número válido")
                                                }
                                            }
                                            hora.isEmpty() || !isValidTimeFormat(hora) -> {
                                                coroutineScope.launch {
                                                    snackbarHostState.showSnackbar("La hora debe tener el formato HH:mm")
                                                }
                                            }
                                            else -> {
                                                val updatedTask = mapOf(
                                                    "titulo" to titulo,
                                                    "numeroVeces" to numeroVeces.toInt(),
                                                    "hora" to hora,
                                                    "progresoActual" to progresoActual
                                                )

                                                FirebaseFirestore.getInstance()
                                                    .collection("tasks")
                                                    .document(documentId)
                                                    .update(updatedTask)
                                                    .addOnSuccessListener {
                                                        coroutineScope.launch {
                                                            snackbarHostState.showSnackbar("Tarea actualizada con éxito")
                                                        }
                                                        onTaskUpdated()
                                                    }
                                                    .addOnFailureListener { exception ->
                                                        coroutineScope.launch {
                                                            snackbarHostState.showSnackbar("Error: ${exception.message}")
                                                        }
                                                    }
                                            }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "Aceptar", fontFamily = GagalinFontFamily,
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}