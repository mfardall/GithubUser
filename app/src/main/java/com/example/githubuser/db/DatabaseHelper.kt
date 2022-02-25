package com.example.githubuser.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.githubuser.db.DatabaseContract.UserColumns.Companion.TABLE_NAME

internal class DatabaseHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "dbUserapp"
        private const val DATABASE_VERSION = 1
        private val UserColumns = DatabaseContract.UserColumns
        private const val SQL_CREATE_TABLE_USER = "CREATE TABLE $TABLE_NAME" +
                " (${UserColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${UserColumns.NAME} TEXT NOT NULL," +
                " ${UserColumns.USERNAME} TEXT NOT NULL," +
                " ${UserColumns.AVATAR} TEXT NOT NULL," +
                " ${UserColumns.ISFAVORITE} INT NOT NULL)"

    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}