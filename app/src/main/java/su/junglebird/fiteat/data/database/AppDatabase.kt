package su.junglebird.fiteat.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import su.junglebird.fiteat.data.database.dao.CustomDishDAO
import su.junglebird.fiteat.data.database.entities.CustomDish

@Database(
    entities = [CustomDish::class], // Добавлять новые сущности здесь
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dishDAO(): CustomDishDAO

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
