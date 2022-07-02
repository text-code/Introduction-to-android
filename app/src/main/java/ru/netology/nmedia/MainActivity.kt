package ru.netology.nmedia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.util.hideKeyboard
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

        binding.saveButton.setOnClickListener {
            with(binding.contentEditText) {
                val content = text.toString()
                viewModel.onSaveButtonClicked(content)

                clearFocus()
                hideKeyboard()
            }
        }

        binding.cancelButton.setOnClickListener {
            with(binding.contentEditText) {
                viewModel.onCancelButtonClicked()

                clearFocus()
                hideKeyboard()
            }
        }

        viewModel.currentPost.observe(this) { currentPost ->
            binding.contentEditText.setText(currentPost?.content)
            binding.groupEdit.isVisible = currentPost != null
            binding.editMessageText.text = currentPost?.content
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
    }
}