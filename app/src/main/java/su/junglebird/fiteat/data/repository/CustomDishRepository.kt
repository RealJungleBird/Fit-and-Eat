package su.junglebird.fiteat.data.repository

import kotlinx.coroutines.flow.Flow
import su.junglebird.fiteat.data.database.dao.CustomDishDAO
import su.junglebird.fiteat.data.database.entities.CustomDish
import javax.inject.Inject

class CustomDishRepository @Inject constructor(
    private val dishDAO: CustomDishDAO
) {
    val allDishes: Flow<List<CustomDish>> = dishDAO.getAllDishes()

    suspend fun insert(dish: CustomDish) = dishDAO.insert(dish)

    suspend fun update(dish: CustomDish) = dishDAO.update(dish)

    suspend fun delete(dish: CustomDish) = dishDAO.delete(dish)
}