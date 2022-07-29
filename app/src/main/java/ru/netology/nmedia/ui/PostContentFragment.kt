package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentPostContentBinding
import ru.netology.nmedia.ui.FeedFragment.Companion.textArg
import ru.netology.nmedia.viewModel.PostViewModel

class PostContentFragment : Fragment() {

//    private val args by navArgs<PostContentFragmentArgs>()
private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentPostContentBinding.inflate(
        layoutInflater,
        container,
        false
    ).also { binding ->
        super.onCreate(savedInstanceState)

        arguments?.textArg?.let(binding.edit::setText)

//        binding.edit.setText(args.initialContent)
        binding.edit.requestFocus()

        binding.ok.setOnClickListener {
//            onOkButtonClicked(binding)
            if (!binding.edit.text.isNullOrBlank()) {
                val content = binding.edit.text.toString()
                viewModel.onSaveButtonClicked(content)
            }
            findNavController().navigateUp()
        }

    }.root
/*
    private fun onOkButtonClicked(binding: FragmentPostContentBinding) {
        val postContent = binding.edit.text
        if (!postContent.isNullOrBlank()) {
            val resultBundle = Bundle(1)
            resultBundle.putString(RESULT_KEY, postContent.toString())
            setFragmentResult(REQUEST_KEY, resultBundle)
        }
        findNavController().popBackStack()
    }
*/


    companion object {
        const val REQUEST_KEY = "requestKey"
        const val RESULT_KEY = "postNewContent"
    }
}