package com.tails.boostcamp.contract

interface MovieContract{
    fun showWaitDialog()
    fun dismissWaitDialog()
    fun showMovie(url : String)
    fun toast(text: String)
}