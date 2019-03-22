package com.rbtgames.boardgame.feature.home.games.gameList.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.databinding.ItemGameListGameBinding
import com.rbtgames.boardgame.databinding.ItemGameListHintBinding

class GameListAdapter(private val onGameClicked: (game: GameViewModel) -> Unit) :
    ListAdapter<GameListListItem, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<GameListListItem>() {

        override fun areItemsTheSame(oldItem: GameListListItem, newItem: GameListListItem) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: GameListListItem, newItem: GameListListItem) = oldItem == newItem
    }) {

    private val exception by lazy { IllegalArgumentException("Invalid view type") }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is GameViewModel -> R.layout.item_game_list_game
        is HintViewModel -> R.layout.item_game_list_hint
        else -> throw exception
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        R.layout.item_game_list_game -> GameViewHolder.create(parent) { position -> onGameClicked(getItem(position) as GameViewModel) }
        R.layout.item_game_list_hint -> HintViewHolder.create(parent)
        else -> throw exception
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
        is GameViewHolder -> holder.bind(getItem(position) as GameViewModel)
        is HintViewHolder -> holder.bind(getItem(position) as HintViewModel)
        else -> throw exception
    }

    public override fun getItem(position: Int): GameListListItem = super.getItem(position)

    class GameViewHolder(private val binding: ItemGameListGameBinding, onItemClicked: (position: Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                adapterPosition.let { adapterPosition ->
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onItemClicked(adapterPosition)
                    }
                }
            }
        }

        fun bind(gameViewModel: GameViewModel) {
            binding.viewModel = gameViewModel
        }

        companion object {
            fun create(parent: ViewGroup, onItemClicked: (position: Int) -> Unit) =
                GameViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_game_list_game, parent, false), onItemClicked)
        }
    }

    class HintViewHolder(private val binding: ItemGameListHintBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: HintViewModel) {
            binding.viewModel = viewModel
        }

        companion object {
            fun create(parent: ViewGroup) = HintViewHolder(
                DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_game_list_hint, parent, false)
            )
        }
    }
}