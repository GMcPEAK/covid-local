package com.example.covid_local

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import kotlin.collections.List

class NewsActivity : AppCompatActivity() {

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder().build()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = getString(R.string.title1)
        setContentView(R.layout.activity_news)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this@NewsActivity)

        lateinit var adapter: StoryAdapter
        try {
            doAsync {
                val stories = getStories()
                runOnUiThread {
                    adapter = StoryAdapter(stories)
                    recyclerView.adapter = adapter
                }
            }
        }  catch (exception: Exception) {
            // Switch back to the UI Thread (required to update the UI)
            runOnUiThread {
                exception.printStackTrace()
                Toast.makeText(
                    this@NewsActivity,
                    "Failed to retrieve local news",
                    Toast.LENGTH_LONG
                ).show()
                Log.d("ERROR:", "Failed to retrieve local news")
            }
        }
    }

    fun getStories(): List<Story> {
        var searchTerm = getSharedPreferences("project2-byoi-covid-local", 0).getString("country", "")
        Log.d("NewsActivity", "Search term is $searchTerm")
        val stories = mutableListOf<Story>()
        // Create the Request object
        val key = getString(R.string.news_api_key)
        val request = Request.Builder()
            .url("https://newsapi.org/v2/everything?q=\"coronavirus delaware\"&sortBy=recent&apiKey=$key")
            .header("Authorization", "Bearer $key")
            .build()
        val response = okHttpClient.newCall(request).execute()
        // Get the JSON string body, if there was one
        val responseString = response.body?.string()
        Log.d("NewsActivity", "$responseString")
        if (response.isSuccessful && !responseString.isNullOrEmpty()) {
            // Parse our JSON string
            val json = JSONObject(responseString)
            val storiesJSON = json.getJSONArray("articles")
            var l = storiesJSON.length()
            Log.d("LocationsActivity", "Length of return array = $l")
            var size = l
            for (i in 0 until size) {
                val curr = storiesJSON.getJSONObject(i)
                Log.d("LocationsActivity", "curr = $curr")
                val author = curr.getString("author")
                val title = curr.getString("title")
                val description = curr.getString("description")
                val url = curr.getString("url")
                val imgUrl = curr.getString("urlToImage")
                val timeStamp = curr.getString("publishedAt")
                stories.add(
                    Story(
                        author = author,
                        title = title,
                        description = description,
                        url = url,
                        imgUrl = imgUrl,
                        timeStamp = timeStamp
                    )
                )
            }

        }
        for (story in stories) {
            var currName = story.title
            Log.d("Locations Activity:", "$currName")
        }
        return stories
    }
}