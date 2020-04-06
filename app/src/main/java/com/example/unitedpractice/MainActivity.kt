package com.example.unitedpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unitedpractice.retrofit.Article

class MainActivity : AppCompatActivity() {

    private val articles: MutableList<Article?>? = mutableListOf()
    private val articlesAdapter by lazy {
        ArticlesAdapter(articles, this@MainActivity)
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<RecyclerView>(R.id.list_recycler).run {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            adapter = articlesAdapter
        }

        viewModel.articleList.observe(this, Observer {
            setUpListView(it)
        })

        viewModel.errorMessage.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }

    private fun setUpListView(it: List<Article?>?) {
        it?.let { articleResponse ->
            articles?.apply {
                clear()
                addAll(articleResponse)
            }
            articlesAdapter.notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.refresh) {
            if (!viewModel.refreshing) {
                viewModel.refreshing = true
                viewModel.makeApiCall()
            } else {
                Toast.makeText(this, "We are refreshing your page, please be patient...", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
