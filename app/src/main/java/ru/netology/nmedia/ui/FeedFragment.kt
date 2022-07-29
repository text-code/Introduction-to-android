package ru.netology.nmedia.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewModel.PostViewModel

class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

//    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.shareEvent.observe(this) { postContent ->
            context?.let { shareEvent(it, postContent) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentFeedBinding.inflate(layoutInflater, container, false).also { binding ->

        val adapter = PostsAdapter(viewModel)

        binding.postsRecyclerView.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }

        binding.addPost.setOnClickListener {
            viewModel.onAddClicked()
        }

        setFragmentResultListener(
            requestKey = PostContentFragment.REQUEST_KEY
        ) { requestKey, bundle ->
            if (requestKey != PostContentFragment.REQUEST_KEY) return@setFragmentResultListener
            val newPostContent = bundle.getString(
                PostContentFragment.RESULT_KEY
            ) ?: return@setFragmentResultListener
            viewModel.onSaveButtonClicked(newPostContent)
        }

        viewModel.contentPost.observe(viewLifecycleOwner) { initialContent ->
//            val direction = FeedFragmentDirections.toPostContentFragment(initialContent)
            findNavController().navigate(
                R.id.toPostContentFragment,
                Bundle().apply { textArg = initialContent }
            )
        }

        viewModel.selectedPost.observe(viewLifecycleOwner) {
            findNavController().navigate(
                R.id.action_feedFragment_to_postDisplayFragment,
                Bundle().apply { idArg = it.id }
            )
        }
    }.root

    companion object {
        private const val ID_POST_KEY = "ID_POST"

        var Bundle.textArg: String? by StringArg

        var Bundle.idArg: Long
            set(value) = putLong(ID_POST_KEY, value)
            get() = getLong(ID_POST_KEY)

        fun shareEvent(context: Context, postContent: String?) {
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"

                putExtra(Intent.EXTRA_TEXT, postContent)
            }

            val shareIntent = Intent.createChooser(intent, "Share")
            startActivity(context, shareIntent, null)
        }
    }
}