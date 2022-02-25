package com.example.githubuser.helper

import android.database.Cursor
import com.example.githubuser.User
import com.example.githubuser.db.DatabaseContract

object MappingHelper {

    fun mapCursorToArrayList(usersCursor: Cursor?): ArrayList<User> {
        val usersList = ArrayList<User>()
        usersCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.NAME))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR))
                val isFavorite = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns.ISFAVORITE))
                usersList.add(User(id, name, username, avatar, isFavorite))
            }
        }
        return usersList
    }
}