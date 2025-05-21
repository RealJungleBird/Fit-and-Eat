package su.junglebird.fiteat.data.database.entities

data class DayCalories(
    val date: String,    // Day number (1-31)
    val totalCalories: Int   // Number of calories
)