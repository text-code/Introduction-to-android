package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post

class InMemoryPostRepository : PostRepository {

    private var nextId = GENERATED_POSTS_AMOUNT.toLong()

    private val posts
        get() = checkNotNull(data.value) {
            "Data value should not be null"
        }

    override val data = MutableLiveData(
        List(GENERATED_POSTS_AMOUNT) { index ->
            Post(
                id = index + 1L,
                author = "Person",
                content = "Any content $index",
                published = "10.05.2022"
            )
        }
    )

    override fun like(postId: Long) {
        data.value = posts.map {
            if (it.id != postId) it
            else it.copy(
                likedByMe = !it.likedByMe,
                like = if (!it.likedByMe) it.like + 1 else it.like - 1
            )
        }
    }

//    override fun like(postId: Long, postLike: Int) {
//        data.value = posts.map {
//            if (it.id != postId) it
//            else it.copy(
//                likedByMe = !it.likedByMe,
//                like = if (!it.likedByMe) postLike + 1 else postLike - 1
//            )
//        }
//    }

    override fun share(postId: Long) {
        data.value = posts.map {
            if (it.id != postId) it
            else
                it.copy(
                    share = it.share + 1,
                    shareByMe = true
                )
        }
    }

    override fun delete(postId: Long) {
        println("delete")
        data.value = posts.filter { it.id != postId }
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
    }

    // region save
    private fun insert(post: Post) {
        data.value = listOf(
            post.copy(id = ++nextId)
        ) + posts
    }

    private fun update(post: Post) {
        data.value = posts.map {
            if (it.id == post.id) post else it
        }
    }
    //endregion save

    private companion object {
        const val GENERATED_POSTS_AMOUNT = 10
    }
}