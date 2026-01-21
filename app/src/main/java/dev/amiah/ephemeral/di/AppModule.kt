package dev.amiah.ephemeral.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.amiah.ephemeral.data.AppDatabase
import dev.amiah.ephemeral.data.datastore.UserPreferences
import dev.amiah.ephemeral.data.datastore.UserPreferencesSerializer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java, "ephemeral.db"
        ).fallbackToDestructiveMigration().build(); // TODO: REMOVE DESTRUCTIVE MIGRATION
    }

    @Provides
    @Singleton
    fun provideNoteDao(db: AppDatabase) = db.noteDao()

    @Provides
    @Singleton
    fun provideTaskDao(db: AppDatabase) = db.taskDao()

    @Provides
    @Singleton
    fun provideReminderDao(db: AppDatabase) = db.reminderDao()

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext appContext: Context): DataStore<UserPreferences> {
        return DataStoreFactory.create(
            serializer = UserPreferencesSerializer,
            produceFile = { appContext.dataStoreFile("user-preferences.json") }
        )
    }

}