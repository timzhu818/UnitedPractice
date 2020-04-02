package com.example.unitedpractice.retrofit

data class NewsResponse(
    val status: String?,
    val totalResults: Int?,
    val articles: List<Article?>?
)
