package com.example.lunimary.design.nicepage

import androidx.paging.compose.LazyPagingItems

fun <T : Any> LazyPagingItems<T>.isEmpty() = itemCount == 0

fun <T : Any> LazyPagingItems<T>.isNotEmpty() = !isEmpty()