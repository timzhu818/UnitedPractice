package com.example.unitedpractice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.unitedpractice.retrofit.Article
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import java.lang.Exception


class MainViewModel : ViewModel() {

    private val repository = MainRepository()

    private val _articleList = MutableLiveData<List<Article?>?>()
    private val _errorMessage = MutableLiveData<String>()

    private lateinit var job: Job

    var refreshing: Boolean = false

    val articleList: LiveData<List<Article?>?> = _articleList
    val errorMessage: LiveData<String> = _errorMessage

    init {
        makeApiCall()
    }

    fun makeApiCall() {
        job = CoroutineScope(IO).launch {
            try {
                val response = repository.getNews()
                withContext(Main) {
                    when (response.isSuccessful) {
                        true -> _articleList.value = response.body()?.articles
                        else -> response.errorBody()?.let { _errorMessage.value = it.toString() }
                    }
                }
            } catch (exception: Exception) {
                _errorMessage.postValue(exception.toString())
            }
        }.also { it.invokeOnCompletion { refreshing = false } }
    }

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }
}