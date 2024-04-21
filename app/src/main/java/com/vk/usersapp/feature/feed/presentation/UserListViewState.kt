package com.vk.usersapp.feature.feed.presentation

import com.vk.usersapp.feature.feed.model.User

sealed class UserListViewState {
    data object Loading : UserListViewState()
    data class Error(val errorText: String?) : UserListViewState()
    data class List(val itemsList: kotlin.collections.List<User>) : UserListViewState()
}