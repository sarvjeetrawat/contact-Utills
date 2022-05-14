package com.example.contactutils.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.contactutils.db.ContactListDao
import com.example.contactutils.repository.ContactRepository
import com.example.contactutils.viewmodel.ContactListViewModel
import java.lang.IllegalArgumentException

class ContactListViewModelFactory(val context: Context, val repository: ContactRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactListViewModel::class.java)){
            return ContactListViewModel(context, repository) as T
        }
        throw IllegalArgumentException("Unknown view Model Class")
    }
}