package su.junglebird.fiteat.data.repository

import kotlinx.coroutines.flow.Flow
import su.junglebird.fiteat.data.database.dao.DailyMenuItemDAO
import su.junglebird.fiteat.data.database.entities.CustomDish
import su.junglebird.fiteat.data.database.entities.DailyMenuItem
import javax.inject.Inject

class DailyMenuItemRepository @Inject constructor(
    private val dao: DailyMenuItemDAO
) {
    fun getItemsForDate(date: String): Flow<List<DailyMenuItem>> = dao.getItemsForDate(date)
    fun getDishesForDate(date: String): Flow<List<CustomDish>> = dao.getDishesForDate(date)
    fun getDailyCalories(date: String): Flow<Int?> = dao.getTotalCaloriesForDate(date)

    suspend fun addToMenu(date: String, dishId: Long) {
        dao.insert(DailyMenuItem(date = date, dishId = dishId))
    }

    suspend fun removeFromMenu(item: DailyMenuItem) {
        dao.delete(item)
    }
}