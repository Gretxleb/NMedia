package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentSignUpBinding
import ru.netology.nmedia.viewmodel.AuthViewModel

class SignUpFragment : Fragment() {
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.registerButton.setOnClickListener {
            val login = binding.loginField.text.toString()
            val pass = binding.passwordField.text.toString()
            val name = binding.nameField.text.toString()
            viewModel.register(login, pass, name)
        }

        binding.signInButton.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        viewModel.data.observe(viewLifecycleOwner) { state ->
            if (state.success) {
                findNavController().navigateUp()
            }
        }

        return binding.root
    }
}