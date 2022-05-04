package com.example.servizi

import android.annotation.SuppressLint
import android.os.Build
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.servizi.technician.model.TechReviewResponse
import com.example.servizi.technician.ui.orders.TechOrdersAdapter
import com.example.servizi.user.model.Appointment
import com.example.servizi.user.model.Technician
import com.example.servizi.user.model.User
import com.example.servizi.user.model.UserData
import com.example.servizi.user.ui.my_orders.OrdersAdapter
import com.example.servizi.user.ui.reviews.ReviewsAdapter
import com.example.servizi.user.ui.technicians.TechsAdapter
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
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
            "",
            0.0f
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
            Technician(0, "", "", "", "", "", "", "", 0.0f)
        )
    )
) {
    val adapter = recyclerView.adapter as OrdersAdapter
    adapter.submitList(data)
}

@BindingAdapter("TechOrdersListData")
fun bindTechOrdersRecyclerView(
    recyclerView: RecyclerView,
    data: List<com.example.servizi.technician.model.Appointment>? = listOf(
        com.example.servizi.technician.model.Appointment(
            0, "", "", "", "", 0, "",
            UserData(
                "",
                "",
                "",
                "",
                "",
                "",
                ""
            )
        )
    )
) {
    val adapter = recyclerView.adapter as TechOrdersAdapter
    adapter.submitList(data)
}

@BindingAdapter("ratingList")
fun bindRatingList(
    recyclerView: RecyclerView,
    data: List<TechReviewResponse>? = listOf(
        TechReviewResponse(
            "",
            "",
            0,
            "",
            "",
            "",
            "",
            User("", "")
        )
    )
) {
    val adapter = recyclerView.adapter as ReviewsAdapter
    adapter.submitList(data)
}

@BindingAdapter("taskTitle")
fun bindTextView(
    textView: TextView,
    tv_task: String? = ""
) {
    textView.text = tv_task
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SimpleDateFormat", "SetTextI18n")
@BindingAdapter("date")
fun bindDate(
    textView: TextView,
    date: String? = ""
) {
    textView.text = getShortDate(date!!)
}

@SuppressLint("SimpleDateFormat")
@RequiresApi(Build.VERSION_CODES.O)
fun getShortDate(ts: String): String {
    return Instant.parse(ts).atOffset(ZoneOffset.UTC)
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm"))
}

@SuppressLint("SetTextI18n")
@BindingAdapter("ReviewsNumberData")
fun bindTextViewNumber(
    textView: TextView,
    tv_task: Int? = 0
) {
    textView.text = "${tv_task.toString()} Reviews"
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

@BindingAdapter("rating")
fun bindRating(
    ratingBar: RatingBar,
    rating: Float? = 0.0f
) {
    ratingBar.rating = rating!!
}

@SuppressLint("SetTextI18n")
@BindingAdapter("ratingNumber")
fun bindRatingNumber(
    textView: TextView,
    rating: Float? = 0.0f
) {
    if (rating != null) {
        textView.text = "%.1f".format(rating)
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("ratingTechNumber")
fun bindRatingTechNumber(
    textView: TextView,
    rating: Float? = 0.0f
) {
    if (rating != null) {
        textView.text = "Rating: %.1f".format(rating)
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
