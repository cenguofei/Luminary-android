package com.example.lunimary.ui.user.information

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import com.example.lunimary.base.niceDateToDay
import com.example.lunimary.base.niceDateUtilDayToLong
import com.example.lunimary.models.User
import com.example.lunimary.models.byText

@Composable
fun BottomDrawer(
    showBottomDrawer: MutableState<Boolean>,
    editItemType: MutableState<EditItemType>,
    initialText: MutableState<Any>,
    newUser: MutableState<User>
) {
    AnimatedVisibility(
        visible = showBottomDrawer.value,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Surface(
            onClick = { showBottomDrawer.value = !showBottomDrawer.value },
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxSize()
                .animateContentSize(
                    animationSpec = tween(durationMillis = 300)
                ),
            color = Color.Black.copy(alpha = 0.2f)
        ) {
            AnimatedVisibility(
                visible = showBottomDrawer.value,
                enter = slideInVertically { it / 5 },
                exit = slideOutVertically { it / 5 },
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    val initText = if (editItemType.value == EditItemType.Birth) {
                        newUser.value.birth.niceDateToDay
                    } else {
                        initialText.value.toString()
                    }
                    Surface(
                        Modifier
                            .fillMaxWidth()
                            .imePadding()
                    ) {
                        EditInformationItem(
                            initialText = initText,
                            editItemType = editItemType.value,
                            onConfirm = {
                                showBottomDrawer.value = false
                                when (editItemType.value) {
                                    EditItemType.Username -> {}
                                    EditItemType.Sex -> {
                                        newUser.value = newUser.value.copy(sex = byText(it))
                                    }

                                    EditItemType.Birth -> {
                                        newUser.value =
                                            newUser.value.copy(birth = it.niceDateUtilDayToLong())
                                    }

                                    EditItemType.Signature -> {
                                        newUser.value = newUser.value.copy(signature = it)
                                    }

                                    EditItemType.BlogAddress -> {
                                        newUser.value = newUser.value.copy(blogAddress = it)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}