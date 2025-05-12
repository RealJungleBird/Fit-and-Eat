package su.junglebird.fiteat.data.repository

import su.junglebird.fiteat.data.database.dao.DailyMenuItemDAO
import su.junglebird.fiteat.data.database.entities.DailyMenuItem
import javax.inject.Inject

class DailyMenuItemRepository @Inject constructor(
    private val dao: DailyMenuItemDAO
) {
    fun getItemsForDate(date: String) = dao.getItemsForDate(date)
    fun getDishesForDate(date: String) = dao.getDishesForDate(date)

    suspend fun addToMenu(date: String, dishId: Long) {
        dao.insert(DailyMenuItem(date = date, dishId = dishId))
    }

    suspend fun removeFromMenu(item: DailyMenuItem) {
        dao.delete(item)
    }
}