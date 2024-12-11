package com.iab.dailyhabit.screens.tareas

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.iab.dailyhabit.R
import com.iab.dailyhabit.model.Tarea
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TareasPendientesScreen(navController: NavController) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid ?: ""
    val tareas = remember { mutableStateListOf<Pair<String, Tarea>>() }
    val selectedTareas = remember { mutableStateMapOf<String, Boolean>() }
    val coroutineScope = rememberCoroutineScope()
    val GagalinFontFamily = FontFamily(Font(R.font.gagalin))
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showNoSelectionDialog by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            FirebaseFirestore.getInstance()
                .collection("tasks")
                .whereEqualTo("idUsuario", userId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        coroutineScope.launch {
                        }
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        tareas.clear()
                        for (document in snapshot.documents) {
                            val tarea = Tarea(
                                titulo = document.getString("titulo") ?: "",
                                numeroVeces = document.getLong("numeroVeces")?.toInt() ?: 0,
                                progresoActual = document.getLong("progresoActual")?.toInt() ?: 0,
                                hora = document.getString("hora") ?: "",
                                idUsuario = document.getString("idUsuario") ?: ""
                            )
                            tareas.add(Pair(document.id, tarea))
                            selectedTareas[document.id] = false
                        }
                    }
                }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "TAREAS PENDIENTES",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = GagalinFontFamily,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Black)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            tareas.forEach { (documentId, tarea) ->
                TaskCard(
                    tarea = tarea,
                    isSelected = selectedTareas[documentId] ?: false,
                    onSelectionChanged = { isSelected ->
                        selectedTareas[documentId] = isSelected
                    },
                    onTaskUpdated = { updatedTask ->
                        FirebaseFirestore.getInstance()
                            .collection("tasks")
                            .document(documentId)
                            .update("progresoActual", updatedTask.progresoActual)
                    },
                    onTaskEditRequested = {
                        navController.navigate("editar_tarea_screen/$documentId")
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val selectedIds = selectedTareas.filterValues { it }.keys
                    if (selectedIds.isEmpty()) {
                        showNoSelectionDialog = true
                    } else {
                        showDeleteDialog = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF57C00)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "Borrar Tareas Seleccionadas",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontFamily = GagalinFontFamily,
                    fontWeight = FontWeight.Bold
                )
            }

            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = {
                        Text(
                            text = "Confirmación",
                            fontFamily = GagalinFontFamily,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    },
                    text = {
                        Text(
                            text = "¿Estás seguro de que deseas borrar las tareas seleccionadas?",
                            fontFamily = GagalinFontFamily,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                val selectedIds = selectedTareas.filterValues { it }.keys
                                selectedIds.forEach { id ->
                                    FirebaseFirestore.getInstance()
                                        .collection("tasks")
                                        .document(id)
                                        .delete()
                                }
                                selectedTareas.clear()
                                showDeleteDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6D213C))
                        ) {
                            Text(
                                text = "Aceptar",
                                color = Color.White,
                                fontFamily = GagalinFontFamily,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = { showDeleteDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Text(
                                text = "Cancelar",
                                color = Color.White,
                                fontFamily = GagalinFontFamily,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    containerColor = Color.White,
                    tonalElevation = 8.dp
                )
            }


            if (showNoSelectionDialog) {
                AlertDialog(
                    onDismissRequest = { showNoSelectionDialog = false },
                    title = {
                        Text(
                            text = "Sin Selección",
                            fontFamily = GagalinFontFamily,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    },
                    text = {
                        Text(
                            text = "No hay tareas seleccionadas para borrar.",
                            fontFamily = GagalinFontFamily,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = { showNoSelectionDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6D213C))
                        ) {
                            Text(
                                text = "OK",
                                color = Color.White,
                                fontFamily = GagalinFontFamily,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    containerColor = Color.White,
                    tonalElevation = 8.dp
                )
            }
        }
    }
}

@Composable
fun TaskCard(
    tarea: Tarea,
    isSelected: Boolean,
    onSelectionChanged: (Boolean) -> Unit,
    onTaskUpdated: (Tarea) -> Unit,
    onTaskEditRequested: (Tarea) -> Unit
) {
    val GagalinFontFamily = FontFamily(Font(R.font.gagalin))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF57C00))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color(0xFFF57C00), shape = RoundedCornerShape(16.dp)),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = tarea.titulo.uppercase(),
                fontWeight = FontWeight.Bold,
                fontFamily = GagalinFontFamily,
                fontSize = 28.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "Hora: ${tarea.hora}",
                fontSize = 18.sp,
                fontFamily = GagalinFontFamily,
                color = Color.Black
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                LinearProgressIndicator(
                    progress = tarea.progresoActual.toFloat() / tarea.numeroVeces.toFloat(),
                    color = Color(0xFFEF5350),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color(0xFFBDBDBD))
                )
                Text(
                    text = "${tarea.progresoActual}/${tarea.numeroVeces}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = GagalinFontFamily,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        if (tarea.progresoActual < tarea.numeroVeces) {
                            val updatedTask = tarea.copy(progresoActual = tarea.progresoActual + 1)
                            onTaskUpdated(updatedTask)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6D213C)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "✓", fontSize = 20.sp, color = Color.White)
                }
                Button(
                    onClick = {
                        if (tarea.progresoActual > 0) {
                            val updatedTask = tarea.copy(progresoActual = tarea.progresoActual - 1)
                            onTaskUpdated(updatedTask)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6D213C)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "-", fontSize = 20.sp, color = Color.White)
                }
                Button(
                    onClick = { onTaskEditRequested(tarea) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6D213C)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Editar", fontFamily = GagalinFontFamily, color = Color.White)
                }

                Switch(
                    checked = isSelected,
                    onCheckedChange = { onSelectionChanged(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF6D213C),
                        uncheckedThumbColor = Color.Gray,
                        checkedTrackColor = Color(0xFF954964),
                        uncheckedTrackColor = Color(0xFFBDBDBD)
                    )
                )
            }
        }
    }
}