package com.kseniabl.petsapp

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val app_white = Color.White
private val app_blue_700 = Color(0xFF313131)
private val app_blue_800 = Color(0xFF242424)
private val app_blue_900 = Color(0xFF1B1B1B)
private val app_beige = Color(0xFFFFA174)

private val blue_color = Color(0xFF9365FF)

val appColors = lightColors(
    primary = app_blue_800,
    secondary = app_beige,
    surface = app_blue_900,
    onSurface = app_white,
    primaryVariant = app_blue_700
)

@Composable
fun PetsAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(colors = appColors) { content() }
}