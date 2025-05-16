package su.junglebird.fiteat.ui.navigation

sealed class Routes(val route: String) {
    object Profile: Routes("profile")
    object DaySummary: Routes("day-summary")
    object MyDishes: Routes("my-dishes")
    object Analytics: Routes("analytics")
}