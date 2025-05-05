package su.junglebird.fiteat.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import su.junglebird.fiteat.data.CustomDish

@Dao
interface CustomDishDAO {
    @Query("SELECT * FROM customDishes")
    fun getAllItems(): Flow<List<CustomDish>>

    @Update
    suspend fun update(item: CustomDish)

    @Insert
    suspend fun insert(item: CustomDish)
}