package com.example.lunimary.ui.viewuser

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import com.example.lunimary.R
import com.example.lunimary.design.BackButton
import com.example.lunimary.models.User
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
        ViewUserScreen(
            onBack = appState::popBackStack,
            user = user,
            appState = appState
        )
    }
}

@Composable
fun ViewUserScreen(
    user: User,
    onBack: () -> Unit,
    appState: LunimaryAppState,
) {
    val viewUserViewModel: ViewUserViewModel = viewModel()
    viewUserViewModel.setUser(user)
    Box(modifier = Modifier.fillMaxSize()) {
        ViewUserBackground(
            modifier = Modifier,
            backgroundUrl = user.realBackgroundUrl()
        )
        BackButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(start = 16.dp),
            onClick = onBack
        )
        ViewUserDetailContent(
            modifier = Modifier,
            user = user,
            viewModel = viewUserViewModel,
            appState = appState,
        )
    }
}

@Composable
fun ViewUserBackground(
    modifier: Modifier,
    backgroundUrl: String
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        AsyncImage(
            model = backgroundUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            placeholder = painterResource(id = R.drawable.user_background),
            error = painterResource(id = R.drawable.user_background),
            fallback = painterResource(id = R.drawable.user_background),
        )
    }
}
