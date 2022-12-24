package com.example.uasmpr2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.uasmpr2.databinding.ActivityDetailBinding
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null

        val getNameAnime = intent?.getStringExtra(EXTRA_ANIME_NAME).toString()
        binding.tvAnimeNameDetail.text = getNameAnime

        val getGenreAnime = intent?.getStringExtra(EXTRA_ANIME_GENRE).toString()
        binding.tvAnimeGenreDetail.text = getGenreAnime

        val getAnimeImg = intent?.getStringExtra(EXTRA_ANIME_IMG).toString()
        Picasso.get().load(getAnimeImg).into(binding.ivAnime)

        database = FirebaseDatabase.getInstance().getReference("anime")

        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val check = snapshot.child(getNameAnime).getValue()
                if (check != null){
                    binding.cbHeart.isChecked = true
                    binding.cbHeart.setOnCheckedChangeListener { checkBox, isChecked ->
                        database.child(getNameAnime).removeValue()
                        Toast.makeText(this@DetailActivity, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    binding.cbHeart.isChecked = false
                    binding.cbHeart.setOnCheckedChangeListener { checkBox, isChecked ->
                        val Anime = Anime(getNameAnime, getGenreAnime, getAnimeImg)
                        database.child(getNameAnime).setValue(Anime).addOnSuccessListener {
                            Toast.makeText(this@DetailActivity, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener{
                            Toast.makeText(this@DetailActivity, "Data gagal ditambahkan", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    companion object {
        const val EXTRA_ANIME_NAME = "extra_anime_name"
        const val EXTRA_ANIME_GENRE = "extra_anime_genre"
        const val EXTRA_ANIME_IMG = "extra_anime_img"
    }
}