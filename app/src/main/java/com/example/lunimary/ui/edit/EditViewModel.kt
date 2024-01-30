package com.example.lunimary.ui.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class EditViewModel : ViewModel() {
    var title: String = ""
        set(value) {
            if (field != value) {
                field = value
            }
        }

    var body: String = ""
        set(value) {
            if (field != value) {
                field = value
            }
        }
}