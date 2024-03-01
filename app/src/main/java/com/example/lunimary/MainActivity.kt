package com.example.lunimary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lunimary.design.LunimaryGradientBackground
import com.example.lunimary.design.theme.LunimaryTheme
import com.example.lunimary.models.Role
import com.example.lunimary.models.Sex
import com.example.lunimary.models.User
import com.example.lunimary.models.UserStatus
import com.example.lunimary.network.NetworkMonitorImpl
import com.example.lunimary.ui.LunimaryApp
import com.example.lunimary.ui.Screens
import com.example.lunimary.ui.TopLevelDestination
import com.example.lunimary.ui.login.UserViewModel
import com.example.lunimary.ui.rememberAppState
import com.example.lunimary.util.UserState
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val userViewModel: UserViewModel = viewModel()
            val systemUiController = rememberSystemUiController()
            val appState = rememberAppState(
                systemUiController = systemUiController,
                windowSizeClass = calculateWindowSizeClass(this),
                userViewModel = userViewModel
            )
            val darkTheme = isSystemInDarkTheme()
            LaunchedEffect(systemUiController, darkTheme) {
                systemUiController.systemBarsDarkContentEnabled = !darkTheme
                systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = !darkTheme)
            }
            LunimaryTheme {
                LunimaryGradientBackground {
                    LunimaryApp(appState = appState, startScreen = TopLevelDestination.Home)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LunimaryTheme {
    }
}