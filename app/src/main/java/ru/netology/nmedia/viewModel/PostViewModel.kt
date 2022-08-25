package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.PostRepositoryImpl
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.SingleLiveEvent

class PostViewModel(
    application: Application
) : AndroidViewModel(application),
    PostInteractionListener {

    private val repository: PostRepository = PostRepositoryImpl(
        dao = AppDb.getInstance(
            context = application
        ).postDao
    )

    val data by repository::data

    val shareEvent = SingleLiveEvent<String>()

    val contentPost = SingleLiveEvent<String>()

    val selectedPost = SingleLiveEvent<Post>()

    val youTubeEvent = SingleLiveEvent<String>()

    private val currentPost = MutableLiveData<Post?>(null)

    fun onSaveButtonClicked(content: String) {
        if (content.isBlank()) return

        val post = currentPost.value?.copy(
            content = content
        ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "Me",
            content = content,
            published = "Today",
            video = "https://www.youtube.com/watch?v=OWX9kov3PX0"
        )
        repository.save(post)
        currentPost.value = null
    }

    fun onAddClicked() {
        contentPost.call()
    }

    // region PostInteractionListener

    override fun onLikeClicked(post: Post) =
        repository.like(post.id)

    override fun onShareClicked(post: Post) {
        repository.share(post.id)
        shareEvent.value = post.content
    }

    override fun onRemoveClicked(post: Post) =
        repository.delete(post.id)

    override fun onEditClicked(post: Post) {
        currentPost.value = post
        contentPost.value = post.content
    }

    override fun onContentClicked(post: Post) {
        selectedPost.value = post
    }

    override fun onVideoClicked(post: Post) {
        youTubeEvent.value = post.video
    }

    // endregion PostInteractionListener
}