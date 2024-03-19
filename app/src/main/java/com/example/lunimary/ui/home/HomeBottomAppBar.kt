package com.example.lunimary.ui.home

import androidx.compose.foundation.layout.Arrangement.SpaceAround
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.lunimary.design.GradientColors
import com.example.lunimary.design.LunimaryGradientBackground
import com.example.lunimary.ui.TopLevelDestination

@Composable
fun HomeBottomAppBar(
    selectedBottomTab: MutableState<TopLevelDestination>,
) {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.9f),
        modifier = Modifier
            .fillMaxWidth()
            .height(bottomBarHeight)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            horizontalArrangement = SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    updateBottomSelectedState(selectedBottomTab, TopLevelDestination.Home)
                }
            ) {
                Icon(
                    imageVector = TopLevelDestination.Home.icon,
                    contentDescription = null,
                    tint = TopLevelDestination.Home.tintColor(selectedBottomTab)
                )
            }

            IconButton(
                onClick = {
                    updateBottomSelectedState(selectedBottomTab, TopLevelDestination.Message)
                }
            ) {
                Icon(
                    imageVector = TopLevelDestination.Message.icon,
                    contentDescription = null,
                    tint = TopLevelDestination.Message.tintColor(selectedBottomTab)
                )
            }

            IconButton(
                onClick = {
                    updateBottomSelectedState(selectedBottomTab, TopLevelDestination.User)
                }
            ) {
                Icon(
                    imageVector = TopLevelDestination.User.icon,
                    contentDescription = null,
                    tint = TopLevelDestination.User.tintColor(selectedBottomTab)
                )
            }
        }
    }
}

private fun updateBottomSelectedState(
    selectedBottomTab: MutableState<TopLevelDestination>,
    tab: TopLevelDestination
) {
    if (selectedBottomTab.value != tab) {
        selectedBottomTab.value = tab
    }
}

val bottomBarHeight: Dp  = 55.dp