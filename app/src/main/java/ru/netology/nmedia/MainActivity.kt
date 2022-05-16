package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.DrawableRes
import ru.netology.nmedia.databinding.PostListItemBinding
import ru.netology.nmedia.dto.Post
import kotlin.math.floor

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = PostListItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 0L,
            author = "Person",
            content = "Any content",
            published = "10.05.2022"
        )

//        ------------------------------------
//        binding.root.setOnClickListener {
//            println("click root")
//        }
//
//        binding.like.setOnClickListener {
//            println("click like")
//        }
//        ------------------------------------

        binding.render(post)
        binding.like.setOnClickListener {
            post.likedByMe = !post.likedByMe
            if (post.likedByMe) post.like++ else post.like--
            binding.likeText.text = shareCount(post.like)
            binding.like.setImageResource(getLikeIconResId(post.likedByMe))
        }

        binding.share.setOnClickListener {
            post.share += 1
            binding.shareText.text = shareCount(post.share)
            binding.share.setImageResource(R.drawable.ic_shared_24dp)
        }
    }

    private fun PostListItemBinding.render(post: Post) {
        authorName.text = post.author
        contentPost.text = post.content
        publicationDate.text = post.published
        likeText.text = post.like.toString()
        shareText.text = post.share.toString()
//        like.setImageResource(getLikeIconResId(post.likedByMe))    //Для чего эта строка ???
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
}