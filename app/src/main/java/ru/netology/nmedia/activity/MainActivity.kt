package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.auth.AppAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppAuth.initAppAuth(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.auth, menu)
        val isAuthenticated = AppAuth.getInstance().authState.value != null
        menu.findItem(R.id.sign_in).isVisible = !isAuthenticated
        menu.findItem(R.id.sign_out).isVisible = isAuthenticated
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sign_in -> {
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_feedFragment_to_signInFragment)
                true
            }
            R.id.sign_out -> {
                AppAuth.getInstance().removeAuth()
                invalidateOptionsMenu()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
