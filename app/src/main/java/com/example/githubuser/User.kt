package com.example.githubuser

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: Int? = 0,
    var name: String? = null,
    var username: String? = null,
    var avatar: String? = null,
    var isFavorite: Int? = 0
) : Parcelable
