package com.vk.usersapp.feature.feed.presentation

sealed interface UserListSideEffect {
    data class LoadUsers(val query: String) : UserListSideEffect
}