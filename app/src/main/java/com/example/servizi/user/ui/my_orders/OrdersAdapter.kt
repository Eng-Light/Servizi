package com.example.servizi.user.ui.my_orders

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.servizi.databinding.OrderItemViewBinding
import com.example.servizi.user.model.Appointment

open class OrdersAdapter : ListAdapter<Appointment,
        OrdersAdapter.OrdersViewHolder>(DiffCallback) {

    var onItemClick: ((Appointment?) -> Unit)? = null

    inner class OrdersViewHolder(
        var binding: OrderItemViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(appointment: Appointment?) {
            binding.appointment = appointment
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                onItemClick?.invoke(appointment)
                //notifyDataSetChanged()
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Appointment>() {
        override fun areItemsTheSame(oldItem: Appointment, newItem: Appointment): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Appointment, newItem: Appointment): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        return OrdersViewHolder(
            OrderItemViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }
}
