package com.tails.boostcamp.util

import com.tails.boostcamp.model.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface Connect {
    companion object {
        private val retrofit = Retrofit.Builder()
            .baseUrl("https://openapi.naver.com/v1/search/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val postService: Connect = retrofit.create(Connect::class.java)
    }

    @GET("movie.json")
    fun getMovie(
        @Header("X-Naver-Client-Id") clientId: String,
        @Header("X-Naver-Client-Secret") clientSecret: String,
        @Query("query") query: String,
        @Query("start") start: Int) : Call<Movie>

    abstract class Res<T> : Callback<T> {

        override fun onResponse(call: Call<T>, response: Response<T>) {
            callback(response.code(), response.body())
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            fail(t.message.toString())
        }

        abstract fun callback(code: Int, body: T?)
        abstract fun fail(massage : String)
    }
}
