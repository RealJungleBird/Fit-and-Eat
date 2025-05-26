package su.junglebird.fiteat.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import su.junglebird.fiteat.data.database.dao.CustomDishDAO
import su.junglebird.fiteat.data.database.AppDatabase
import su.junglebird.fiteat.data.database.dao.DailyMenuItemDAO
import su.junglebird.fiteat.data.database.dao.UserInfoDAO
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideDailyMenuItemDao(database: AppDatabase): DailyMenuItemDAO {
        return database.dailyMenuItemDAO()
    }

    @Provides
    fun provideCurrentDishDAO(database: AppDatabase): CustomDishDAO {
        return database.dishDAO()
    }

    @Provides
    fun provideUserInfoDAO(database: AppDatabase): UserInfoDAO {
        return database.userInfoDAO()
    }
}