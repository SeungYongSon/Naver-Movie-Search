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

class MovieViewModel(private val mainContract: MovieContract) {

    var searchObservable = ObservableField<String>()
    var movieItemList = ObservableArrayList<Movie.Item>()

    private var search: String = ""

    private var loading = true
    private var isTotal = false

    private var pastVisibleItems: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0

    private var point: Int = 0

    fun onClickSearch() {
        if (!searchObservable.get()?.isEmpty()!!) {
            search = searchObservable.get().toString()

            point = 1
            isTotal = false
            loading = true

            Connect.postService.getMovie(
                Util.XNaverClientId,
                Util.XNaverClientSecret,
                search,
                point
            ).enqueue(object : Connect.Res<Movie>() {
                override fun callback(code: Int, body: Movie?) {
                    if (code == 200) {
                        if (body?.total != 0) {
                            movieItemList.clear()
                            movieItemList.addAll(body!!.items)
                        } else mainContract.toast("\"$search\" 검색 결과는 없습니다..")
                        mainContract.dismissWaitDialog()
                    } else {
                        mainContract.dismissWaitDialog()
                        mainContract.toast("검색결과를 가져오는 중에 오류가 생겼습니다. $code")
                    }
                }

                override fun fail(massage: String) {
                    mainContract.dismissWaitDialog()
                    mainContract.toast("검색결과를 가져오는 중에 예기치 못한 오류가 생겼습니다.")
                }
            })
            mainContract.showWaitDialog()
        } else mainContract.toast("검색어를 입력하세요.")
    }

    fun onClickMovieItem(link: String) {
        mainContract.showMovie(link)
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

                if(point != 1000) {
                    var count = 10
                    if (point + 10 > 1000) count--

                    if (loading && !isTotal) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            loading = false
                            Connect.postService.getMovie(
                                Util.XNaverClientId,
                                Util.XNaverClientSecret,
                                search,
                                point + count
                            ).enqueue(object : Connect.Res<Movie>() {
                                override fun callback(code: Int, body: Movie?) {
                                    if (code == 200) {
                                        movieItemList.addAll(body?.items!!)

                                        loading = true
                                        point += count
                                        isTotal = movieItemList.size >= body.total
                                    } else {
                                        loading = true
                                        mainContract.toast("결과를 가져오는 중에 오류가 생겼습니다. $code")
                                    }
                                }

                                override fun fail(massage: String) {
                                    loading = true
                                    mainContract.toast("결과를 가져오는 중에 예기치 못한 오류가 생겼습니다.")
                                }
                            })
                        }
                    }
                } else{
                    mainContract.toast("최대 1000개까지의 결과를 가져올 수 있습니다.")
                }
            }
        }
    }
}