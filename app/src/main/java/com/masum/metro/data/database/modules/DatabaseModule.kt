package com.masum.metro.data.database.modules

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.masum.metro.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext  context: Context
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "metro.db"
    ).fallbackToDestructiveMigration()
        .createFromAsset("metro.sqlite",object :RoomDatabase.PrepackagedDatabaseCallback(){

        })
        .build()

}