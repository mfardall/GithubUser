package com.example.githubuser

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getUser(@Query("q") query: String) : Call<GithubResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getDetailUser(@Path("username") username: String) : Call<UserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getUserFollowers(@Path("username") username: String) : Call<List<UserResponse>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getUserFollowing(@Path("username") username: String) : Call<List<UserResponse>>
}