package com.kseniabl.petsapp.ui

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.kseniabl.petsapp.R

enum class BottomBarItem (
    val itemName: String,
    val route: String,
    val icon: Int
) {
    WALLET("Wallet", "wallet", R.drawable.wallet),
    TRIP("Trip", "trip", R.drawable.distance),
    SHOP("Shop", "shop", R.drawable.shop)
}


data class TransactionItem(
    val name: String,
    val date: Long,
    val type: TransactionsTypes,
    val sum: Float
)

enum class TransactionsTypes(
    val image: Int,
    val color: Color
) { REFILL(R.drawable.refill, Color(0xFF65AAFF)),
    WITHDRAWAL(R.drawable.withdrawal, Color(0xFF9365FF))
}

val transactionsList = listOf(
    TransactionItem("Wallet refill", 1665435600000, TransactionsTypes.REFILL, 53F),
    TransactionItem("Wallet refill", 1665349200000, TransactionsTypes.REFILL, 22.65F),
    TransactionItem("Money withdrawal", 1662843600000, TransactionsTypes.WITHDRAWAL, 11.23F),
    TransactionItem("Wallet refill", 1661979600000, TransactionsTypes.REFILL, 66.66F),
)