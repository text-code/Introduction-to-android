package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post

class InMemoryPostRepository : PostRepository {

    override val data = MutableLiveData(
        List(10) { index ->
            Post(
                id = index + 1L,
                author = "Person",
                content = "Any content $index",
                published = "10.05.2022"
            )
        }
    )

    private val posts
        get() = checkNotNull(data.value) {
            "Data value should not be null"
        }

    override fun like(postId: Long, postLike: Int) {
        data.value = posts.map {
            if (it.id != postId) it
            else it.copy(
                likedByMe = !it.likedByMe,
                like = if (!it.likedByMe) postLike + 1 else postLike - 1
            )
        }
    }

    override fun share(postId: Long, postShare: Int) {
        data.value = posts.map {
            if (it.id != postId) it
            else
                it.copy(
                    share = postShare + 1,
                    shareByMe = true
                )
        }
    }
}