package su.junglebird.fiteat.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// сущность для хранения дневного меню
@Entity(
    tableName = "daily_menu_items",     // название таблицы
    foreignKeys = [ForeignKey(          // связь с таблицей customDishes через внешний ключ
        entity = CustomDish::class,
        parentColumns = ["id"],         // столбец в родительской таблице
        childColumns = ["dishId"],      // столбец в этой таблице
        onDelete = ForeignKey.CASCADE   // удаление каскадом
    )]
)
data class DailyMenuItem (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val date: String,   // дата в формате "yyyy-MM-dd"

    @ColumnInfo(index = true)   // индекс для ускорения поиска
    val dishId: Long    // ссылка на ID блюда из customDishes
)