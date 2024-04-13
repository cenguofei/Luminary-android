package com.example.lunimary.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.lunimary.R

@Composable
fun RemindToLogin(
    onLoginClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        onClick = onLoginClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(id = R.string.login_account),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(id = R.string.login_benefits),
                    style = MaterialTheme.typography.labelMedium
                )
            }
            TextButton(onClick = onLoginClick) {
                Text(
                    text = stringResource(id = R.string.login),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}