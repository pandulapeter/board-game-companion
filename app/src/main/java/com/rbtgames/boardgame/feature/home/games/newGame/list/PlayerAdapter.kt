package com.rbtgames.boardgame.feature.home.games.newGame.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.databinding.ItemNewGameHintBinding
import com.rbtgames.boardgame.databinding.ItemNewGamePlayerBinding

class PlayerAdapter(private val onPlayerClicked: (player: PlayerViewModel) -> Unit) :
    ListAdapter<NewGameListItem, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<NewGameListItem>() {

        override fun areItemsTheSame(oldItem: NewGameListItem, newItem: NewGameListItem) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: NewGameListItem, newItem: NewGameListItem) = oldItem == newItem
    }) {

    var dragHandleTouchListener: ((position: Int) -> Unit)? = null
    private val exception by lazy { IllegalArgumentException("Invalid view type") }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is PlayerViewModel -> R.layout.item_new_game_player
        is HintViewModel -> R.layout.item_new_game_hint
        else -> throw exception
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        R.layout.item_new_game_player -> PlayerViewHolder.create(parent) { position -> onPlayerClicked(getItem(position) as PlayerViewModel) }.apply {
            setDragHandleTouchListener(dragHandleTouchListener)
        }
        R.layout.item_new_game_hint -> HintViewHolder.create(parent)
        else -> throw exception
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
        is PlayerViewHolder -> holder.bind(getItem(position) as PlayerViewModel)
        is HintViewHolder -> holder.bind(getItem(position) as HintViewModel)
        else -> throw exception
    }

    public override fun getItem(position: Int): NewGameListItem = super.getItem(position)

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

        @SuppressLint("ClickableViewAccessibility")
        fun setDragHandleTouchListener(itemTouchListener: ((position: Int) -> Unit)?) {
            if (itemTouchListener != null) {
                binding.dragHandle.setOnTouchListener { _, event ->
                    if (event.actionMasked == MotionEvent.ACTION_DOWN && adapterPosition != RecyclerView.NO_POSITION) {
                        itemTouchListener(adapterPosition)
                    }
                    false
                }
            }
        }

        fun bind(playerViewModel: PlayerViewModel) {
            binding.viewModel = playerViewModel
        }

        companion object {
            fun create(parent: ViewGroup, onItemClicked: (position: Int) -> Unit) =
                PlayerViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_new_game_player,
                        parent,
                        false
                    ), onItemClicked
                )
        }
    }

    class HintViewHolder(private val binding: ItemNewGameHintBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: HintViewModel) {
            binding.viewModel = viewModel
        }

        companion object {
            fun create(parent: ViewGroup) = HintViewHolder(
                DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_new_game_hint, parent, false)
            )
        }
    }
}