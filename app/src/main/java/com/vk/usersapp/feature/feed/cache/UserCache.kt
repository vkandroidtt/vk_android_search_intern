package com.vk.usersapp.feature.feed.cache

import android.content.Context
import com.vk.usersapp.feature.feed.model.User
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.concurrent.TimeUnit

class UsersCache private constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: UsersCache? = null

        fun getInstance(context: Context): UsersCache {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: UsersCache(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }

    private val cacheDir = File(context.cacheDir, "users_cache")
    private val searchCacheDir = File(context.cacheDir, "users_search_cache")
    private val cacheLifetime = TimeUnit.HOURS.toMillis(1)

    init {
        cacheDir.mkdirs()
        searchCacheDir.mkdirs()
    }

    fun cacheUsers(users: List<User>) {
        val jsonArray = JSONArray().apply {
            users.forEach { user ->
                put(JSONObject().apply {
                    put("firstName", user.firstName)
                    put("lastName", user.lastName)
                    put("image", user.image)
                    put("age", user.age)
                    put("university", user.university)
                })
            }
        }

        val cacheFile = File(cacheDir, "users_list.json")
        val cacheData = JSONObject().apply {
            put("timestamp", System.currentTimeMillis())
            put("users", jsonArray)
        }

        cacheFile.writeText(cacheData.toString())
    }

    fun getCachedUsers(): List<User>? {
        val cacheFile = File(cacheDir, "users_list.json")
        if (!cacheFile.exists()) return null

        return try {
            val jsonData = JSONObject(cacheFile.readText())
            val timestamp = jsonData.getLong("timestamp")

            if (System.currentTimeMillis() - timestamp > cacheLifetime) {
                cacheFile.delete()
                return null
            }

            val usersArray = jsonData.getJSONArray("users")
            List(usersArray.length()) { index ->
                val userJson = usersArray.getJSONObject(index)
                User(
                    firstName = userJson.getString("firstName"),
                    lastName = userJson.getString("lastName"),
                    image = userJson.getString("image"),
                    age = userJson.getInt("age"),
                    university = userJson.getString("university")
                )
            }
        } catch (e: Exception) {
            cacheFile.delete()
            null
        }
    }

    fun cacheSearchResults(query: String, users: List<User>) {
        val jsonArray = JSONArray().apply {
            users.forEach { user ->
                put(JSONObject().apply {
                    put("firstName", user.firstName)
                    put("lastName", user.lastName)
                    put("image", user.image)
                    put("age", user.age)
                    put("university", user.university)
                })
            }
        }

        val cacheFile = File(searchCacheDir, "${query.hashCode()}.json")
        val cacheData = JSONObject().apply {
            put("timestamp", System.currentTimeMillis())
            put("query", query)
            put("users", jsonArray)
        }

        cacheFile.writeText(cacheData.toString())
    }

    fun getCachedSearchResults(query: String): List<User>? {
        val cacheFile = File(searchCacheDir, "${query.hashCode()}.json")
        if (!cacheFile.exists()) return null

        return try {
            val jsonData = JSONObject(cacheFile.readText())
            val timestamp = jsonData.getLong("timestamp")
            val cachedQuery = jsonData.getString("query")

            if (System.currentTimeMillis() - timestamp > cacheLifetime || cachedQuery != query) {
                cacheFile.delete()
                return null
            }

            val usersArray = jsonData.getJSONArray("users")
            List(usersArray.length()) { index ->
                val userJson = usersArray.getJSONObject(index)
                User(
                    firstName = userJson.getString("firstName"),
                    lastName = userJson.getString("lastName"),
                    image = userJson.getString("image"),
                    age = userJson.getInt("age"),
                    university = userJson.getString("university")
                )
            }
        } catch (e: Exception) {
            cacheFile.delete()
            null
        }
    }

    fun clearCache() {
        cacheDir.listFiles()?.forEach { it.delete() }
        searchCacheDir.listFiles()?.forEach { it.delete() }
    }
}