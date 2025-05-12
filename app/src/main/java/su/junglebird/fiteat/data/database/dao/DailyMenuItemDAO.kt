package su.junglebird.fiteat.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import su.junglebird.fiteat.data.database.entities.CustomDish
import su.junglebird.fiteat.data.database.entities.DailyMenuItem

@Dao
interface DailyMenuItemDAO {
    // Получение меню на конкретную дату
    @Query("SELECT * FROM daily_menu_items WHERE date = :date")
    fun getItemsForDate(date: String): Flow<List<DailyMenuItem>>

    // Получение полной информации о блюдах для даты через JOIN
    @Query("""
        SELECT c.* FROM customDishes c
        INNER JOIN daily_menu_items d ON c.id = d.dishId
        WHERE d.date = :date
    """)
    fun getDishesForDate(date: String): Flow<List<CustomDish>>

    @Insert
    suspend fun insert(menuItem: DailyMenuItem)

    @Delete
    suspend fun delete(menuItem: DailyMenuItem)
}