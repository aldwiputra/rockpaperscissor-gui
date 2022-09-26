package com.example.rockpaperscissor_gui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rockpaperscissor_gui.databinding.ActivityMainBinding
import com.example.rockpaperscissor_gui.enum.GameState
import com.example.rockpaperscissor_gui.enum.PlayerSide
import com.example.rockpaperscissor_gui.enum.Weapon
import com.example.rockpaperscissor_gui.enum.Weapon.*
import com.example.rockpaperscissor_gui.manager.GameManager
import com.example.rockpaperscissor_gui.user.Player

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val gameManager: GameManager by lazy {
        GameManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        gameManager.initGame()
        setOnClickListener()
        supportActionBar?.hide()
    }

    fun onChoosingOrClearWeapon(playerOneWeapon: Weapon, playerTwoWeapon: Weapon, gameState: GameState) {
        val color = if (gameState == GameState.STARTED) {
            getDrawable(R.color.teal_opaque)
        } else {
            getDrawable(R.color.teal_transparent)
        }

        binding.apply {
            when (playerOneWeapon) {
                ROCK -> batuButtonLeft.background = color
                PAPER -> kertasButtonLeft.background = color
                SCISSOR -> guntingButtonLeft.background = color
            }
            when (playerTwoWeapon) {
                ROCK -> batuButtonRight.background = color
                PAPER -> kertasButtonRight.background = color
                SCISSOR -> guntingButtonRight.background = color
            }
        }
    }

    fun onGameFinished(winner: Player?) {
        when (winner?.playerSide) {
            PlayerSide.PLAYER_ONE -> {
                binding.tvResult.text = getString(R.string.text_you_won)
            }
            PlayerSide.PLAYER_TWO -> {
                binding.tvResult.text = getString(R.string.text_computer_won)
            }
            null -> {
                binding.tvResult.text = getString(R.string.text_draw)
            }
        }
        binding.tvResult.setBackgroundColor(getColor(R.color.orange))
        binding.tvResult.textSize = 24.toFloat()
    }

    private fun setOnClickListener() {
        binding.apply {
            batuButtonLeft.setOnClickListener {
                gameManager.chooseWeaponAndShowResult(ROCK)
            }
            kertasButtonLeft.setOnClickListener {
                gameManager.chooseWeaponAndShowResult(PAPER)
            }
            guntingButtonLeft.setOnClickListener {
                gameManager.chooseWeaponAndShowResult(SCISSOR)
            }
            ivRestart.setOnClickListener {
                gameManager.restartGame()
                tvResult.text = getString(R.string.versus)
                tvResult.setBackgroundColor(getColor(R.color.teal_transparent))
                tvResult.textSize = 45.toFloat()
            }
        }
    }
}