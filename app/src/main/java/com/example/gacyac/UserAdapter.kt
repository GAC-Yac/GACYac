package com.example.gacyac

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gacyac.databinding.UserProfileBinding

class UserAdapter(private val user: User): RecyclerView.Adapter<UserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = UserProfileBinding.inflate(from, parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        //holder.bindPost(user)
    }

    override fun getItemCount(): Int = 0
}