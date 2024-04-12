package com.example.lunimary.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.lunimary.R

@Composable
fun AccountInput(
    password: MutableState<String>,
    username: MutableState<String>
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = username.value,
        onValueChange = {
            username.value = it
        },
        singleLine = true,
        maxLines = 1,
        placeholder = {
            Text(
                text = stringResource(id = R.string.account),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            errorBorderColor = Color.Transparent
        ),
        isError = !isUsernameValid(username.value)
    )
    Spacer(
        modifier = Modifier
            .height(1.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
    )

    Spacer(modifier = Modifier.height(20.dp))

    val visibilityOff = remember { mutableStateOf(true) }
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = password.value,
        onValueChange = {
            password.value = it
        },
        singleLine = true,
        maxLines = 1,
        placeholder = {
            Text(
                text = stringResource(id = R.string.password),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        },
        trailingIcon = {
            IconButton(
                onClick = { visibilityOff.value = !visibilityOff.value }
            ) {
                Icon(
                    imageVector = if (visibilityOff.value) {
                        Icons.Default.VisibilityOff
                    } else { Icons.Default.Visibility },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        },
        visualTransformation = if (visibilityOff.value) PasswordVisualTransformation() else VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            errorBorderColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        isError = !isPasswordValid(password.value)
    )
    Spacer(
        modifier = Modifier
            .height(1.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
    )
}