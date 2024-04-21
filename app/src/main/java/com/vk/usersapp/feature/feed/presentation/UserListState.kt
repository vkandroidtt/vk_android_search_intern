package com.vk.usersapp.feature.feed.presentation

import com.vk.usersapp.feature.feed.model.User

data class UserListState(
    val query: String = "",
    val items: List<User> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = true,
)