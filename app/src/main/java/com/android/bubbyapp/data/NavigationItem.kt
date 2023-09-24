package com.android.bubbyapp.data

import androidx.compose.ui.graphics.vector.ImageVector
import com.android.bubbyapp.navigation.Screen

data class NavigationItem(
    val title: String,
    val description: String,
    val toNavigate: Screen,
    val icon: ImageVector
)