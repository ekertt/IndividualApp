package com.example.individualtodolist.roomDatabase

import androidx.room.*
import com.example.individualtodolist.models.ToDo

@Dao
interface ToDoDao {

    @Query("SELECT * FROM toDo")
    fun getAllReminders(): List<ToDo>

    @Insert
    fun insertReminder(todo: ToDo)

    @Delete
    fun deleteReminder(todo: ToDo)

    @Update
    fun updateReminder(todo: ToDo)

}
