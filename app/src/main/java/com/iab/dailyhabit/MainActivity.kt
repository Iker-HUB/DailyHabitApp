package com.iab.dailyhabit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.iab.dailyhabit.navigation.Navigation
import com.iab.dailyhabit.screens.splash.SplashScreen
import com.iab.dailyhabit.ui.theme.DailyHabitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DailyHabitTheme {
                Surface(
                    color = colorScheme.background
                ) {
                    Navigation()
                }
            }
        }
    }
}
