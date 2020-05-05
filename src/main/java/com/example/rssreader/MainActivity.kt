package com.example.rssreader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rssreader.adapter.FeedAdapter
import com.example.rssreader.common.RetrofitServiceGenerator
import com.example.rssreader.model.RSSObject
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val RSS_link = "https://people.onliner.by/feed"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.title = "NEWS by Onliner"

        setSupportActionBar(toolbar)

        val linearLayoutManager = LinearLayoutManager(baseContext,
            LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        loadRSS()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_refresh) {
            loadRSS()
        }
        return true
    }

    private fun loadRSS() {
        val call = RetrofitServiceGenerator.createService().getFeed(RSS_link)
        call.enqueue(object : Callback<RSSObject> {
            override fun onFailure(call: Call<RSSObject>?, t: Throwable?) {
                Log.d("ResponseError", "failed")
            }
            override fun onResponse(call: Call<RSSObject>?, response:
            Response<RSSObject>?) {
                if (response?.isSuccessful == true) {
                    response.body()?.let { rssObject ->
                        Log.d("Response", "${rssObject.toString()}")
                        val adapter = FeedAdapter(rssObject, baseContext)
                        recyclerView.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }
}
