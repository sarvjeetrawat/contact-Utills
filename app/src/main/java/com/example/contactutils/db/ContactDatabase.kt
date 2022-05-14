package com.example.contactutils.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ContactListModel::class], version = 1)
abstract class ContactDatabase : RoomDatabase() {

    abstract val contactListDao: ContactListDao

    companion object {
        @Volatile
        private var INSTANCE: ContactDatabase? = null
        fun getInstance(context: Context): ContactDatabase {
            synchronized(this) {
                var instance: ContactDatabase? = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext, ContactDatabase::class.java,
                        "Contact_Database"
                    ).build()
                }
                return instance
            }
        }
    }

}