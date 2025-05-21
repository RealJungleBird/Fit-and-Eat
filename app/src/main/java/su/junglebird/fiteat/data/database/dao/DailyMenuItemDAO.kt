package su.junglebird.fiteat.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import su.junglebird.fiteat.data.database.entities.CustomDish
import su.junglebird.fiteat.data.database.entities.DailyMenuItem
import su.junglebird.fiteat.data.database.entities.DayCalories

@Dao
interface DailyMenuItemDAO {
    // Получение меню на конкретную дату
    @Query("SELECT * FROM daily_menu_items WHERE date = :date")
    fun getItemsForDate(date: String): Flow<List<DailyMenuItem>>

    // Получение полной информации о блюдах для даты
    @Query("""
        SELECT c.* FROM customDishes c
        INNER JOIN daily_menu_items d ON c.id = d.dishId
        WHERE d.date = :date
    """)
    fun getDishesForDate(date: String): Flow<List<CustomDish>>

    // Получение суммы калорий за указанный день
    @Query("""
        SELECT COALESCE(SUM(c.calories), 0) FROM customDishes c
        INNER JOIN daily_menu_items d ON c.id = d.dishId
        WHERE d.date = :date
    """)
    fun getTotalCaloriesForDate(date: String): Flow<Int?>


    // Получение суммы калорий за каждый день указанного месяца
    @Query("""
        SELECT d.date, SUM(c.calories) AS totalCalories
        FROM daily_menu_items
        INNER JOIN customDishes c ON d.dishId = c.id
        WHERE strftime('%Y-%m', d.date) = :monthYear
        GROUP BY d.date
    """)
    fun getCaloriesForMonth(monthYear: String): Flow<List<DayCalories>>



    @Insert
    suspend fun insert(menuItem: DailyMenuItem)

    @Delete
    suspend fun delete(menuItem: DailyMenuItem)
}