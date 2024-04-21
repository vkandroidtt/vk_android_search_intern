package com.vk.usersapp.feature.feed.model

data class UsersResponse(
    val users: List<User>,
    val total: Int,
    val skip: Int,
    val limit: Int,
)