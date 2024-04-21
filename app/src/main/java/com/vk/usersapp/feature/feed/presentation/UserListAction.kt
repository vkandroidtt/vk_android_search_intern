package com.vk.usersapp.feature.feed.presentation

import com.vk.usersapp.feature.feed.model.User

sealed interface UserListAction {
    data object Init : UserListAction
    data class QueryChanged(val query: String) : UserListAction
    data class UsersLoaded(val users: List<User>) : UserListAction
    data class LoadError(val error: String) : UserListAction
}