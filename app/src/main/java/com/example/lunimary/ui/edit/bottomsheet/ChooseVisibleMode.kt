package com.example.lunimary.ui.edit.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.lunimary.R
import com.example.lunimary.models.VisibleMode
import com.example.lunimary.ui.edit.EditViewModel

@Composable
fun ChooseVisibleMode(editViewModel: EditViewModel) {
    PublishSettingsItem(title = stringResource(id = R.string.choose_visible_mode)) {
        Row(modifier = Modifier) {
            val radioOptions = listOf(
                VisibleMode.PUBLIC to stringResource(id = R.string.public_visible_mode),
                VisibleMode.OWN to stringResource(id = R.string.only_own_mode)
            )
            Column(modifier = Modifier.selectableGroup()) {
                radioOptions.forEach { pair ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .selectable(
                                selected = pair.first == editViewModel.visibleMode.value,
                                onClick = { editViewModel.visibleModeChange(pair.first) },
                                role = Role.RadioButton
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = pair.first == editViewModel.visibleMode.value,
                            onClick = null,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = pair.second,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
