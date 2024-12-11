package com.iab.dailyhabit.screens.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.iab.dailyhabit.R

val GagalinFontFamily = FontFamily(Font(R.font.gagalin))
@Composable
fun SplashScreen(navController: NavController) {
    var showContent by remember { mutableStateOf(false) }

    // Retraso inicial para activar la animación
    LaunchedEffect(Unit) {
        showContent = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(animationSpec = tween(1000)) + slideInVertically(initialOffsetY = { -100 }),
                exit = fadeOut()
            ) {
                Text(
                    text = "DAILY HABIT",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = GagalinFontFamily,
                    modifier = Modifier.padding(top = 24.dp)
                )
            }

            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(animationSpec = tween(1000)) + scaleIn(initialScale = 0.5f),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .size(350.dp)
                        .background(
                            Color.Black,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.dailyhabitlogo), // Reemplaza con tu recurso
                        contentDescription = "Icono Daily Habit",
                        modifier = Modifier.size(250.dp)
                    )
                }
            }

            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(animationSpec = tween(1500)) + slideInHorizontally(initialOffsetX = { -300 }),
                exit = fadeOut()
            ) {
                Text(
                    text = "Por una vida sin olvidos",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    fontFamily = GagalinFontFamily,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(animationSpec = tween(1500)) + slideInVertically(initialOffsetY = { 300 }),
                exit = fadeOut()
            ) {
                Button(
                    onClick = { navController.navigate("login")},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Ir al Login",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = GagalinFontFamily,
                    )
                }
            }
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(animationSpec = tween(2000)),
                exit = fadeOut()
            ) {
                Text(
                    text = "Desarrollado por:\nIKER ANTÓN BUERA",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontFamily = GagalinFontFamily,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }
        }
    }
}