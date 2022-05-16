package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post

class InMemoryPostRepository : PostRepository {

    override val data = MutableLiveData(
        Post(
            id = 0L,
            author = "Person",
            content = "Any content",
            published = "10.05.2022"
        )
    )

    override fun like() {
        val currentPost = checkNotNull(data.value) {
            "Data value should not be null"
        }

        val likeCount = currentPost.like

        val likePost = currentPost.copy(
            likedByMe = !currentPost.likedByMe,
            like = if (!currentPost.likedByMe) likeCount + 1 else likeCount - 1
        )

        data.value = likePost
    }

    override fun share() {
        val currentPost = checkNotNull(data.value) {
            "Data value should not be null"
        }

        val sharePost = currentPost.share

        val clickShare = currentPost.copy(
            share = sharePost + 1,
            shareByMe = true
        )

        data.value = clickShare
    }

}