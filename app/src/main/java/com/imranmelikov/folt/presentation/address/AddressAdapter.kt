package com.imranmelikov.folt.presentation.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.imranmelikov.folt.R
import com.imranmelikov.folt.constants.AddressConstants
import com.imranmelikov.folt.databinding.AddressRvBinding
import com.imranmelikov.folt.domain.model.Address

class AddressAdapter:RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {
    class AddressViewHolder(val binding:AddressRvBinding):RecyclerView.ViewHolder(binding.root)

    // DiffUtil for efficient RecyclerView updates
    private val diffUtil=object : DiffUtil.ItemCallback<Address>(){
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem==newItem
        }
    }
    private val recyclerDiffer= AsyncListDiffer(this,diffUtil)

    // Getter and setter for the list of addressList
    var addressList:List<Address>
        get() = recyclerDiffer.currentList
        set(value) = recyclerDiffer.submitList(value)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding=AddressRvBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AddressViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address=addressList[position]
        holder.binding.addressText.text=address.addressName
        holder.binding.countryText.text=address.countryName

        holder.binding.menuImg.setOnClickListener {
            val bundle=Bundle()
            bundle.apply {
                putSerializable(AddressConstants.address,address)
                putString(AddressConstants.addressDetailBundleKey,AddressConstants.address)
            }
            Navigation.findNavController(it).navigate(R.id.action_addressFragment_to_addressDetailsFragment,bundle)
        }
    }
}