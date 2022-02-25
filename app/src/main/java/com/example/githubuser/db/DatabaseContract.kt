package com.example.githubuser.db

import android.provider.BaseColumns

internal class DatabaseContract {

    internal class UserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "user"
            const val _ID = "_id"
            const val NAME = "name"
            const val USERNAME = "username"
            const val AVATAR = "avatar"
            const val ISFAVORITE = "isfavorite"
        }
    }
}