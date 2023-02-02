package com.kseniabl.petsapp.utils

sealed class Routes(val route: String) {
    object Main : Routes("main")
    object Profile : Routes("profile")
}