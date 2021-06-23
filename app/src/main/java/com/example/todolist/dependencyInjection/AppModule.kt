package com.example.todolist.dependencyInjection

import android.app.Application
import androidx.room.Room
import com.example.todolist.roomDatabase.TodoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app:Application,callback:TodoDatabase.Callback)=
        Room.databaseBuilder(app,TodoDatabase::class.java,"todo_database")
        .fallbackToDestructiveMigration()
            .addCallback(callback)
        .build()

    @Provides
    fun provideTodoDao(db:TodoDatabase)=db.taskDao()

    @Scope
    @Provides
    @Singleton
    fun customScope()=CoroutineScope(SupervisorJob())

}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class Scope