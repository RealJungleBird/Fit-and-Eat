package su.junglebird.fiteat.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import su.junglebird.fiteat.data.database.entities.CustomDish

@Dao
interface CustomDishDAO {
    // получение всех блюд, отсортированных по ID
    @Query("SELECT * FROM customDishes ORDER BY id DESC")
    fun getAllDishes(): Flow<List<CustomDish>>

    @Insert
    suspend fun insert(dish: CustomDish)

    @Update
    suspend fun update(dish: CustomDish)

    @Delete
    suspend fun delete(dish: CustomDish)
}