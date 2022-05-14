package com.example.contactutils.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "Contact")
    @Parcelize
    data class ContactListModel(
        @PrimaryKey()
        var phoneNumber : String,
        var ContactName : String? = null,
        var isChecked : Boolean? = false
    ) : Parcelable
