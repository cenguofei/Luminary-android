package com.example.lunimary.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.lunimary.R

@Composable
fun PrivacyProtocol(
    modifier: Modifier,
    agreement: MutableState<Boolean>,
    onNavToProtocol: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val agreeColor = if (agreement.value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
            alpha = 0.6f
        )
        IconButton(onClick = { agreement.value = !agreement.value }) {
            val icon = if (agreement.value) Icons.Outlined.CheckCircle else Icons.Outlined.Circle
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = agreeColor
            )
        }
        Text(
            text = stringResource(id = R.string.read_and_agree),
            style = MaterialTheme.typography.labelMedium,
            color = agreeColor
        )
        Text(
            text = stringResource(id = R.string.protocols),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.clickable(onClick = onNavToProtocol)
        )
    }
}