package su.junglebird.fiteat.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// сущность для хранения информации о пользовательских блюдах
@Entity(tableName = "customDishes")
data class CustomDish(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,   // автоинкрементный ID

    @ColumnInfo(name = "name")
    val name: String,   // название блюда

    @ColumnInfo(name = "calories")
    val calories: Int   // калорийность блюда
)