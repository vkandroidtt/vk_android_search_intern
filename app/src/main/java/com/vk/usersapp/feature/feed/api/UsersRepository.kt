package com.vk.usersapp.feature.feed.api

import android.util.Log
import com.vk.usersapp.UserApplication
import com.vk.usersapp.core.Retrofit
import com.vk.usersapp.feature.feed.cache.UsersCache
import com.vk.usersapp.feature.feed.model.User

class UsersRepository() {
    private val api: UsersApi by lazy { Retrofit.getClient().create(UsersApi::class.java) }
    private val cache by lazy {
        UsersCache.getInstance(UserApplication.getInstance())
    }
    suspend fun getUsers(): List<User> {
        cache.getCachedUsers()?.let { users ->
            Log.d("UserRepository", "Информация о пользователях из кэша " + users)
            return users
        }

        return api.getUsers(
            limit = 30,
            skip = 0
        ).users.also { users ->
            cache.cacheUsers(users)
        }
    }

    suspend fun searchUsers(query: String): List<User> {
        cache.getCachedSearchResults(query)?.let { users ->
            Log.d("UserRepository", "Информация о пользователях из кэша " + users)
            return users
        }

        return api.searchUsers(
            query = query,
            limit = 30,
            skip = 0
        ).users.also { users ->
            cache.cacheSearchResults(query, users)
        }
    }
}