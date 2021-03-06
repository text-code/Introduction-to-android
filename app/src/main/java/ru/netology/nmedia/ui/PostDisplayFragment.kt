package ru.netology.nmedia.ui

import android.content.Intent
import android.net.Uri
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
import ru.netology.nmedia.ui.FeedFragment.Companion.shareEvent
import ru.netology.nmedia.ui.FeedFragment.Companion.textArg
import ru.netology.nmedia.viewModel.PostViewModel

class PostDisplayFragment : Fragment() {

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
            val post = posts.firstOrNull { it.id == arguments?.idArg }
            if (post != null) {
                viewHolder.bind(post)
            } else {
                findNavController().navigateUp()
            }
        }

        viewModel.contentPost.observe(viewLifecycleOwner) { text ->
            findNavController().navigate(
                R.id.action_postDisplayFragment_to_postContentFragment,
                Bundle().apply { textArg = text }
            )
        }

        viewModel.youTubeEvent.observe(viewLifecycleOwner) { url ->
            val urlEvent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(urlEvent)
        }

        viewModel.selectedPost.observe(viewLifecycleOwner) {
        }


//        findNavController().navigate(R.id.action_feedFragment_to_postDisplayFragment)
//        binding.postLayout.contentPost.text = "qsdfuihcmwoief"

    }.root
}