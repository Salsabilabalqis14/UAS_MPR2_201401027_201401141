package com.example.uasmpr2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class AnimeAdapter(private val listAnime: ArrayList<Anime>) : RecyclerView.Adapter<AnimeAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_anime, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.tvItemName.text = listAnime[position].name.toString()
        viewHolder.tvItemGenre.text = listAnime[position].genre

        val img = listAnime[position].img

        Picasso.get().load(img).into(viewHolder.avatar)

        viewHolder.itemView.setOnClickListener{
            onItemClickCallback.onItemClicked(listAnime[viewHolder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return listAnime.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvItemName: TextView = view.findViewById(R.id.tv_anime_nameDetail)
        val tvItemGenre: TextView = view.findViewById(R.id.tv_anime_genre)
        val avatar: ImageView = view.findViewById(R.id.img_anime)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Anime)
    }
}