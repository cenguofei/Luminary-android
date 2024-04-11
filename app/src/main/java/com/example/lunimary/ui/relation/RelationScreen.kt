package com.example.lunimary.ui.relation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.lunimary.ui.LunimaryAppState
import com.example.lunimary.ui.Screens
import com.example.lunimary.ui.common.getRelationPage

fun NavGraphBuilder.relationScreen(appState: LunimaryAppState) {
    composable(
        route = Screens.Relation.route
    ) {
        val pageType = getRelationPage()
        val relationViewModel: RelationViewModel = viewModel()
        RelationScreenRoute(
            pageType = pageType,
            onBack = appState::popBackStack,
            relationViewModel = relationViewModel,
            onItemClick = { appState.navToViewUser(it, Screens.Relation.route) }
        )
    }
}