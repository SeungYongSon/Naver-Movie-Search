package com.tails.boostcamp.model

import androidx.databinding.ObservableArrayList

class Movie(
    val lastBuildDate: String,
    val total: Int,
    val start: Int,
    val display: Int,
    val items: ObservableArrayList<Item>){

    class Item(
        val title: String,
        val link: String,
        val image: String,
        val subtitle: String,
        val pubDate: String,
        val director: String,
        val actor: String,
        val userRating: Float)
}

