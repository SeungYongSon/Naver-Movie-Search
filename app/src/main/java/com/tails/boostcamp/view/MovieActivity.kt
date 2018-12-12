package com.tails.boostcamp.view

import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tails.boostcamp.R
import com.tails.boostcamp.contract.MovieContract
import com.tails.boostcamp.databinding.ActivityMovieBinding
import com.tails.boostcamp.view.adapter.MovieAdapter
import com.tails.boostcamp.view.dialog.WaitDialogFragment
import com.tails.boostcamp.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.activity_movie.*
import saschpe.android.customtabs.CustomTabsHelper
import saschpe.android.customtabs.WebViewFallback

class MovieActivity : AppCompatActivity(), MovieContract {

    private val dialog = WaitDialogFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityMovieBinding>(this, R.layout.activity_movie)
        val movieViewModel = MovieViewModel(this as MovieContract)
        binding.viewModel = movieViewModel

        setupViews()
    }

    private fun setupViews(){
        movie_recycler.layoutManager = LinearLayoutManager(this)
        movie_recycler.adapter = MovieAdapter(this as MovieContract)
        movie_recycler.setHasFixedSize(true)
        movie_recycler.addItemDecoration(
            DividerItemDecoration(applicationContext, LinearLayoutManager(applicationContext).orientation))

        dialog.isCancelable = false
    }

    override fun showWaitDialog() {
        dialog.show(supportFragmentManager, "wait")
    }

    override fun dismissWaitDialog() {
        dialog.dismiss()
    }

    override fun toast(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    override fun showMovie(url: String) {
        val customTabsIntent = CustomTabsIntent.Builder()
            .addDefaultShareMenuItem()
            .setToolbarColor(applicationContext.getColor(R.color.colorPrimary))
            .setShowTitle(false)
            .build()

        CustomTabsHelper.addKeepAliveExtra(
            applicationContext,
            customTabsIntent.intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        )

        CustomTabsHelper.openCustomTab(
            applicationContext, customTabsIntent,
            Uri.parse(url),
            WebViewFallback()
        )
    }
}
