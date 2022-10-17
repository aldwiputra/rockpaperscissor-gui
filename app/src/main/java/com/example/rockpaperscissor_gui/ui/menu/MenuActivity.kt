package com.example.rockpaperscissor_gui.ui.menu


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rockpaperscissor_gui.R
import com.example.rockpaperscissor_gui.databinding.ActivityMenuBinding
import com.example.rockpaperscissor_gui.ui.game.GameActivity

class MenuActivity : AppCompatActivity() {
    private val binding : ActivityMenuBinding by lazy {
        ActivityMenuBinding.inflate(layoutInflater)
    }

    private val extraData: String? by lazy {
        intent.getStringExtra("NAME")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()
        setTextName()
        setMenuClickListeners()
    }

    private fun setMenuClickListeners() {
        binding.llVsPlayer.setOnClickListener {
            GameActivity.startActivity(this, true, extraData)
        }
        binding.llVsCpu.setOnClickListener {
            GameActivity.startActivity(this, false, extraData)
        }
    }

    private fun setTextName() {
        binding.tvVsPlayer.text = getString(R.string.text_menu_vs_player, extraData)
        binding.tvVsCpu.text = getString(R.string.text_menu_vs_cpu, extraData)
    }
}