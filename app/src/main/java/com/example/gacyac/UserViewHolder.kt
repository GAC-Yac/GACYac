package com.example.gacyac

import androidx.recyclerview.widget.RecyclerView
import com.example.gacyac.databinding.UserProfileBinding

class UserViewHolder(private val userBinding: UserProfileBinding):RecyclerView.ViewHolder(userBinding.root) {
    fun bindPost(user: User){
        userBinding.username.text = user.username
        userBinding.bonusPoints.text = user.bonuspoints.toString()
        userBinding.dateJoinTextView.text = user.dateJoined.toString()
    }
}