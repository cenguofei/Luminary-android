package com.example.lunimary.ui.privacy

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.lunimary.ui.LunimaryAppState
import com.example.lunimary.ui.Screens

fun NavGraphBuilder.privacyScreen(appState: LunimaryAppState) {
    composable(
        route = Screens.PrivacyProtocol.route
    ) {
        val viewModel: PrivacyViewModel = viewModel()
        PrivacyScreenRoute(
            onBack = appState::popBackStack,
            viewModel = viewModel
        )
    }
}

