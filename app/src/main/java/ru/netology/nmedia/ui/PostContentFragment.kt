package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.databinding.FragmentPostContentBinding

class PostContentFragment : Fragment() {

    private val args by navArgs<PostContentFragmentArgs>()

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
        binding.edit.setText(args.initialContent)
        binding.edit.requestFocus()
        binding.ok.setOnClickListener {
            onOkButtonClicked(binding)
        }
    }.root

    private fun onOkButtonClicked(binding: FragmentPostContentBinding) {
        val postContent = binding.edit.text
        if (!postContent.isNullOrBlank()) {
            val resultBundle = Bundle(1)
            resultBundle.putString(RESULT_KEY, postContent.toString())
            setFragmentResult(REQUEST_KEY, resultBundle)
        }
        findNavController().popBackStack()
    }

    companion object {
        const val REQUEST_KEY = "requestKey"
        const val RESULT_KEY = "postNewContent"
    }
}