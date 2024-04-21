package com.vk.usersapp.feature.feed.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.vk.usersapp.R
import com.vk.usersapp.feature.feed.model.User

class UserListAdapter : Adapter<UserListItemVh>() {

    private val dataset: MutableList<User> = mutableListOf()

    fun setUsers(users: List<User>) {
        dataset.clear()
        dataset.addAll(users)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListItemVh {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vh_user, parent, false)
        return UserListItemVh(view)
    }

    override fun onBindViewHolder(holder: UserListItemVh, position: Int) {
        holder.bind(dataset[position])
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}