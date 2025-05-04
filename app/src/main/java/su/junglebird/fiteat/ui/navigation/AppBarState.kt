package su.junglebird.fiteat.ui.navigation

import androidx.compose.runtime.Composable

data class AppBarState (
    val title: String = "",
    val actions: @Composable () -> Unit = {},
    val navigationIcon: @Composable () -> Unit = {}
)

fun getAppBarState(route: String?): AppBarState {
    return when(route) {
        Routes.Profile.route -> AppBarState(
            title = "Профиль",
            actions = {}
        )
        Routes.DaySummary.route -> AppBarState(
            title = "Сводка за день",
            actions = {}
        )
        Routes.MyDishes.route -> AppBarState(
            title = "Мои блюда",
            actions = {}
        )
        else -> AppBarState()
    }
}