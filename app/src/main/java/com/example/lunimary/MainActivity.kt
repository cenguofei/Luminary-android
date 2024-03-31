package com.example.lunimary

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lunimary.base.mmkv.DarkThemeSetting
import com.example.lunimary.design.LunimaryGradientBackground
import com.example.lunimary.design.theme.LunimaryTheme
import com.example.lunimary.ui.LunimaryApp
import com.example.lunimary.ui.TopLevelDestination
import com.example.lunimary.ui.login.UserViewModel
import com.example.lunimary.ui.rememberAppState
import com.example.lunimary.base.UserState
import com.example.lunimary.util.logd
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val userViewModel: UserViewModel = viewModel()
            userViewModel.checkLogin(
                isLogin = { UserState.updateLoginState(true, "onActivityResumed") },
                logout = { UserState.updateLoginState(false, "onActivityResumed") }
            )
            val systemUiController = rememberSystemUiController()
            val appState = rememberAppState(
                systemUiController = systemUiController,
                windowSizeClass = calculateWindowSizeClass(this),
                userViewModel = userViewModel
            )
            "appState=$appState".logd("appState")
            val darkTheme = DarkThemeConfig(themeSetting = appState.darkThemeSettingState.value)
            LaunchedEffect(systemUiController, darkTheme) {
                systemUiController.systemBarsDarkContentEnabled = !darkTheme
                systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = !darkTheme)
            }
            LunimaryTheme(darkTheme = darkTheme,) {
                LunimaryGradientBackground {
                    LunimaryApp(appState = appState, startScreen = TopLevelDestination.Home)
                }
            }
        }
    }
}

@Composable
private fun DarkThemeConfig(themeSetting: DarkThemeSetting): Boolean {
    return when(themeSetting) {
        DarkThemeSetting.FollowSystem -> isSystemInDarkTheme()
        DarkThemeSetting.DarkMode -> true
        DarkThemeSetting.NightMode -> false
    }
}