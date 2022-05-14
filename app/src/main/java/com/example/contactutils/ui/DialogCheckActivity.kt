package com.example.contactutils.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.contactutils.R
import com.example.contactutils.databinding.ActivityForDialogBinding
import com.example.contactutils.db.ContactListModel
import com.example.contactutils.fragment.OutSideDialogFragment


class DialogCheckActivity: AppCompatActivity() {


   lateinit var binding : ActivityForDialogBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_for_dialog)




        intent.getParcelableExtra<ContactListModel>("model")?.let {

            val newFragment = OutSideDialogFragment.newInstance(it) { data ->

                finish()
            }

            newFragment.show(supportFragmentManager,OutSideDialogFragment.TAG)


        }

    }
}