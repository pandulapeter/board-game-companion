package com.rbtgames.boardgame.feature.gameDetail.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.databinding.ItemGameDetailPlayerBinding

class GameDetailAdapter : ListAdapter<PlayerViewModel, GameDetailAdapter.ViewHolder>(object : DiffUtil.ItemCallback<PlayerViewModel>() {

    override fun areItemsTheSame(oldItem: PlayerViewModel, newItem: PlayerViewModel) = oldItem.player.id == newItem.player.id

    override fun areContentsTheSame(oldItem: PlayerViewModel, newItem: PlayerViewModel) = oldItem == newItem
}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.create(parent)

    override fun onBindViewHolder(holder: GameDetailAdapter.ViewHolder, position: Int) = holder.bind(getItem(position))

    class ViewHolder(private val binding: ItemGameDetailPlayerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(playerViewModel: PlayerViewModel) {
            binding.viewModel = playerViewModel
        }

        companion object {
            fun create(parent: ViewGroup) =
                ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_game_detail_player, parent, false))
        }
    }
}