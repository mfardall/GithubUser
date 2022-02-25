package com.example.githubuser

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GithubResponse(

	@field:SerializedName("total_count")
	val totalCount: Int,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean,

	@field:SerializedName("items")
	val items: List<UsersItem>
) : Parcelable

@Parcelize
data class UsersItem(

	@field:SerializedName("score")
	val score: Double,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("repos_url")
	val reposUrl: String,

	@field:SerializedName("html_url")
	val htmlUrl: String,

	@field:SerializedName("following_url")
	val followingUrl: String,

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("followers_url")
	val followersUrl: String,

	@field:SerializedName("url")
	val url: String
) : Parcelable
