package com.example.lunimary.ui.viewuser

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.lunimary.model.User
import com.example.lunimary.ui.LunimaryAppState
import com.example.lunimary.ui.Screens
import com.example.lunimary.ui.common.getNavViewUser

fun NavGraphBuilder.viewUserScreen(
    appState: LunimaryAppState
) {
    composable(
        route = Screens.ViewUser.route
    ) {
        val user = getNavViewUser()
        if (user == User.NONE) {
            appState.navToHome(Screens.ViewUser.route)
            return@composable
        }
        ViewUserScreenRoute(
            onBack = appState::popBackStack,
            user = user,
            appState = appState
        )
    }
}


