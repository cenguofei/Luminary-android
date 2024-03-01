package com.example.lunimary.ui.home

import androidx.compose.foundation.layout.Arrangement.SpaceAround
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.lunimary.ui.TopLevelDestination

@Composable
fun HomeBottomAppBar(
    selectedBottomTab: MutableState<TopLevelDestination>,
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

private fun updateBottomSelectedState(
    selectedBottomTab: MutableState<TopLevelDestination>,
    tab: TopLevelDestination
) {
    if (selectedBottomTab.value != tab) {
        selectedBottomTab.value = tab
    }
}