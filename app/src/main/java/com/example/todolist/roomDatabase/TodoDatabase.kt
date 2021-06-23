package com.example.todolist.roomDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todolist.data.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class],version = 2)
abstract class TodoDatabase:RoomDatabase() {

    abstract fun taskDao():Dao

    class Callback @Inject constructor(
        private val database: Provider<TodoDatabase>,
        @com.example.todolist.dependencyInjection.Scope private val customScope:CoroutineScope
        ) :RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao=database.get().taskDao()

            customScope.launch {
                dao.insert(Task("You are welcome...."))
            }

        }
    }
}