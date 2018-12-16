package com.tails.boostcamp.viewmodel

import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tails.boostcamp.contract.MovieContract
import com.tails.boostcamp.model.Movie
import com.tails.boostcamp.util.Connect
import com.tails.boostcamp.util.Util

class MovieViewModel(private val movieContract: MovieContract) {

    var searchObservable = ObservableField<String>().apply {
        set("")
    }
    var movieItemList = ObservableArrayList<Movie.Item>()

    private var search: String = ""

    private var load = true
    private var isTotal = false

    private var pastVisibleItems: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0

    private var point: Int = 0

    private fun resetData() {
        search = searchObservable.get().toString()
        point = 0
        isTotal = false
        load = true
    }

    private fun failSearch(text: String) {
        if(point == 0 && !searchObservable.get()?.isEmpty()!!) movieContract.dismissWaitDialog()
        movieContract.toast(text)
    }

    private fun searchMovie(start : Int) {
        Connect.postService.getMovie(
            Util.XNaverClientId,
            Util.XNaverClientSecret,
            search,
            point + start
        ).enqueue(object : Connect.Res<Movie>() {
            override fun callback(code: Int, body: Movie?) {
                if (code == 200) {
                    if(start == 1) movieItemList.clear()
                    if (body!!.total != 0) {
                        movieItemList.addAll(body.items)
                        movieContract.dismissWaitDialog()
                        point += start
                    } else failSearch("\"$search\"\n검색 결과는 없습니다..")
                    isTotal = movieItemList.size >= body.total
                    load = true
                } else failSearch("검색결과를 가져오는 중에\n오류가 생겼습니다. $code")
            }

            override fun fail(massage: String) {
                load = true
                failSearch("검색결과를 가져오는 중에\n예기치 못한 오류가 생겼습니다.")
            }
        })
        if (point == 0) movieContract.showWaitDialog()
    }

    fun onClickSearch() {
        if (!searchObservable.get()?.isEmpty()!!) {
            resetData()
            searchMovie(1)
        } else failSearch("검색어를 입력하세요.")
    }

    fun onClickMovieItem(link: String) {
        movieContract.showMovie(link)
    }

    val editSearch = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(c: CharSequence?, p1: Int, p2: Int, p3: Int) {
            searchObservable.set(c.toString())
        }
    }

    val isScrollEnd = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 0) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager

                visibleItemCount = layoutManager.childCount
                totalItemCount = layoutManager.itemCount
                pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    if (point != 1000) {
                        if (load && !isTotal) {
                            load = false
                            val count = if (point + 10 > 1000) 9 else 10
                            searchMovie(count)
                        }
                    } else failSearch("최대 1000개까지의 검색결과를\n가져올 수 있습니다.")
                }
            }
        }
    }
}