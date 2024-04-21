package com.vk.usersapp.feature.feed.presentation

class UserListReducer {
    fun applyAction(action: UserListAction, state: UserListState): UserListState {
        return when (action) {
            UserListAction.Init -> state.copy(isLoading = true)
            is UserListAction.QueryChanged -> state.copy(query = action.query, isLoading = true)
            is UserListAction.UsersLoaded -> state.copy(items = action.users, isLoading = false)
            is UserListAction.LoadError -> state.copy(error = action.error, isLoading = false)
        }
    }
}