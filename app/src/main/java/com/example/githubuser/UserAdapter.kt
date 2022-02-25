package com.example.githubuser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class UserAdapter(private val listUser: List<Any>?, private val viewType: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback
    var listUsers = ArrayList<User>()
        set(listUsers) {
            if (listUsers.size > 0) {
                this.listUsers.clear()
            }
            this.listUsers.addAll(listUsers)
        }

    fun addItem(user: User?) {
        if (user != null) {
            this.listUsers.add(user)
        }
        notifyItemInserted(this.listUsers.size - 1)
    }

    fun removeItem(position: Int) {
        this.listUsers.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listUsers.size)
    }

    companion object {
        const val LAYOUT_ONE = 0
        const val LAYOUT_TWO = 1
        const val LAYOUT_THREE = 2
        const val LAYOUT_FOUR = 3
    }

    override fun getItemViewType(position: Int): Int {
        return when (viewType) {
            0 -> {
                LAYOUT_ONE
            }
            1 -> {
                LAYOUT_TWO
            }
            2 -> {
                LAYOUT_THREE
            }
            3 -> {
                LAYOUT_FOUR
            }
            else -> 4
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.tv_user_name)
        var ivUser: ImageView = itemView.findViewById(R.id.iv_user)
    }

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.tv_user_name)
        var ivUser: ImageView = itemView.findViewById(R.id.iv_user)
        var tvUsername: TextView = itemView.findViewById(R.id.tv_user_username)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_users, parent, false)
        return if (viewType == LAYOUT_FOUR) {
            FavoriteViewHolder(view)
        } else {
            ListViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            LAYOUT_ONE -> {
                val listHolder = holder as ListViewHolder
                val list = listUser?.get(position) as UsersItem
                listHolder.tvName.text = list.login
                Glide
                    .with(listHolder.itemView.context)
                    .load(list.avatarUrl)
                    .into(listHolder.ivUser)
                listHolder.itemView.setOnClickListener {
                    onItemClickCallback.onItemClicked(null, null, list.login)
                }
            }
            LAYOUT_TWO -> {
                val listHolder = holder as ListViewHolder
                val list = listUser?.get(position) as UserResponse
                listHolder.tvName.text = list.login
                Glide
                    .with(listHolder.itemView.context)
                    .load(list.avatarUrl)
                    .into(listHolder.ivUser)

            }
            LAYOUT_THREE -> {
                val listHolder = holder as ListViewHolder
                val list = listUser?.get(position) as UserResponse
                listHolder.tvName.text = list.login
                Glide
                    .with(listHolder.itemView.context)
                    .load(list.avatarUrl)
                    .into(listHolder.ivUser)

            }
            LAYOUT_FOUR -> {
                val favHolder = holder as FavoriteViewHolder
                val list = listUsers[position]
                favHolder.tvName.text = list.name
                favHolder.tvUsername.text = list.username
                Glide
                    .with(favHolder.itemView.context)
                    .load(list.avatar)
                    .into(favHolder.ivUser)
                favHolder.itemView.setOnClickListener {
                    onItemClickCallback.onItemClicked(listUsers[favHolder.adapterPosition], favHolder.adapterPosition, null)
                }
            }
        }

    }

    override fun getItemCount(): Int = if (getItemViewType(viewType) == LAYOUT_FOUR) listUsers.size else listUser!!.size

    interface OnItemClickCallback {
        fun onItemClicked(data: User?, position: Int?, username: String?)
    }
}