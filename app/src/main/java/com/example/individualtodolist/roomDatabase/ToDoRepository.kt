package com.example.individualtodolist.roomDatabase

import android.content.Context
import com.example.individualtodolist.models.ToDo

class ToDoRepository(context: Context) {

    private var todoDao: ToDoDao

    init {
        val reminderRoomDatabase = ToDoRoomDatabase.getDatabase(context)
        todoDao = reminderRoomDatabase!!.todoDao()
    }

    fun getAllReminders(): List<ToDo> {
        return todoDao.getAllReminders()
    }

    fun insertReminder(reminder: ToDo) {
        todoDao.insertReminder(reminder)
    }

    fun deleteReminder(reminder: ToDo) {
        todoDao.deleteReminder(reminder)
    }


    fun updateReminder(reminder: ToDo) {
        todoDao.updateReminder(reminder)
    }
}

