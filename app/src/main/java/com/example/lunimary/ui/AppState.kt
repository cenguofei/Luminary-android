package com.example.lunimary.ui

import android.util.Log
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navOptions
import com.example.lunimary.network.NetworkMonitor
import com.example.lunimary.network.NetworkMonitorImpl
import com.example.lunimary.ui.login.UserViewModel
import com.example.lunimary.util.logd
import com.google.accompanist.systemuicontroller.SystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun rememberAppState(
    networkMonitor: NetworkMonitor = NetworkMonitorImpl(LocalContext.current),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
    userViewModel: UserViewModel,
    windowSizeClass: WindowSizeClass,
    systemUiController: SystemUiController
): LunimaryAppState {
    NavigationTrackingSideEffect(navController)
    return remember(
        navController,
        coroutineScope,
        windowSizeClass,
        networkMonitor,
        userViewModel
    ) {
        LunimaryAppState(
            navController = navController,
            coroutineScope = coroutineScope,
            windowSizeClass = windowSizeClass,
            networkMonitor = networkMonitor,
            systemUiController = systemUiController,
            userViewModel = userViewModel
        )
    }
}

@Stable
class LunimaryAppState(
    val navController: NavHostController,
    coroutineScope: CoroutineScope,
    private val windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
    val systemUiController: SystemUiController,
    val userViewModel: UserViewModel
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            TopLevelDestination.Home.route -> TopLevelDestination.Home
            else -> null
        }

    val shouldShowBottomBar: Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val shouldShowNavRail: Boolean get() = !shouldShowBottomBar

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()

    var lastBackClickedTime = System.currentTimeMillis()
    fun popBackStack() {
        if (System.currentTimeMillis() - lastBackClickedTime < 1000){
            return
        }
        navController.popBackStack()
    }

    fun navToLogin() {
        navController.navigate(Screens.Login.route)
    }

    fun navToHome() {
        navController.navigate(
            TopLevelDestination.Home.route,
            navOptions = navOptions {
                popUpTo(TopLevelDestination.Home.route) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        )
    }

    fun navToSettings() {
        navController.navigate(Screens.Settings.route)
    }
}

@Composable
private fun NavigationTrackingSideEffect(navController: NavHostController) {
    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, arguments ->
            Log.v(
                "navigation_events",
                "Foods Destination:${destination.route.toString()},arguments:$arguments"
            )
            val entries = navController.visibleEntries.value.toString()
            Log.v("navigation_events", "visibleEntries=$entries")
        }

        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
}