package com.iab.dailyhabit.screens.perfil

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.iab.dailyhabit.R
import com.iab.dailyhabit.screens.tareas.GagalinFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PerfilScreen(navController: NavController) {
    var showProgressBar by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }
    var sliderValue by remember { mutableStateOf(0f) }
    // Para simular el progreso de cerradode sesión
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .padding(top = 32.dp, bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.dailyhabitlogo),
                contentDescription = "App Logo",
                modifier = Modifier.size(100.dp)
            )
        }
        // Espaciado entre el logo y el botón
        Spacer(modifier = Modifier.height(16.dp))
        // Botón de cerrar sesión
        Button(
            onClick = {
                showProgressBar = true
                coroutineScope.launch {
                    // Simular progreso durante 3 segundos
                    for (i in 1..100) {
                        delay(15) // 30ms * 100 iteraciones = 3 segundos
                        progress = i / 100f
                    }
                    FirebaseAuth.getInstance().signOut()
                    // Eliminar el historial de navegación
                    navController.navigate("login") {
                        popUpTo("login") {
                            inclusive = true
                        }
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF57C00)), // Color naranja
            modifier = Modifier
                .width(200.dp)
                .height(50.dp)
        ) {
            Text(
                text = "Cerrar sesión",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Mostrar barra de progreso y texto "Cerrando sesión" si showProgressBar es true
        if (showProgressBar) {
            Spacer(modifier = Modifier.height(16.dp))

            // Texto de "Cerrando sesión"
            Text(
                text = "Cerrando sesión",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Barra de progreso
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f) // Ancho de la barra
                    .height(8.dp) // Altura de la barra
                    .background(Color.Gray, shape = RoundedCornerShape(4.dp)) // Fondo gris
            ) {
                LinearProgressIndicator(
                    progress = progress, // Progreso actual
                    color = Color(0xFFF57C00), // Color de progreso
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                )
            }
        }

        // Espaciado entre la barra de progreso y el slider
        Spacer(modifier = Modifier.height(32.dp))

        // Texto para el slider
        Text(
            text = "Recuerde revisar sus Tareas Pendientes.\nDía de hoy:",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = com.iab.dailyhabit.screens.login.GagalinFontFamily,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Slider para ajustar la prioridad
        Slider(
            value = sliderValue,
            onValueChange = { sliderValue = it },
            valueRange = 0f..100f,
            steps = 3, // 4 intervalos en total (0-25-50-75-100)
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFFF57C00), // Color del thumb
                activeTrackColor = Color(0xFFEF5350), // Color de la barra activa
                inactiveTrackColor = Color.Gray // Color de la barra inactiva
            ),
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        // Texto que cambia dinámicamente con el slider
        Text(
            text = when {
                sliderValue <= 25f -> "Prioridad Baja"
                sliderValue <= 50f -> "Prioridad Media"
                sliderValue <= 75f -> "Prioridad Alta"
                else -> "URGENTE"
            },
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = com.iab.dailyhabit.screens.login.GagalinFontFamily,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}