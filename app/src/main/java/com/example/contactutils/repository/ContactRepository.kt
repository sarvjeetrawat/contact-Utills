package com.example.contactutils.repository

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import com.example.contactutils.db.ContactListDao
import com.example.contactutils.db.ContactListModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactRepository(val mContext: Context, val dao: ContactListDao) {

    @SuppressLint("Range")
    suspend fun fetchContacts() {


        val data =  ArrayList<ContactListModel>()

        val cursor: Cursor? = mContext.getContentResolver().query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        cursor?.let {

            when{
                (it.count > 0)->{

                    while (cursor.moveToNext()) {
                        // val id: String = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.NAME_RAW_CONTACT_ID))
                        val name: String = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                        var phoneNo: String = cursor.getString(cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER))
                        phoneNo = phoneNo.replace("\\s".toRegex(), "")
                        phoneNo = phoneNo.replace("-".toRegex(), "")

                        val contact = ContactListModel(phoneNo,name,false)

                        if(!data.contains(contact)){
                            data.add(contact)
                        }
                    }
                }
            }

            cursor.close()

        }

        withContext(Dispatchers.IO){
            insertAllData(data)

        }

    }

    suspend fun insertAllData(data: List<ContactListModel>) {
        withContext(Dispatchers.IO){
            dao.insertAll(data)
        }
    }



    suspend fun insertData(model : ContactListModel)
    {
        withContext(Dispatchers.IO){
            dao.update(model)
        }

    }

    val getallContact = dao.getAllContact()


}