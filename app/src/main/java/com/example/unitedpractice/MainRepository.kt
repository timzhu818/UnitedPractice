package com.example.unitedpractice

import com.example.unitedpractice.retrofit.RetrofitController


class MainRepository {

    private var client = RetrofitController.requestApi

    suspend fun getNews() = client.getBody()
}