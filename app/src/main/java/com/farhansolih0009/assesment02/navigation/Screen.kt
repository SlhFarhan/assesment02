package com.farhansolih0009.assesment02.navigation

import com.farhansolih0009.assesment02.ui.screen.KEY_ID_TRANSAKSI

sealed class Screen(val route: String) {
    data object Home: Screen("mainScreen")
    data object FormBaru: Screen("detailScreen")
    data object RecycleBin: Screen("recycleBin")
    data object FormUbah: Screen("detailScreen/{$KEY_ID_TRANSAKSI}") {
        fun withId(id: Long) = "detailScreen/$id"
    }
}