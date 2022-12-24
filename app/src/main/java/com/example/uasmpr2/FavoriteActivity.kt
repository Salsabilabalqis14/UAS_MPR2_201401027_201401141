package com.example.uasmpr2

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uasmpr2.databinding.ActivityFavoriteBinding
import com.google.firebase.database.*

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorite"

        database = FirebaseDatabase.getInstance().getReference("anime")

        getAnimeData()

    }

    private fun getAnimeData() {
        val listAnime = ArrayList<Anime>()

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (a : DataSnapshot in snapshot.children) {
                        val an = a.getValue(Anime::class.java)
                        if (an != null) {
                            listAnime.add(an)
                        }
                    }
                    showRecycler(listAnime)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun showRecycler(listAnime: ArrayList<Anime>) {
        if (applicationContext.resources.configuration.orientation == Configuration
                .ORIENTATION_LANDSCAPE) {
            binding.listAnimeFav.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.listAnimeFav.layoutManager = LinearLayoutManager(this)
        }

        val adapter = AnimeAdapter(listAnime)
        binding.listAnimeFav.adapter = adapter

        adapter.setOnItemClickCallback(object : AnimeAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Anime) {
                val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_ANIME_NAME, data.name)
                intent.putExtra(DetailActivity.EXTRA_ANIME_GENRE, data.genre)
                intent.putExtra(DetailActivity.EXTRA_ANIME_IMG, data.img)
                startActivity(intent)
            }
        })
    }
}