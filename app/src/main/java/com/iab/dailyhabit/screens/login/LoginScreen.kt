package com.iab.dailyhabit.screens.login

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.iab.dailyhabit.R
import kotlinx.coroutines.launch

val GagalinFontFamily = FontFamily(Font(R.font.gagalin))
val token = "57995066176-7shf79h2cdbpi67s5u6g7a1u8e0jtglg.apps.googleusercontent.com"
@Composable
fun LoginScreen(navController: NavController, activity: Activity) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    var isCreatingAccount by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        handleGoogleSignInResult(
            task,
            onSuccess = { user ->
                navController.navigate("tareas_pendientes/${user.uid}")
            },
            onError = { errorMessage ->
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = errorMessage,
                        actionLabel = "OK"
                    )
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF121212))
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "DAILY HABIT",
                    fontSize = 36.sp,
                    fontFamily = GagalinFontFamily,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.dailyhabitlogo),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(120.dp)
                        .padding(bottom = 24.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1E1E1E), shape = RoundedCornerShape(12.dp))
                        .shadow(8.dp, shape = RoundedCornerShape(12.dp))
                        .padding(24.dp)
                ) {
                    Column {
                        OutlinedTextField(
                            value = email.value,
                            onValueChange = { email.value = it },
                            label = { Text("Email", color = Color.Gray) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = "Email Icon",
                                    tint = Color.Gray
                                )
                            },
                            textStyle = TextStyle(color = Color.White),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedLabelColor = Color.Gray,
                                focusedLabelColor = Color.White,
                                focusedIndicatorColor = Color.White,
                                unfocusedIndicatorColor = Color.Gray
                            )
                        )

                        OutlinedTextField(
                            value = password.value,
                            onValueChange = { password.value = it },
                            label = { Text("Password", color = Color.Gray) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Password Icon",
                                    tint = Color.Gray
                                )
                            },
                            textStyle = TextStyle(color = Color.White),
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedLabelColor = Color.Gray,
                                focusedLabelColor = Color.White,
                                focusedIndicatorColor = Color.White,
                                unfocusedIndicatorColor = Color.Gray
                            )
                        )

                        Button(
                            onClick = {
                                if (email.value.isBlank() || password.value.isBlank()) {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Por favor, complete todos los campos",
                                            actionLabel = "OK"
                                        )
                                    }
                                } else {
                                    if (isCreatingAccount) {
                                        createUserWithEmailAndPassword(
                                            email = email.value,
                                            password = password.value,
                                            onSuccess = {
                                                val currentUser = FirebaseAuth.getInstance().currentUser
                                                val userId = currentUser?.uid ?: ""

                                                if (userId.isNotEmpty()) {
                                                    navController.navigate("tareas_pendientes/$userId")
                                                } else {
                                                    coroutineScope.launch {
                                                        snackbarHostState.showSnackbar(
                                                            message = "Error al obtener el ID del usuario",
                                                            actionLabel = "OK"
                                                        )
                                                    }
                                                }
                                            },
                                            onError = { errorMessage ->
                                                coroutineScope.launch {
                                                    snackbarHostState.showSnackbar(
                                                        message = errorMessage,
                                                        actionLabel = "OK"
                                                    )
                                                }
                                            }
                                        )
                                    } else {
                                        signInWithEmailAndPassword(
                                            email = email.value,
                                            password = password.value,
                                            onSuccess = {
                                                val currentUser = FirebaseAuth.getInstance().currentUser
                                                val userId = currentUser?.uid ?: ""

                                                if (userId.isNotEmpty()) {
                                                    navController.navigate("tareas_pendientes/$userId")
                                                } else {
                                                    coroutineScope.launch {
                                                        snackbarHostState.showSnackbar(
                                                            message = "Error al obtener el ID del usuario",
                                                            actionLabel = "OK"
                                                        )
                                                    }
                                                }
                                            },
                                            onError = { errorMessage ->
                                                coroutineScope.launch {
                                                    snackbarHostState.showSnackbar(
                                                        message = errorMessage,
                                                        actionLabel = "OK"
                                                    )
                                                }
                                            }
                                        )
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = if (isCreatingAccount) "Sign Up" else "Login",
                                color = Color.White
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (isCreatingAccount) "Already have an account?" else "Don't have an account?",
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isCreatingAccount) "Sign in" else "Create an account",
                        color = Color(0xFFFF9800),
                        modifier = Modifier.clickable { isCreatingAccount = !isCreatingAccount }
                    )
                }

                Button(
                    onClick = {
                        signInWithGoogle(
                            activity = activity,
                            launcher = launcher,
                            onSuccess = { user ->
                                navController.navigate("tareas_pendientes/${user.uid}")
                            },
                            onError = { errorMessage ->
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = errorMessage,
                                        actionLabel = "OK"
                                    )
                                }
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.google_icon),
                            contentDescription = "Google Icon",
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Login with Google",
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
fun createUserWithEmailAndPassword(
    email: String,
    password: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                onError(task.exception?.message ?: "Error desconocido")
            }
        }
}

fun signInWithEmailAndPassword(
    email: String,
    password: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                onError(task.exception?.message ?: "Error desconocido")
            }
        }
}

fun signInWithGoogle(
    activity: Activity,
    launcher: ActivityResultLauncher<Intent>,
    onSuccess: (FirebaseUser) -> Unit,
    onError: (String) -> Unit
) {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("57995066176-7shf79h2cdbpi67s5u6g7a1u8e0jtglg.apps.googleusercontent.com")
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(activity, gso)
    // Cierra cualquier sesión previa para forzar el selector de cuentas
    googleSignInClient.signOut().addOnCompleteListener {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }
}

fun handleGoogleSignInResult(
    task: Task<GoogleSignInAccount>,
    onSuccess: (FirebaseUser) -> Unit,
    onError: (String) -> Unit
) {
    try {
        val account = task.getResult(ApiException::class.java)!!
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { authTask ->
                if (authTask.isSuccessful) {
                    onSuccess(authTask.result?.user!!)
                } else {
                    onError(authTask.exception?.message ?: "Google Sign-In failed")
                }
            }
    } catch (e: ApiException) {
        onError("Google Sign-In failed: ${e.localizedMessage}")
    }
}
