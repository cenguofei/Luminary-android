package com.example.lunimary.ui.user.information

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lunimary.R
import com.example.lunimary.models.Sex
import com.example.lunimary.util.logd
import java.util.Calendar

@Composable
fun EditInformationItem(
    initialText: String,
    editItemType: EditItemType,
    onConfirm: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(12.dp))
        val (text, setText) = remember { mutableStateOf(initialText) }
        TextField(
            value = text,
            onValueChange = setText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RectangleShape,
            isError = isError(text, editItemType),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height((16.dp)))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "${text.length} / ${editItemType.length}",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            TextButton(
                onClick = { onConfirm(text) },
                shape = RoundedCornerShape(50),
                enabled = !isError(text, editItemType),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.height(32.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.ok),
                        color = MaterialTheme.colorScheme.surface,
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

enum class EditItemType(val length: Int) {
    Username(50),
    Sex(2),
    Birth(10),
    Signature(100),
    BlogAddress(10000)
}

private fun isError(text: String, editItemType: EditItemType): Boolean {
     return when(editItemType) {
        EditItemType.Birth -> {
            !text.isBirth()
        }
        EditItemType.Username -> {
            false
        }
        EditItemType.Sex -> {
            !text.isSex()
        }
        EditItemType.Signature -> {
            false
        }
        EditItemType.BlogAddress -> {
            !text.isLink()
        }
    }
}

private fun String.isBirth(): Boolean {
    val arr = this.split("-")
    "arr=$arr".logd("is_birth")
    if (arr.size != 3) {
        return false
    }
    val currentDate = Calendar.getInstance()

    val year = arr[0]
    if (year.length != 4) {
        return false
    }
    if (year.toIntOrNull() == null) {
        return false
    }
    if (year.toInt() > currentDate.get(Calendar.YEAR)) {
        return false
    }

    val month = arr[1]
    if (month.length > 2) {
        return false
    }
    if (month.toIntOrNull() == null) {
        return false
    }
    val m = month.toInt()
    if (m == 0 || m > 12) {
        return false
    }

    val day = arr[2]
    if (day.toIntOrNull() == null) {
        return false
    }
    val days = getDaysInMonth(year.toInt(), month.toInt())
    "day=$day, days=$days".logd("is_birth")
    if (day.toInt() <= 0 || day.toInt() > days) {
        return false
    }

    return true
}

fun getDaysInMonth(year: Int, month: Int): Int {
    val isRun = year % 4 == 0
    //val daysOf31 = listOf(1, 3, 5, 7, 8,  10, 11)
    val days30 = listOf(4, 6, 9, 12)
    val days2 = if (isRun) 29 else 28
    if (month == 2) return days2
    if (month in days30) return 30
    return 31
}

private fun String.isSex(): Boolean {
    return this == Sex.Male.text || this == Sex.Female.text || this == Sex.Sealed.text
}

private fun String.isLink(): Boolean {
    return this.startsWith("http://") || this.startsWith("https://")
}