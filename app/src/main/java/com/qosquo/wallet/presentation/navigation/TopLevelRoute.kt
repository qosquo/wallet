package com.qosquo.wallet.presentation.navigation

import androidx.compose.ui.graphics.vector.ImageVector

data class TopLevelRoute<T : Any>(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: T
)