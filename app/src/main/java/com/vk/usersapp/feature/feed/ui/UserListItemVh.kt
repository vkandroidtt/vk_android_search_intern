package com.vk.usersapp.feature.feed.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vk.usersapp.R
import com.vk.usersapp.feature.feed.model.User
import android.annotation.SuppressLint

class UserListItemVh(view: View) : RecyclerView.ViewHolder(view) {

    private val avatar: ImageView = view.findViewById(R.id.photo)
    private val title: TextView = view.findViewById(R.id.title)
    private val subtitle: TextView = view.findViewById(R.id.subtitle)
    private val university: TextView = view.findViewById(R.id.university)

    @SuppressLint("SetTextI18n")
    fun bind(user: User) {
        Glide.with(avatar).load(user.image).into(avatar)
        title.text = "${user.firstName} ${user.lastName}"
        university.text = user.university
        subtitle.text = user.age.toString()

    }
}