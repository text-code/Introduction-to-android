package ru.netology.nmedia.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.ui.FeedFragment.Companion.idArg
import ru.netology.nmedia.ui.FeedFragment.Companion.textArg
import ru.netology.nmedia.viewModel.PostViewModel

class PostDisplayFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

//    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel.shareEvent.observe(this) { postContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"

                putExtra(Intent.EXTRA_TEXT, postContent)
            }

            val shareIntent = Intent.createChooser(intent, getString(R.string.share))
            startActivity(shareIntent)
            findNavController().navigateUp()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentPostBinding.inflate(
        layoutInflater,
        container,
        false
    ).also { binding ->
        super.onCreate(savedInstanceState)

        val viewHolder = PostsAdapter.ViewHolder(viewModel, binding.postLayout)


//        val post = viewModel.data.value?.first { it.id == arguments?.idArg }
//        if (post != null) viewHolder.bind(post)


//        viewModel.data.value?.first { it.id == arguments?.idArg }?.let { viewHolder.bind(it) }


        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.first { it.id == arguments?.idArg }
            viewHolder.bind(post)
        }

        viewModel.contentPost.observe(viewLifecycleOwner) { text ->
            findNavController().navigate(
                R.id.action_postDisplayFragment_to_postContentFragment,
                Bundle().apply { textArg = text }
            )
        }


//        findNavController().navigate(R.id.action_feedFragment_to_postDisplayFragment)
//        binding.postLayout.contentPost.text = "qsdfuihcmwoief"

    }.root
}