package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentSignInBinding
import ru.netology.nmedia.viewmodel.AuthViewModel

class SignInFragment : Fragment() {
    private val viewModel: AuthViewModel by viewModels()
    private var binding: FragmentSignInBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val b = FragmentSignInBinding.inflate(inflater, container, false)
        binding = b
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = binding ?: return

        binding.loginButton.setOnClickListener {
            val login = binding.loginField.text.toString().trim()
            val pass = binding.passwordField.text.toString()
            if (login.isNotBlank() && pass.isNotBlank()) {
                viewModel.login(login, pass)
            }
        }

        binding.signUpButton.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        viewModel.data.observe(viewLifecycleOwner) { state ->
            binding.loginButton.isEnabled = !state.loading
            binding.signUpButton.isEnabled = !state.loading
            binding.progress.isVisible = state.loading
            binding.errorText.isVisible = state.error

            if (state.success) {
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}