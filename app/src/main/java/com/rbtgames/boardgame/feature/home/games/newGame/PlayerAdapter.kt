package com.rbtgames.boardgame.feature.home.games.newGame

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.databinding.ItemNewGameHintBinding
import com.rbtgames.boardgame.databinding.ItemNewGamePlayerBinding

class PlayerAdapter(private val onPlayerClicked: (player: PlayerViewModel) -> Unit) : ListAdapter<Any, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<Any>() {

    override fun areItemsTheSame(oldItem: Any, newItem: Any) = when {
        oldItem is PlayerViewModel && newItem is PlayerViewModel -> oldItem.player.id == newItem.player.id
        oldItem is Int && newItem is Int -> true
        else -> false
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any) = oldItem == newItem
}) {
    private val exception by lazy { IllegalArgumentException("Invalid view type") }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is PlayerViewModel -> R.layout.item_new_game_player
        is Int -> R.layout.item_new_game_hint
        else -> throw exception
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        R.layout.item_new_game_player -> PlayerViewHolder.create(parent) { position -> onPlayerClicked(getItem(position) as PlayerViewModel) }
        R.layout.item_new_game_hint -> HintViewHolder.create(parent)
        else -> throw exception
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when {
        holder is PlayerViewHolder -> holder.bind(getItem(position) as PlayerViewModel)
        holder is HintViewHolder -> holder.bind(getItem(position) as Int)
        else -> throw exception
    }

    class PlayerViewHolder(private val binding: ItemNewGamePlayerBinding, onItemClicked: (position: Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                adapterPosition.let { adapterPosition ->
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onItemClicked(adapterPosition)
                    }
                }
            }
        }

        fun bind(playerViewModel: PlayerViewModel) {
            binding.viewModel = playerViewModel
        }

        companion object {
            fun create(parent: ViewGroup, onItemClicked: (position: Int) -> Unit) =
                PlayerViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_new_game_player, parent, false), onItemClicked)
        }
    }

    class HintViewHolder(private val binding: ItemNewGameHintBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: Int) {
            binding.viewModel = viewModel
        }

        companion object {
            fun create(parent: ViewGroup) = HintViewHolder(
                DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_new_game_hint, parent, false)
            )
        }
    }
}