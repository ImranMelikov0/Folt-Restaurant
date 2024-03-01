package com.imranmelikov.folt.presentation.orderhistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.imranmelikov.folt.databinding.OrderHistoryRvBinding
import com.imranmelikov.folt.domain.model.Order

class OrderHistoryAdapter:RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>() {
    class OrderHistoryViewHolder(val binding:OrderHistoryRvBinding):RecyclerView.ViewHolder(binding.root)


    // DiffUtil for efficient RecyclerView updates
    private val diffUtil=object : DiffUtil.ItemCallback<Order>(){
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem==newItem
        }
    }
    private val recyclerDiffer= AsyncListDiffer(this,diffUtil)

    // Getter and setter for the list of OrderList
    var orderList:List<Order>
        get() = recyclerDiffer.currentList
        set(value) = recyclerDiffer.submitList(value)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val binding=OrderHistoryRvBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OrderHistoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        val order=orderList[position]
        "History: ${order.history}".also { holder.binding.history.text = it }
        holder.binding.venueName.text=order.venueName
        "Address: ${order.address.addressName}".also { holder.binding.address.text = it }
        "Total price: ${order.totalPrice}".also { holder.binding.totalPrice.text = it }
    }
}