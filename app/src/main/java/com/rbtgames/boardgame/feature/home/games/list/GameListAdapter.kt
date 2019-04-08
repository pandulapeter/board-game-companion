package com.rbtgames.boardgame.feature.home.games.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.data.model.Game
import com.rbtgames.boardgame.databinding.ItemGameListGameActiveBinding
import com.rbtgames.boardgame.databinding.ItemGameListGameFinishedBinding
import com.rbtgames.boardgame.databinding.ItemGameListHintBinding

class GameListAdapter(private val onGameClicked: (game: Game) -> Unit) :
    ListAdapter<GameListListItem, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<GameListListItem>() {

        override fun areItemsTheSame(oldItem: GameListListItem, newItem: GameListListItem) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: GameListListItem, newItem: GameListListItem) = oldItem == newItem
    }) {

    private val exception by lazy { IllegalArgumentException("Invalid view type") }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is ActiveGameViewModel -> R.layout.item_game_list_game_active
        is FinishedGameViewModel -> R.layout.item_game_list_game_finished
        is HintViewModel -> R.layout.item_game_list_hint
        else -> throw exception
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        R.layout.item_game_list_game_active -> ActiveGameViewHolder.create(parent) { position -> onGameClicked((getItem(position) as ActiveGameViewModel).game) }
        R.layout.item_game_list_game_finished -> FinishedGameViewHolder.create(parent) { position -> onGameClicked((getItem(position) as FinishedGameViewModel).game) }
        R.layout.item_game_list_hint -> HintViewHolder.create(parent)
        else -> throw exception
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
        is ActiveGameViewHolder -> holder.bind(getItem(position) as ActiveGameViewModel)
        is FinishedGameViewHolder -> holder.bind(getItem(position) as FinishedGameViewModel)
        is HintViewHolder -> holder.bind(getItem(position) as HintViewModel)
        else -> throw exception
    }

    public override fun getItem(position: Int): GameListListItem = super.getItem(position)

    class ActiveGameViewHolder(private val binding: ItemGameListGameActiveBinding, onItemClicked: (position: Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                adapterPosition.let { adapterPosition ->
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onItemClicked(adapterPosition)
                    }
                }
            }
        }

        fun bind(gameViewModel: ActiveGameViewModel) {
            binding.viewModel = gameViewModel
        }

        companion object {
            fun create(parent: ViewGroup, onItemClicked: (position: Int) -> Unit) =
                ActiveGameViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_game_list_game_active, parent, false), onItemClicked)
        }
    }

    class FinishedGameViewHolder(private val binding: ItemGameListGameFinishedBinding, onItemClicked: (position: Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                adapterPosition.let { adapterPosition ->
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onItemClicked(adapterPosition)
                    }
                }
            }
        }

        fun bind(gameViewModel: FinishedGameViewModel) {
            binding.viewModel = gameViewModel
        }

        companion object {
            fun create(parent: ViewGroup, onItemClicked: (position: Int) -> Unit) =
                FinishedGameViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_game_list_game_finished, parent, false), onItemClicked)
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