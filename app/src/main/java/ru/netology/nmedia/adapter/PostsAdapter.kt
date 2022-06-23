package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.databinding.PostListItemBinding
import ru.netology.nmedia.dto.Post
import kotlin.math.floor

internal class PostsAdapter(
    private val interactionListener: PostInteractionListener
) : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(interactionListener, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        listener: PostInteractionListener,
        private val binding: PostListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.menu).apply {
                inflate(R.menu.options_post)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.remove -> {
                            listener.onRemoveClicked(post)
                            true
                        }
                        R.id.edit -> {
                            listener.onEditClicked(post)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        init {
            binding.like.setOnClickListener { listener.onLikeClicked(post) }
            binding.share.setOnClickListener { listener.onShareClicked(post) }
        }

        fun bind(post: Post) {
            this.post = post

            with(binding) {
                authorName.text = post.author
                contentPost.text = post.content
                publicationDate.text = post.published
                likeText.text = shareCount(post.like)
                shareText.text = shareCount(post.share)
                like.setImageResource(getLikeIconResId(post.likedByMe))
                share.setImageResource(getShareIconResId(post.shareByMe))
                menu.setOnClickListener { popupMenu.show() }
            }
        }

        private fun shareCount(value: Int) =
            when {
                (value in 1_000..1_099) -> "1K"
                (value in 1_100..9_999) -> (floor((value / 100).toDouble()) / 10).toString() + "K"
                (value in 10_000..999_999) -> (value / 1_000).toString() + "K"
                (value >= 1_000_000) -> (value / 1_000_000).toString() + "M"
                else -> value.toString()
            }

        @DrawableRes
        private fun getLikeIconResId(liked: Boolean) =
            if (liked) R.drawable.ic_like_24dp
            else R.drawable.ic_like_border_24dp

        @DrawableRes
        private fun getShareIconResId(shared: Boolean) =
            if (shared) R.drawable.ic_shared_24dp
            else R.drawable.ic_share_24dp
    }

    private object DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem == newItem

    }
}