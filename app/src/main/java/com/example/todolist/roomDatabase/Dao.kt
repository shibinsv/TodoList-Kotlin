package com.example.todolist.roomDatabase

import androidx.room.*
import androidx.room.Dao
import com.example.todolist.data.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Insert(onConflict =OnConflictStrategy.REPLACE )
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT*FROM todo_table")
    fun getTasks():Flow<List<Task>>

}