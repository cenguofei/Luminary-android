package com.example.lunimary.ui.user.information

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.lunimary.R
import com.example.lunimary.models.Sex
import com.example.lunimary.models.User

@Composable
fun InformationItems(user: User, headImageSize: Dp) {
    Spacer(modifier = Modifier.height(headImageSize / 2))

    Item(
        itemName = stringResource(id = R.string.username),
        itemContent = user.username,
    )

    Item(
        itemName = stringResource(id = R.string.sex),
        itemContent = when (user.sex) {
            Sex.Sealed -> stringResource(id = R.string.unknown)
            Sex.Male -> stringResource(id = R.string.male)
            Sex.Female -> stringResource(id = R.string.female)
        },
    )
}

@Composable
private fun Item(
    itemName: String,
    itemContent: String,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = itemName,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.width(80.dp)
            )
            Text(
                text = itemContent,
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            )
        }
    }
}
