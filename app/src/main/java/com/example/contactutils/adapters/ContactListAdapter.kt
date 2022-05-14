package com.example.contactutils.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.contactutils.R
import com.example.contactutils.databinding.ItemListContactBinding
import com.example.contactutils.db.ContactListModel

class ContactListAdapter(private val clickListener: (ContactListModel) -> Unit,val checkChangeListener : (data : ContactListModel,isChecked: Boolean)-> Unit) : ListAdapter<ContactListModel, ContactListAdapter.ViewHolder>(Callbacks()) {


    private val mDiffer: AsyncListDiffer<ContactListModel> = AsyncListDiffer(this, Callbacks())

    fun setData(list: List<ContactListModel>) {
        mDiffer.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding: ItemListContactBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.item_list_contact,
            parent,
            false
        )


        return ViewHolder( binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        mDiffer.currentList.get(position).let {
            holder.bind(it, clickListener)
        }
    }

    inner class ViewHolder(itemView: ItemListContactBinding) : RecyclerView.ViewHolder(itemView.root) {

        val bindingItem = itemView

        fun bind(task : ContactListModel, clickListener : (ContactListModel) -> Unit) {
            bindingItem.model = task
            bindingItem.isVisibleCheckBox = true


            bindingItem.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                // Code here

                checkChangeListener.invoke(task,isChecked)

            }
            itemView.setOnClickListener { clickListener(task) }
        }
    }

    override fun getItemCount(): Int {
        return  mDiffer.currentList.size
    }

}

class Callbacks : DiffUtil.ItemCallback<ContactListModel>() {
    override fun areItemsTheSame(oldItem: ContactListModel, newItem: ContactListModel): Boolean {
        return oldItem.phoneNumber == newItem.phoneNumber
    }

    override fun areContentsTheSame(oldItem: ContactListModel, newItem: ContactListModel): Boolean {
        return oldItem == newItem
    }
}