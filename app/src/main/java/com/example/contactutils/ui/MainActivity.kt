package com.example.contactutils.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.contactutils.BuildConfig
import com.example.contactutils.R
import com.example.contactutils.db.ContactDatabase
import com.example.contactutils.db.ContactListDao
import com.example.contactutils.factory.ContactListViewModelFactory
import com.example.contactutils.repository.ContactRepository
import com.example.contactutils.viewmodel.ContactListViewModel
import com.google.android.material.snackbar.Snackbar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.example.contactutils.adapters.ContactListAdapter
import com.example.contactutils.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding : ActivityMainBinding
    lateinit var contactListViewModel: ContactListViewModel
    val PERMISSIONS_REQUEST_CODE = 101
    lateinit var adpater : ContactListAdapter

    var contactPermission  =  MutableLiveData<Boolean>(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mainBinding.lifecycleOwner = this


        if (checkContactPermission()) {
            contactPermission.value = true

        } else {
            requestPermissions()
        }


        val dao: ContactListDao = ContactDatabase.getInstance(applicationContext).contactListDao
        val repository = ContactRepository(this, dao)

        val factory = ContactListViewModelFactory(this,repository)

        contactListViewModel = ViewModelProvider(this, factory).get(ContactListViewModel::class.java)


        Log.e("TAG", contactListViewModel.toString());


        adpater= ContactListAdapter ({ data ->


        },{
                data, isChecked ->

            lifecycleScope.launch(Dispatchers.IO){
                data.isChecked = isChecked

                contactListViewModel.insertEntry(model = data)

            }

        })



        lifecycleScope.launch(Dispatchers.IO){
            contactListViewModel.insertContacts()
        }


        contactListViewModel.getContact().observe(this, {
            Log.e("Observed", "-->")
            adpater.setData(it)
        }
        )

        mainBinding.rePhoneContact.adapter = adpater

    }

    private fun checkContactPermission(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CONTACTS
        ) &&
                PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_PHONE_STATE
        ) &&
                PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CALL_LOG)

    }

    private fun requestPermissions() {
        val isPermission = checkContactPermission()

        if (isPermission) {



        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_CALL_LOG),
                PERMISSIONS_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("TAG", "onRequestPermissionResult")

        when (requestCode) {
            PERMISSIONS_REQUEST_CODE -> when {
                grantResults.isEmpty() ->{

                }

                (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED)
                ->{
                    contactPermission.value = true

                }


                else -> {


                    Snackbar.make(
                        findViewById(android.R.id.content),
                        R.string.permission_allow,
                        Snackbar.LENGTH_INDEFINITE
                    )
                        .setAction(R.string.settings) {
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                BuildConfig.APPLICATION_ID,
                                null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                        .show()

                }
            }
        }
    }
}