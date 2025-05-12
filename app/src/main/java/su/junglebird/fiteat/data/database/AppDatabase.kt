package su.junglebird.fiteat.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import su.junglebird.fiteat.data.database.dao.CustomDishDAO
import su.junglebird.fiteat.data.database.dao.DailyMenuItemDAO
import su.junglebird.fiteat.data.database.entities.CustomDish
import su.junglebird.fiteat.data.database.entities.DailyMenuItem

@Database(
    entities = [CustomDish::class, DailyMenuItem::class], // Добавлять новые сущности здесь
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dishDAO(): CustomDishDAO
    abstract fun dailyMenuItemDAO(): DailyMenuItemDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app-database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }

        }
    }
}
