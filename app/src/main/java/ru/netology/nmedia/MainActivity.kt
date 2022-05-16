package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import ru.netology.nmedia.databinding.PostListItemBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewModel.PostViewModel
import kotlin.math.floor

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = PostListItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.data.observe(this) { post ->
            binding.render(post)
        }

        binding.like.setOnClickListener {
            viewModel.onLikeClicked()
        }

        binding.share.setOnClickListener {
            viewModel.onShareClicked()
        }
    }

    private fun PostListItemBinding.render(post: Post) {
        authorName.text = post.author
        contentPost.text = post.content
        publicationDate.text = post.published
        likeText.text = shareCount(post.like)
        shareText.text = shareCount(post.share)
        like.setImageResource(getLikeIconResId(post.likedByMe))
        share.setImageResource(getShareIconResId(post.shareByMe))
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