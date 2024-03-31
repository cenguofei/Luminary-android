package com.example.lunimary.ui.home

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.lunimary.base.checkLogin
import com.example.lunimary.base.notLogin
import com.example.lunimary.ui.HOME_ROOT
import com.example.lunimary.ui.LunimaryAppState
import com.example.lunimary.ui.MESSAGE_ROOT
import com.example.lunimary.ui.TopLevelDestination
import com.example.lunimary.ui.USER_ROOT
import com.example.lunimary.ui.message.MessageScreen
import com.example.lunimary.ui.user.UserDetailScreen

fun NavGraphBuilder.topLevelScreens(appState: LunimaryAppState) {
    composable(
        route = TopLevelDestination.Home.route,
        arguments = listOf(
            navArgument("topScreen") {
                defaultValue = HOME_ROOT
                type = NavType.StringType
            }
        )
    ) {
        TopLevelScreens(
            appState = appState
        )
    }
}

@Composable
fun TopLevelScreens(appState: LunimaryAppState) {
    val selectedBottomTab = appState.selectedBottomTab
    Scaffold(
        modifier = Modifier,
        containerColor = Color.Transparent,
        bottomBar = {
            HomeBottomAppBar(
                selectedBottomTab = selectedBottomTab.value,
                updateBottomSelectedState = appState::updateSelectedBottomTab
            )
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
                    onSearchClick = { appState.navToSearch() },
                    appState = appState,
                    onLoginClick = {
                        appState.navToLogin()
                    },
                    onItemClick = { appState.navToBrowse(it) }
                )
            }

            TopLevelDestination.Message -> {
                if (notLogin()) {
                    appState.navToLogin(true)
                } else {
                    MessageScreen(
                        modifier = paddingModifier
                    )
                }
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