package tk.atna.shortlyapp.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import tk.atna.shortlyapp.R
import tk.atna.shortlyapp.databinding.MainAcBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { MainAcBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.container, MainFragment())
            }
        }
    }
}