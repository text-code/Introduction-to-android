package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.databinding.PostContentFragmentBinding

class PostContentFragment : Fragment() {

    private val args by navArgs<PostContentFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PostContentFragmentBinding.inflate(
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

    private fun onOkButtonClicked(binding: PostContentFragmentBinding) {
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
//
//    object ResultContract : ActivityResultContract<String?, String?>() {
//        override fun createIntent(context: Context, input: String?) =
//            Intent(context, PostContentFragment::class.java)
//
//        override fun parseResult(resultCode: Int, intent: Intent?): String? {
//            if (resultCode != Activity.RESULT_OK) return null
//            intent ?: return null
//
//            return intent.getStringExtra(POST_CONTENT_EXTRA_KEY)
//        }
//    }
}