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
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.lunimary.ui.HOME_ROOT
import com.example.lunimary.ui.LunimaryAppState
import com.example.lunimary.ui.MESSAGE_ROOT
import com.example.lunimary.ui.TopLevelDestination
import com.example.lunimary.ui.USER_ROOT
import com.example.lunimary.ui.message.MessageScreen
import com.example.lunimary.ui.user.UserDetailScreen
import com.example.lunimary.util.checkLogin
import com.example.lunimary.util.notLogin

fun NavGraphBuilder.topLevelScreens(appState: LunimaryAppState) {
    composable(
        route = TopLevelDestination.Home.route,
        arguments = listOf(
            navArgument("topScreen") {
                defaultValue = HOME_ROOT
                type = NavType.StringType
            }
        )
    ) { navBackStackEntry ->
        val topType = when(
            navBackStackEntry.arguments?.getString("topScreen") ?: HOME_ROOT
        ) {
            MESSAGE_ROOT -> { TopLevelDestination.Message }
            USER_ROOT -> { TopLevelDestination.User }
            else -> TopLevelDestination.Home
        }
        TopLevelScreens(
            appState = appState,
            destination = topType
        )
    }
}

@Composable
fun TopLevelScreens(appState: LunimaryAppState, destination: TopLevelDestination) {
    val selectedBottomTab = rememberSaveable(stateSaver = Saver(
        save = { it.toString() },
        restore = { TopLevelDestination.valueOf(it) }
    )) { mutableStateOf(destination) }

    Scaffold(
        modifier = Modifier,
        containerColor = Color.Transparent,
        bottomBar = {
            HomeBottomAppBar(selectedBottomTab = selectedBottomTab)
        }
    ) { paddingValues ->
        val paddingModifier = Modifier
            .padding(paddingValues)
            .consumeWindowInsets(paddingValues)
        when (selectedBottomTab.value) {
            TopLevelDestination.Home -> {
                HomeScreen(
                    modifier = paddingModifier,
                    onAddClick = {
                        checkLogin(
                            isLogin = { appState.navToEdit() },
                            isLogout = { appState.navToLogin() }
                        )
                    },
                    onSearchClick = {},
                    appState = appState,
                    onLoginClick = {
                        appState.navToLogin()
                    },
                    onItemClick = { appState.navToBrowse(it) }
                )
            }

            TopLevelDestination.Message -> {
                MessageScreen(modifier = paddingModifier)
            }

            TopLevelDestination.User -> {
                if (notLogin()) {
                    appState.navToLogin(true)
                } else {
                    UserDetailScreen(
                        onOpenMenu = {
                            appState.navToSettings()
                        },
                        onDraftClick = { appState.navToDraft() },
                        appState = appState
                    )
                }
            }
        }
    }
}