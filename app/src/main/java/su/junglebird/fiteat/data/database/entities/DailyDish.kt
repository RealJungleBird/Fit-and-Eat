package su.junglebird.fiteat.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "dailyDishes")
data class DailyDish (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "dish_id")
    val dishId: Long,

    @ColumnInfo(name = "date")
    val date: LocalDate
)