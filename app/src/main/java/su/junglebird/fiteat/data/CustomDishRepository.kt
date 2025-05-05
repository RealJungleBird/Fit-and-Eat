package su.junglebird.fiteat.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CustomDishRepository @Inject constructor(
    private val itemDAO: CustomDishDAO
) {
    val allItems: Flow<List<CustomDish>> = itemDAO.getAllItems()

    suspend fun updateItem(item: CustomDish) {
        itemDAO.update(item)
    }

    suspend fun insertItem(item: CustomDish) {
        itemDAO.insert(item)
    }
}