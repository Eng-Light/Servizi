package com.example.servizi.user.ui.technicians

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.servizi.user.model.Technician

/*The @BindingAdapter annotation tells data binding to execute this binding adapter
when a View item has the imageUrl attribute.*/
@BindingAdapter("listData")
fun bindRecyclerView(
    recyclerView: RecyclerView,
    data: List<Technician>?
) {
    val adapter = recyclerView.adapter as TechsAdapter
    adapter.submitList(data)
}

@BindingAdapter("taskTitle")
fun bindTextView(
    textView: TextView,
    tv_task: String?
) {
    textView.text = tv_task
}

/*
@BindingAdapter("TasksApiStatus")
fun bindStatus(
    statusImageView: ImageView,
    status: TasksApiStatus?
) {
    when (status) {
        TasksApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        TasksApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        TasksApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}*/
