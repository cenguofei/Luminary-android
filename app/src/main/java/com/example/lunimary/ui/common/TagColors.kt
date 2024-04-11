package com.example.lunimary.ui.common

import android.util.LruCache
import androidx.compose.ui.graphics.Color
import com.example.lunimary.design.components.tagColors

object TagColors : LruCache<String, Int>(1024) {
    private val colors = tagColors

    fun getColor(tag: String): Color = colors[getOrPut(tag)]

    private fun LruCache<String, Int>.getOrPut(
        tag: String
    ): Int {
        val color = this[tag]
        if (color == null) {
            val range = colors.indices
            val index = range.random()
            this.put(tag, index)
            return index
        }
        return color
    }
}