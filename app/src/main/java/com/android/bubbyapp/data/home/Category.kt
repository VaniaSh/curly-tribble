package com.android.bubbyapp.data.home

import androidx.compose.ui.graphics.Color

data class Category(
	val userId: String = "",
	val title: String = "",
	val description: String = "",
	val totalSum: Float = 0f,
	val documentId: String = ""
)