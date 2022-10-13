package com.example.rockpaperscissor_gui.ui.game

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import com.example.rockpaperscissor_gui.R
import com.example.rockpaperscissor_gui.databinding.ActivityGameBinding
import com.example.rockpaperscissor_gui.enum.GameState
import com.example.rockpaperscissor_gui.enum.PlayerSide
import com.example.rockpaperscissor_gui.enum.Weapon
import com.example.rockpaperscissor_gui.enum.Weapon.*
import com.example.rockpaperscissor_gui.manager.GameListener
import com.example.rockpaperscissor_gui.manager.GameManager
import com.example.rockpaperscissor_gui.user.Player

class GameActivity : AppCompatActivity(), GameListener {
    private val binding: ActivityGameBinding by lazy {
        ActivityGameBinding.inflate(layoutInflater)
    }

    private val gameManager: GameManager by lazy {
        GameManager(this)
    }

    private val isMultiplayerModeOn: Boolean by lazy {
        intent.getBooleanExtra(EXTRAS_MULTIPLAYER_MODE, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        gameManager.initGame()
        setOnClickListener()
        supportActionBar?.hide()
    }

    override fun onChoosingOrClearWeapon(playerOneWeapon: Weapon, playerTwoWeapon: Weapon, gameState: GameState) {
        val color = if (gameState == GameState.STARTED) {
            AppCompatResources.getDrawable(this, R.drawable.img_rounded_background)
        } else {
            AppCompatResources.getDrawable(this, R.drawable.img_rounded_background_transparent)
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

    override fun onGameFinished(winner: Player?) {
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

    companion object {
        private const val EXTRAS_MULTIPLAYER_MODE = "EXTRAS_MULTIPLAYER_MODE"

        fun startActivity(context: Context, isMultiplayerModeOn: Boolean) {
            context.startActivity(Intent(context, GameActivity::class.java).apply {
                putExtra(EXTRAS_MULTIPLAYER_MODE, isMultiplayerModeOn)
            })
        }
    }
}