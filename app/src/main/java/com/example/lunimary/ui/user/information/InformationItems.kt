package com.example.lunimary.ui.user.information

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.lunimary.R
import com.example.lunimary.base.network.NetworkResult
import com.example.lunimary.base.niceDateToDay
import com.example.lunimary.design.LinearButton
import com.example.lunimary.design.LoadingWheel
import com.example.lunimary.design.LunimaryDialog
import com.example.lunimary.models.Sex
import com.example.lunimary.models.User
import com.example.lunimary.util.unknownErrorMsg

@Composable
fun ColumnScope.InformationItems(
    user: User,
    headImageSize: Dp,
    informationViewModel: InformationViewModel,
    onBack: () -> Unit,
    newUser: MutableState<User>,
    initialText: MutableState<Any>,
    editItemType: MutableState<EditItemType>,
    showBottomDrawer: MutableState<Boolean>,
    onShowSnackbar: (msg: String, label: String?) -> Unit
) {
    val save = remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    LunimaryDialog(
        text = stringResource(id = R.string.no_save_of_back),
        onConfirmClick = onBack,
        onCancelClick = { },
        openDialog = showDialog
    )
    BackHandler {
        if (!user.equal(newUser.value) && !save.value) {
            showDialog.value = true
        } else {
            onBack()
        }
    }
    Spacer(modifier = Modifier.height(headImageSize / 2))

    val modifierUsernameNotAllowed = stringResource(id = R.string.not_allowed_modifier_username)
    Item(
        itemName = stringResource(id = R.string.username),
        itemContent = newUser.value.username,
        onClick = {
            onShowSnackbar(modifierUsernameNotAllowed, null)
        }
    )

    Item(
        itemName = stringResource(id = R.string.sex),
        itemContent = when (newUser.value.sex) {
            Sex.Sealed -> stringResource(id = R.string.unknown)
            Sex.Male -> stringResource(id = R.string.male)
            Sex.Female -> stringResource(id = R.string.female)
        },
        onClick = {
            initialText.value = newUser.value.sex.text
            editItemType.value = EditItemType.Sex
            showBottomDrawer.value = true
        }
    )

    Item(
        itemName = stringResource(id = R.string.birth),
        itemContent = newUser.value.birth.niceDateToDay,
        onClick = {
            initialText.value = newUser.value.birth.niceDateToDay
            editItemType.value = EditItemType.Birth
            showBottomDrawer.value = true
        }
    )

    Item(
        itemName = stringResource(id = R.string.signature),
        itemContent = newUser.value.signature,
        onClick = {
            initialText.value = newUser.value.signature
            editItemType.value = EditItemType.Signature
            showBottomDrawer.value = true
        }
    )

    Item(
        itemName = stringResource(id = R.string.blog_address),
        itemContent = newUser.value.blogAddress,
        onClick = {
            initialText.value = newUser.value.blogAddress
            editItemType.value = EditItemType.BlogAddress
            showBottomDrawer.value = true
        }
    )

    Spacer(modifier = Modifier.weight(1f))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        LinearButton(
            onClick = {
                if (newUser.value != user) {
                    save.value = true
                    informationViewModel.save(newUser.value)
                }
            },
            text = stringResource(id = R.string.save),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(45.dp)
        )
    }
    Spacer(modifier = Modifier.height(32.dp))
    when (informationViewModel.updateUserState.value) {
        is NetworkResult.Loading -> {
            LoadingWheel()
        }

        is NetworkResult.Error -> {
            LaunchedEffect(
                key1 = Unit,
                block = {
                    val msg = (informationViewModel.updateUserState.value as NetworkResult.Error).msg
                    onShowSnackbar(msg ?: unknownErrorMsg, null)
                }
            )
        }

        is NetworkResult.Success -> {
            LaunchedEffect(
                key1 = Unit,
                block = { onBack() }
            )
        }

        is NetworkResult.None -> {}
    }
}


private fun User.equal(other: User): Boolean {
    if (id != other.id) return false
    if (username != other.username) return false
    if (age != other.age) return false
    if (sex != other.sex) return false
    if (password != other.password) return false
    if (role != other.role) return false
    if (status != other.status) return false
    if (birth.niceDateToDay != other.birth.niceDateToDay) return false
    if (signature != other.signature) return false
    if (location != other.location) return false
    if (blogAddress != other.blogAddress) return false
    return true
}