package com.example.flixster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

private const val NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=01c2a4ad5d46852e4ffd2fd867f5554c"
private const val TAG = "MainActivity"
private lateinit var rvMovies: RecyclerView

//1.Define a data model class as the data source
//  Movie.kt
//2.Add the RecyclerView to the layout
//activity_main.xml
//3.Create a custom row layout XML file to visualize the item
//item_movie.xml
//4.Create an Adapter and ViewHolder to render the item
//MovieAdpater.kt
//5.Bind the adapter to the data source to populate the RecyclerView
//6.Bind a layout manager to the RecyclerView

class MainActivity : AppCompatActivity()
{
    private val movies = mutableListOf<Movie>()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvMovies = findViewById(R.id.rvMovies)

        val movieAdapter = MovieAdapter(this,movies)
        rvMovies.adapter = movieAdapter
        rvMovies.layoutManager = LinearLayoutManager(this)

        val client = AsyncHttpClient()
        client.get(NOW_PLAYING_URL,object: JsonHttpResponseHandler()
        {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            )
            {
                Log.e(TAG, "onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON)
            {
                Log.i(TAG,"onSuccess: JSON")
               try
               {
                   val movieJsonArray = json.jsonObject.getJSONArray("results")
                   movies.addAll(Movie.fromJsonArray(movieJsonArray))
                   movieAdapter.notifyDataSetChanged()
                   Log.i(TAG,"Movie list $movies")
               } catch (e: JSONException)
               {
                   Log.e(TAG,"Encountered exception $e")
               }


            }

        })
    }
}