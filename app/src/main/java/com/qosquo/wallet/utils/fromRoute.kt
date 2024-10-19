package com.qosquo.wallet.utils

import androidx.navigation.NavBackStackEntry

fun NavBackStackEntry?.fromRoute(): String? {
    return this?.destination?.route?.substringBefore("?")?.substringBefore("/")
}