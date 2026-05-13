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
import ru.netology.nmedia.databinding.FragmentSignUpBinding
import ru.netology.nmedia.viewmodel.AuthViewModel

class SignUpFragment : Fragment() {
    private val viewModel: AuthViewModel by viewModels()
    private var binding: FragmentSignUpBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val b = FragmentSignUpBinding.inflate(inflater, container, false)
        binding = b
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = binding ?: return

        binding.registerButton.setOnClickListener {
            val login = binding.loginField.text.toString().trim()
            val pass = binding.passwordField.text.toString()
            val name = binding.nameField.text.toString().trim()
            if (login.isNotBlank() && pass.isNotBlank() && name.isNotBlank()) {
                viewModel.register(login, pass, name)
            }
        }

        binding.signInButton.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        viewModel.data.observe(viewLifecycleOwner) { state ->
            binding.registerButton.isEnabled = !state.loading
            binding.signInButton.isEnabled = !state.loading
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