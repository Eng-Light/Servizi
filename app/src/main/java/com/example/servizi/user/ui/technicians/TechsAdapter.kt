package com.example.servizi.user.ui.technicians

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.servizi.R
import com.example.servizi.databinding.ItemViewBinding
import com.example.servizi.user.model.Technician

class TechsAdapter : ListAdapter<Technician,
        TechsAdapter.TechsViewHolder>(DiffCallback) {

    inner class TechsViewHolder(
        private var binding: ItemViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(tech: Technician?) {
            binding.tech = tech
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Technician>() {
        override fun areItemsTheSame(oldItem: Technician, newItem: Technician): Boolean {
            return oldItem.phone == newItem.phone
        }

        override fun areContentsTheSame(oldItem: Technician, newItem: Technician): Boolean {
            return oldItem.email == newItem.email
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TechsViewHolder {
        return TechsViewHolder(
            ItemViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: TechsViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }
}