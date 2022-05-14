package com.example.contactutils.utils

import android.app.Application
import com.example.contactutils.db.ContactDatabase

class ApplicationClass : Application()
{
    companion object{
        lateinit var appdataBase : ContactDatabase
    }
    override fun onCreate() {
        super.onCreate()

        appdataBase = ContactDatabase.getInstance(this)
    }
}