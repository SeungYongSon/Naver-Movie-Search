package com.tails.boostcamp.view.binding

import android.os.Build
import android.text.Html
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tails.boostcamp.R
import com.tails.boostcamp.model.Movie
import com.tails.boostcamp.view.adapter.MovieAdapter

object Bind {

    @JvmStatic
    @BindingAdapter("movie")
    fun movieBindingAdapter(recyclerView: RecyclerView, item: ObservableArrayList<Movie.Item>) {
        val adapter = recyclerView.adapter as MovieAdapter
        adapter.item = item
        adapter.notifyDataSetChanged()
    }

    @JvmStatic
    @BindingAdapter("scrollEvent")
    fun scrollEventListener(recyclerView: RecyclerView, listener: RecyclerView.OnScrollListener) {
        recyclerView.addOnScrollListener(listener)
    }

    @JvmStatic
    @BindingAdapter("editEvent")
    fun editEventListener(editText: EditText, listener: TextWatcher) {
        editText.addTextChangedListener(listener)
    }

    @JvmStatic
    @BindingAdapter("glideLink")
    fun glideLinkBindingAdapter(imageView: ImageView, link: String) {
        Glide.with(imageView.context)
            .load(link)
            .apply(RequestOptions().error(R.drawable.noimage))
            .into(imageView)
    }

    @JvmStatic
    @BindingAdapter("textHtml")
    fun textHtmlBindingAdapter(textView: TextView, text: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            textView.text = Html.fromHtml(text, 0)
        else textView.text = Html.fromHtml(text)
    }
}