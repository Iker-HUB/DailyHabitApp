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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.iab.dailyhabit.model.Tarea
import kotlinx.coroutines.launch
val GagalinFontFamily = FontFamily(Font(R.font.gagalin))

@Composable
fun AgregarTareaScreen(
    navController: NavController,
    userId: String,
    onTaskCreated: () -> Unit
) {
    val titulo = remember { mutableStateOf("") }
    val numeroVeces = remember { mutableStateOf("") }
    val hora = remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()


    fun isValidTimeFormat(input: String): Boolean {
        val regex = Regex("^([01]\\d|2[0-3]):([0-5]\\d)$")
        return regex.matches(input)
    }
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
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Text(
                    text = "Agregar Tarea",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = GagalinFontFamily,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 24.dp)
                )


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF57C00), shape = RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column {

                        OutlinedTextField(
                            value = titulo.value,
                            onValueChange = { titulo.value = it },
                            label = {
                                Text(
                                    "Título de la tarea",
                                    color = Color.Black,
                                    fontFamily = GagalinFontFamily
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
                            value = numeroVeces.value,
                            onValueChange = { numeroVeces.value = it },
                            label = {
                                Text(
                                    "Número de veces",
                                    color = Color.Black,
                                    fontFamily = GagalinFontFamily
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
                            value = hora.value,
                            onValueChange = { hora.value = it },
                            label = {
                                Text(
                                    "Hora (HH:mm)",
                                    color = Color.Black,
                                    fontFamily = GagalinFontFamily
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
                                    color = Color.Black,
                                    fontFamily = GagalinFontFamily
                                )
                            }
                            Button(
                                onClick = {
                                    when {
                                        titulo.value.isEmpty() -> {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("El título no puede estar vacío")
                                            }
                                        }
                                        numeroVeces.value.isEmpty() || numeroVeces.value.toIntOrNull() == null -> {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Número de veces debe ser un número válido")
                                            }
                                        }
                                        hora.value.isEmpty() || !isValidTimeFormat(hora.value) -> {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("La hora debe tener el formato HH:mm")
                                            }
                                        }
                                        else -> {
                                            val nuevaTarea = Tarea(
                                                titulo = titulo.value,
                                                numeroVeces = numeroVeces.value.toInt(),
                                                progresoActual = 0,
                                                hora = hora.value,
                                                idUsuario = userId
                                            )
                                            agregarTareaAFirebase(
                                                tarea = nuevaTarea,
                                                onSuccess = {
                                                    coroutineScope.launch {
                                                        snackbarHostState.showSnackbar("Tarea creada con éxito")
                                                    }
                                                    onTaskCreated()
                                                },
                                                onError = { errorMessage ->
                                                    coroutineScope.launch {
                                                        snackbarHostState.showSnackbar("Error: $errorMessage")
                                                    }
                                                }
                                            )
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "Aceptar",
                                    color = Color.Black,
                                    fontFamily = GagalinFontFamily
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun agregarTareaAFirebase(
    tarea: Tarea,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    db.collection("tasks")
        .add(tarea.toMap())
        .addOnSuccessListener {
            onSuccess()
        }
        .addOnFailureListener { exception ->
            onError(exception.message ?: "Error desconocido")
        }
}