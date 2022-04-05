package com.example.servizi.user.ui

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.servizi.user.model.Appointment
import com.example.servizi.user.model.Technician
import com.example.servizi.user.ui.my_orders.OrdersAdapter
import com.example.servizi.user.ui.technicians.TechsAdapter
import java.util.*

/*The @BindingAdapter annotation tells data binding to execute this binding adapter
when a View item has the imageUrl attribute.*/
@BindingAdapter("listData")
fun bindRecyclerView(
    recyclerView: RecyclerView,
    data: List<Technician>? = listOf(
        Technician(
            0,
            "",
            "",
            "",
            "",
            "",
            "",
            ""
        )
    )
) {
    val adapter = recyclerView.adapter as TechsAdapter
    adapter.submitList(data)
}

@BindingAdapter("OrdersListData")
fun bindOrdersRecyclerView(
    recyclerView: RecyclerView,
    data: List<Appointment>? = listOf(
        Appointment(
            0, "", "", "", "", 0, "",
            Technician(0, "", "", "", "", "", "", "")
        )
    )
) {
    val adapter = recyclerView.adapter as OrdersAdapter
    adapter.submitList(data)
}

@BindingAdapter("taskTitle")
fun bindTextView(
    textView: TextView,
    tv_task: String? = ""
) {
    textView.text = tv_task
}

@BindingAdapter("Text")
fun bindText(
    textView: TextView,
    tv_name: String? = ""
) {
    if (tv_name != null) {
        textView.text = tv_name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("Review")
fun bindReview(
    textView: TextView,
    tv_status: Int? = 2
) {
    when (tv_status) {
        0 -> textView.text = "Pending Review"
        1 -> textView.text = "Reviewed"
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("TextNumber")
fun bindTextId(
    textView: TextView,
    tv_Id: Int? = 0
) {
    textView.text = "ID: $tv_Id"
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
