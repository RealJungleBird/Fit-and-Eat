package su.junglebird.fiteat.ui.navigation

sealed class Routes(val routes: String) {
    object Profile: Routes("profile")
    object DaySummary: Routes("day-summary")
}