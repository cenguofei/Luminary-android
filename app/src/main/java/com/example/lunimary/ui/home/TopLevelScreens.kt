package com.example.lunimary.ui.home

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.lunimary.ui.LunimaryAppState
import com.example.lunimary.ui.TopLevelDestination
import com.example.lunimary.ui.message.MessageScreen
import com.example.lunimary.ui.user.UserScreen

fun NavGraphBuilder.topLevelScreens(appState: LunimaryAppState) {
    composable(route = TopLevelDestination.Home.route) {
        TopLevelScreens(
            appState = appState
        )
    }
}

@Composable
fun TopLevelScreens(appState: LunimaryAppState) {
    val selectedBottomTab = rememberSaveable(stateSaver = Saver(
        save = { it.toString() },
        restore = { TopLevelDestination.valueOf(it) }
    )) { mutableStateOf(TopLevelDestination.Home) }

    Scaffold(
        modifier = Modifier,
        containerColor = Color.Transparent,
        bottomBar = {
            HomeBottomAppBar(selectedBottomTab = selectedBottomTab)
        }
    ) { paddingValues ->
        val paddingModifier = Modifier.padding(paddingValues).consumeWindowInsets(paddingValues)
        when(selectedBottomTab.value) {
            TopLevelDestination.Home -> {
                HomeScreen(
                    modifier = paddingModifier,
                    onAddClick = {},
                    onSearchClick = {},
                    appState = appState,
                    onLoginClick = {
                        appState.navToLogin()
                    }
                )
            }
            TopLevelDestination.Message -> {
                MessageScreen(modifier = paddingModifier)
            }
            TopLevelDestination.User -> {
                UserScreen(
                    modifier = paddingModifier,
                    onOpenMenu = {
                        appState.navToSettings()
                    }
                )
            }
        }
    }
}