package com.example.servizi.user.ui.my_orders

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.servizi.R
import com.example.servizi.databinding.ItemMyOrderBinding
import java.util.*

class StatusAdapter : ListAdapter<String,
        StatusAdapter.StatusViewHolder>(DiffCallback) {

    private var itemPosition: Int? = 0
    var onItemClick: ((Int) -> Unit)? = null

    inner class StatusViewHolder(
        private var binding: ItemMyOrderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.textView.setOnClickListener {
                itemPosition = adapterPosition
                onItemClick?.invoke(adapterPosition)
                notifyDataSetChanged()
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(status: String) {
            binding.textView.text = status.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }
            //binding.executePendingBindings()

            if (itemPosition == adapterPosition) {
                binding.cardV.apply {
                    setCardBackgroundColor(
                        resources.getColor(
                            R.color.primaryColor,
                            context.theme
                        )
                    )
                }
            } else {
                binding.cardV.apply {
                    setCardBackgroundColor(
                        resources.getColor(
                            R.color.primaryLightColor,
                            context.theme
                        )
                    )
                }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        return StatusViewHolder(
            ItemMyOrderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }
}