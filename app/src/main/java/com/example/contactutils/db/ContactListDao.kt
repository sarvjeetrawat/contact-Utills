package com.example.contactutils.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ContactListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<ContactListModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(repos: ContactListModel)

    @Update(entity = ContactListModel::class)
    suspend fun update(repos: ContactListModel)


    @Query("SELECT * FROM contact ORDER BY ContactName ASC")
    fun getAllContact(): LiveData<List<ContactListModel>>


    @Query("SELECT * FROM contact WHERE isChecked LIKE :checked AND phoneNumber LIKE '%'")
    suspend fun getCheckedNumber(checked : Boolean): List<ContactListModel>


    @Query("DELETE FROM Contact")
    suspend fun clearRepos()
}