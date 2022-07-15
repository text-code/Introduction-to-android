package ru.netology.nmedia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.activity.NewPostActivity
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostsAdapter(viewModel)
        binding.postsRecyclerView.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        viewModel.youTubeEvent.observe(this) { url ->
            val urlEvent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(urlEvent)
        }

        viewModel.shareEvent.observe(this) { post ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"

                putExtra(Intent.EXTRA_TEXT, post.content)
            }

            val shareIntent = Intent.createChooser(intent, getString(R.string.share))
            startActivity(shareIntent)
        }

//        val activityLauncher = registerForActivityResult(
//            NewPostActivity.ResultContract
//        ) { postContent: String? ->
//            postContent?.let(viewModel::onSaveButtonClicked)
//        }

        val activityLauncher = registerForActivityResult(
            NewPostActivity.ResultContract
        ) { postContent: String? ->
            postContent?.let(viewModel::onSaveButtonClicked) ?: run {
                viewModel.currentPost.value = null
            }
        }

        binding.addPost.setOnClickListener {
            activityLauncher.launch("")
        }

        viewModel.currentPost.observe(this) { currentPost ->
            if (currentPost != null) {
                activityLauncher.launch(currentPost.content)
            }
        }
    }
}