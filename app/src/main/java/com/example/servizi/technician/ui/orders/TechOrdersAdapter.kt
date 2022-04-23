package com.example.servizi.technician.ui.orders

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.servizi.databinding.TechOrderItemViewBinding
import com.example.servizi.technician.model.Appointment

class TechOrdersAdapter : ListAdapter<Appointment,
        TechOrdersAdapter.OrdersViewHolder>(DiffCallback) {

    var onItemClick: ((Appointment?) -> Unit)? = null

    inner class OrdersViewHolder(
        private var binding: TechOrderItemViewBinding
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
            TechOrderItemViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }
}
