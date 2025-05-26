package su.junglebird.fiteat.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_info")
data class UserInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "gender")
    val gender: Boolean,

    @ColumnInfo(name = "date-of-birth")
    val dateOfBirth: String,

    @ColumnInfo(name = "height")
    val height: Float,

    @ColumnInfo(name = "mass")
    val mass: Float
)