package su.junglebird.fiteat.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import su.junglebird.fiteat.data.database.dao.DailyMenuItemDAO
import su.junglebird.fiteat.data.database.entities.CustomDish
import su.junglebird.fiteat.data.database.entities.DailyMenuItem
import javax.inject.Inject
import su.junglebird.fiteat.data.database.entities.DayCalories
import java.time.YearMonth

class DailyMenuItemRepository @Inject constructor(
    private val dao: DailyMenuItemDAO
) {
    fun getItemsForDate(date: String): Flow<List<DailyMenuItem>> = dao.getItemsForDate(date)
    fun getDishesForDate(date: String): Flow<List<CustomDish>> = dao.getDishesForDate(date)
    fun getDailyCalories(date: String): Flow<Int?> = dao.getTotalCaloriesForDate(date)
    fun getMonthlyCalories(date: String): Flow<List<DayCalories>> = dao.getCaloriesForMonth(date)

//    // Вероятно, временное решение
//    fun getDailyCaloriesForMonth(date: String): Flow<List<DayCalories>> {
//        val localDate = LocalDate.parse(date)
//        val daysInMonth = YearMonth.of(localDate.year, localDate.month.number).lengthOfMonth()
//        val result = listOf<DayCalories>()
//        for(i in 1..daysInMonth) {
//            val dayData = getDailyCalories(date)
//        }
////
////        return (1..daysInMonth).map { day ->
////            DayCalories(7, 8)
////        }
//    }





    suspend fun addToMenu(date: String, dishId: Long) {
        dao.insert(DailyMenuItem(date = date, dishId = dishId))
    }

    suspend fun removeFromMenu(item: DailyMenuItem) {
        dao.delete(item)
    }
}