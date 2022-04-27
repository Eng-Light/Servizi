package com.example.servizi.user.ui.reviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.servizi.databinding.RevItemBinding
import com.example.servizi.technician.model.TechReviewResponse

class ReviewsAdapter : ListAdapter<TechReviewResponse,
        ReviewsAdapter.ReviewsViewHolder>(DiffCallback) {

    inner class ReviewsViewHolder(
        private var binding: RevItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(review: TechReviewResponse) {
            binding.review = review
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<TechReviewResponse>() {
        override fun areItemsTheSame(
            oldItem: TechReviewResponse,
            newItem: TechReviewResponse
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: TechReviewResponse,
            newItem: TechReviewResponse
        ): Boolean {
            return oldItem.content == newItem.content
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsViewHolder {
        return ReviewsViewHolder(
            RevItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ReviewsViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }
}
