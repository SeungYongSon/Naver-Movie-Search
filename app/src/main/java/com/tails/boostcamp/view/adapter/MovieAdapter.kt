package com.tails.boostcamp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tails.boostcamp.BR
import com.tails.boostcamp.contract.MovieContract
import com.tails.boostcamp.databinding.MovieItemBinding
import com.tails.boostcamp.model.Movie
import com.tails.boostcamp.viewmodel.MovieViewModel

class MovieAdapter(private val movieContract: MovieContract) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    var item: List<Movie.Item> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.viewModel = MovieViewModel(movieContract)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = item.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item[position])
    }

    class ViewHolder(private val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Movie.Item) {
            binding.setVariable(BR.movieItem, item)
        }
    }
}