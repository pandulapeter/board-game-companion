package com.rbtgames.boardgame.feature.playerDetail.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.data.model.Player
import com.rbtgames.boardgame.databinding.ItemPlayerDetailColorBinding

class ColorAdapter(private val onItemClicked: (color: Player.Color) -> Unit) :
    ListAdapter<ColorViewModel, ColorAdapter.ViewHolder>(object : DiffUtil.ItemCallback<ColorViewModel>() {

        override fun areItemsTheSame(oldItem: ColorViewModel, newItem: ColorViewModel) = oldItem.color.colorResourceId == newItem.color.colorResourceId

        override fun areContentsTheSame(oldItem: ColorViewModel, newItem: ColorViewModel) = oldItem == newItem
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder.create(parent) { position -> onItemClicked(getItem(position).color) }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class ViewHolder(private val binding: ItemPlayerDetailColorBinding, onItemClicked: (position: Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                adapterPosition.let { adapterPosition ->
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onItemClicked(adapterPosition)
                    }
                }
            }
        }

        fun bind(colorViewModel: ColorViewModel) {
            binding.viewModel = colorViewModel
        }

        companion object {
            fun create(parent: ViewGroup, onItemClicked: (position: Int) -> Unit) =
                ViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_player_detail_color,
                        parent,
                        false
                    ), onItemClicked
                )
        }
    }
}