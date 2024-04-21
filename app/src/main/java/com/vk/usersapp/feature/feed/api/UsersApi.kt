package com.vk.usersapp.feature.feed.api

import com.vk.usersapp.feature.feed.model.User
import com.vk.usersapp.feature.feed.model.UsersResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface UsersApi {
    @GET("users")
    suspend fun getUsers(@Query("limit") limit: Int, @Query("skip") skip: Int): UsersResponse

    @GET("users/search")
    suspend fun searchUsers(@Query("q") query: String, @Query("limit") limit: Int, @Query("skip") skip: Int): UsersResponse
}