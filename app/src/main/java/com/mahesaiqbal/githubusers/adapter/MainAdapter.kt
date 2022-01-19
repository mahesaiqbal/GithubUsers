package com.mahesaiqbal.githubusers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mahesaiqbal.githubusers.core.model.UsersResponse
import com.mahesaiqbal.githubusers.databinding.ItemListUserBinding

class MainAdapter : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private var listData = ArrayList<UsersResponse>()
    var onItemClick: ((UsersResponse) -> Unit)? = null

    fun setData(newListData: List<UsersResponse>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemListUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size

    inner class ViewHolder(binding: ItemListUserBinding) : RecyclerView.ViewHolder(binding.root) {
        private val cvUser = binding.cvUser
        private val civUser = binding.civUser
        private val tvUsername = binding.tvUsername
        private val tvId = binding.tvId
        private val tvReposUrl = binding.tvReposUrl

        fun bind(data: UsersResponse) {
            Glide.with(itemView.context)
                .load(data.avatarUrl)
                .override(500, 500)
                .into(civUser)

            tvUsername.text = data.login
            tvId.text = data.id.toString()
            tvReposUrl.text = data.reposUrl
        }

        init {
            cvUser.setOnClickListener {
                onItemClick?.invoke(listData[adapterPosition])
            }
        }
    }
}