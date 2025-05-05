package su.junglebird.fiteat.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customDishes")
data class CustomDish (
    @PrimaryKey
    val id: Long = 0,
    val name: String,
    val calories: Int
)
