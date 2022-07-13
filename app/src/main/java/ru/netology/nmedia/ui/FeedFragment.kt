package ru.netology.nmedia.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FeedFragmentBinding
import ru.netology.nmedia.viewModel.PostViewModel

class FeedFragment : Fragment() {

    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.shareEvent.observe(this) { post ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"

                putExtra(Intent.EXTRA_TEXT, post)
            }

            val shareIntent = Intent.createChooser(intent, getString(R.string.share))
            startActivity(shareIntent)

            setFragmentResultListener(
                requestKey = PostContentFragment.REQUEST_KEY
            ) {requestKey, bundle ->
            if (requestKey != PostContentFragment.REQUEST_KEY) return@setFragmentResultListener
                val newPostContent = bundle.getString(
                    PostContentFragment.RESULT_KEY
                ) ?: return@setFragmentResultListener
                viewModel.onSaveButtonClicked(newPostContent)
            }

            viewModel.navigateToPostContentScreenEvent.observe(viewLifecycleOwner) { initialContent ->
                val direction = FeedFragmentDirections.toPostContentFragment(initialContent)
                findNavController().navigate(direction)
            }

//            binding.addPost.setOnClickListener {
//                activityLauncher.launch(toString())
//            }
//
//            viewModel.currentPost.observe(viewLifecycleOwner) { currentPost ->
//                if (currentPost != null) {
//                    activityLauncher.launch(currentPost.content)
//                }
//            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FeedFragmentBinding.inflate(layoutInflater, container, false).also { binding ->
        val adapter = PostsAdapter(viewModel)
        binding.postsRecyclerView.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }
        binding.addPost.setOnClickListener {
            viewModel.addClicked()
        }
    }.root

    companion object {
        const val TAG = "FeedFragment"
    }
}