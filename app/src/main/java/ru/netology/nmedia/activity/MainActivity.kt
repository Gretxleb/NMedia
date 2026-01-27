// Внутри onCreate
binding.cancel_edit.setOnClickListener {
    viewModel.cancelEdit() // Эту функцию нужно добавить в ViewModel
    binding.content.setText("")
    binding.content.clearFocus()
    AndroidUtils.hideKeyboard(it)
    binding.edit_group.visibility = View.GONE
}

viewModel.edited.observe(this) { post ->
    if (post.id == 0L) return@observe
    binding.edit_group.visibility = View.VISIBLE
    binding.edit_content_preview.text = post.content
    with(binding.content) {
        requestFocus()
        setText(post.content)
    }
}