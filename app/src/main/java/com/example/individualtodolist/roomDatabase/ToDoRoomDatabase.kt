package com.example.individualtodolist.roomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.individualtodolist.models.ToDo

@Database(entities = [ToDo::class], version = 1, exportSchema = false)
abstract class ToDoRoomDatabase : RoomDatabase() {

    abstract fun todoDao(): ToDoDao

    companion object {
        private const val DATABASE_NAME = "TODO_DATABASE"

        @Volatile
        private var reminderRoomDatabaseInstance: ToDoRoomDatabase? = null

        fun getDatabase(context: Context): ToDoRoomDatabase? {
            if (reminderRoomDatabaseInstance == null) {
                synchronized(ToDoRoomDatabase::class.java) {
                    if (reminderRoomDatabaseInstance == null) {
                        reminderRoomDatabaseInstance = Room.databaseBuilder(
                            context.applicationContext,
                            ToDoRoomDatabase::class.java, DATABASE_NAME
                        )
                            .allowMainThreadQueries()
                            .build()
                    }
                }
            }
            return reminderRoomDatabaseInstance
        }
    }

}
