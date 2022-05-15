package com.example.contactutils.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.contactutils.db.ContactListModel
import com.example.contactutils.repository.ContactRepository

class ContactListViewModel(val mContext:Context, val repository: ContactRepository) : ViewModel() {



    suspend fun insertContacts() = repository.fetchContacts()

    fun getContact() =repository.getallContact

    suspend fun insertEntry(model: ContactListModel){
        repository.insertData(model)
    }



}