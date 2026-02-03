package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityNewPostBinding

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val text = intent.getStringExtra(Intent.EXTRA_TEXT)
        binding.edit.setText(text)
        binding.edit.requestFocus()

        binding.ok.setOnClickListener {
            val content = binding.edit.text.toString()
            if (content.isBlank()) {
                setResult(Activity.RESULT_CANCELED)
            } else {
                val intent = Intent().putExtra(Intent.EXTRA_TEXT, content)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }
}