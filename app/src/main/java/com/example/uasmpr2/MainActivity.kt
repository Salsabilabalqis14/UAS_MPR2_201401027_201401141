package com.example.uasmpr2

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uasmpr2.databinding.ActivityMainBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val listCat = ArrayList<Anime>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Anime"
        getListAnime()
    }

    private fun getListAnime() {
        val client = AsyncHttpClient()
        val url = "https://gogoanime.consumet.org/top-airing"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>,responseBody: ByteArray) {
                val result = String(responseBody)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)

                        val title = jsonObject.getString("animeTitle")
                        val img = jsonObject.getString("animeImg")

                        val genres = jsonObject.getJSONArray("genres")
                        var genre: String? = null
                        for (j in 0 until genres.length()) {
                            genre = if (genre == null)
                                genres[j].toString()
                            else
                                "$genre, " + genres[j].toString()
                        }

                        listCat.add(Anime(title, genre, img))
                    }

                    showRecycler()
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity,e.message, Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Toast.makeText(this@MainActivity, statusCode, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showRecycler() {
        if (applicationContext.resources.configuration.orientation == Configuration
                .ORIENTATION_LANDSCAPE) {
            binding.listAnime.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.listAnime.layoutManager = LinearLayoutManager(this)
        }

        val adapter = AnimeAdapter(listCat)
        binding.listAnime.adapter = adapter

        adapter.setOnItemClickCallback(object : AnimeAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Anime) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_ANIME_NAME, data.name)
                intent.putExtra(DetailActivity.EXTRA_ANIME_GENRE, data.genre)
                intent.putExtra(DetailActivity.EXTRA_ANIME_IMG, data.img)
                startActivity(intent)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu1 -> {
                val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return true
        }
    }
}