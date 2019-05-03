package com.rbtgames.boardgame.feature.gameDetail.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.databinding.ItemGameDetailCounterBinding
import com.rbtgames.boardgame.databinding.ItemGameDetailPlayerBinding

class GameDetailAdapter : ListAdapter<GameDetailListItem, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<GameDetailListItem>() {

    override fun areItemsTheSame(oldItem: GameDetailListItem, newItem: GameDetailListItem) = oldItem.id == newItem.id

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: GameDetailListItem, newItem: GameDetailListItem) = oldItem == newItem

    override fun getChangePayload(oldItem: GameDetailListItem, newItem: GameDetailListItem) = ""
}) {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is PlayerViewModel -> R.layout.item_game_detail_player
        is CounterViewModel -> R.layout.item_game_detail_counter
        else -> throw IllegalArgumentException("Unsupported item type at position $position.")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        R.layout.item_game_detail_player -> PlayerViewHolder.create(parent)
        R.layout.item_game_detail_counter -> CounterViewHolder.create(parent)
        else -> throw IllegalArgumentException("Unsupported item type: $viewType.")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PlayerViewHolder -> holder.bind(getItem(position) as PlayerViewModel)
            is CounterViewHolder -> holder.bind(getItem(position) as CounterViewModel)
        }
    }

    class PlayerViewHolder(private val binding: ItemGameDetailPlayerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(playerViewModel: PlayerViewModel) {
            binding.viewModel = playerViewModel
        }

        companion object {
            fun create(parent: ViewGroup) =
                PlayerViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_game_detail_player, parent, false))
        }
    }

    class CounterViewHolder(private val binding: ItemGameDetailCounterBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(counterViewModel: CounterViewModel) {
            binding.viewModel = counterViewModel
        }

        companion object {
            fun create(parent: ViewGroup) =
                CounterViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_game_detail_counter, parent, false))
        }
    }
}