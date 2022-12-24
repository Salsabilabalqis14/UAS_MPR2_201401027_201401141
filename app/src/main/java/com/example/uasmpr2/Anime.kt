package com.example.uasmpr2

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Anime(
    val name: String? = null,
    val genre: String? = null,
    val img: String? = null
) : Parcelable

