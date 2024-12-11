package com.iab.dailyhabit.navigation
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.iab.dailyhabit.screens.login.GagalinFontFamily
import com.iab.dailyhabit.screens.login.LoginScreen
import com.iab.dailyhabit.screens.perfil.PerfilScreen
import com.iab.dailyhabit.screens.splash.SplashScreen
import com.iab.dailyhabit.screens.tareas.AgregarTareaScreen
import com.iab.dailyhabit.screens.tareas.EditarTareaScreen
import com.iab.dailyhabit.screens.tareas.TareasPendientesScreen


@Composable
fun Navigation() {
    val navController = rememberNavController()
    val currentUser = FirebaseAuth.getInstance().currentUser

    NavHost(
        navController = navController,
        startDestination = if (currentUser == null) "splash" else "tareas_pendientes/${currentUser.uid}"
    ) {
        composable("splash") {
            SplashScreen(navController)
        }

        composable("login") {
            LoginScreen(navController,activity = LocalContext.current as Activity)
        }

        composable(
            route = "tareas_pendientes/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            ScreenWithNavigationBar(navController = navController) {
                TareasPendientesScreen(navController = navController)
            }
        }
        composable(
            route = "editar_tarea_screen/{documentId}",
            arguments = listOf(
                navArgument("documentId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val documentId = backStackEntry.arguments?.getString("documentId") ?: ""
            EditarTareaScreen(
                navController = navController,
                documentId = documentId,
                onTaskUpdated = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = "agregar_tareas_screen/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userIdParam = backStackEntry.arguments?.getString("userId") ?: ""
            ScreenWithNavigationBar(navController = navController) {
                AgregarTareaScreen(
                    navController = navController,
                    userId = userIdParam,
                    onTaskCreated = {
                        navController.popBackStack()
                    }
                )
            }
        }
        composable(
            route = "perfil/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userIdParam = backStackEntry.arguments?.getString("userId") ?: ""
            ScreenWithNavigationBar(navController = navController) {
                PerfilScreen(navController = navController)
            }
        }
    }
}


// Envoltura para las pantallas que incluyen la NavigationBar
@Composable
fun ScreenWithNavigationBar(
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = { NavigationBar(navController = navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            content()
        }
    }
}

// Componente de NavigationBar
@Composable
fun NavigationBar(navController: NavHostController) {
    NavigationBar(
        containerColor = Color(0xFFF57C00),
        modifier = Modifier.height(56.dp)
    ) {
        // Botón de TareasPendientes
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Tareas Pendientes", tint = Color.Black) },
            selected = false,
            label = { Text(text = "Tareas", fontSize = 12.sp, fontFamily = GagalinFontFamily, fontWeight = FontWeight.Bold,color = Color.Black) },
            onClick = { navController.navigate("tareas_pendientes/${FirebaseAuth.getInstance().currentUser?.uid ?: ""}") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.Gray
            )
        )
        // Botón de AgregarTareaScreen
        NavigationBarItem(
            icon = { Icon(Icons.Default.Add, contentDescription = "Agregar Tarea", tint = Color.Black) },
            selected = false,
            label = { Text(text = "Agregar Tarea", fontSize = 12.sp, fontFamily = GagalinFontFamily, fontWeight = FontWeight.Bold,color = Color.Black) },
            onClick = { navController.navigate("agregar_tareas_screen/${FirebaseAuth.getInstance().currentUser?.uid ?: ""}") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.Gray
            )
        )
        // Botón de PerfilScreen
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil", tint = Color.Black) },
            selected = false,
            label = { Text(text = "Perfil", fontSize = 12.sp, fontFamily = GagalinFontFamily, fontWeight = FontWeight.Bold,color = Color.Black) },
            onClick = { navController.navigate("perfil/${FirebaseAuth.getInstance().currentUser?.uid ?: ""}") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.Gray
            )
        )
    }
}