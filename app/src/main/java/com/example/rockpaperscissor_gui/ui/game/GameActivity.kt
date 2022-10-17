package com.example.rockpaperscissor_gui.ui.game

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import com.example.rockpaperscissor_gui.R
import com.example.rockpaperscissor_gui.databinding.ActivityGameBinding
import com.example.rockpaperscissor_gui.enum.GameState
import com.example.rockpaperscissor_gui.enum.PlayerSide
import com.example.rockpaperscissor_gui.enum.Weapon
import com.example.rockpaperscissor_gui.enum.Weapon.*
import com.example.rockpaperscissor_gui.manager.GameListener
import com.example.rockpaperscissor_gui.manager.GameManager
import com.example.rockpaperscissor_gui.manager.MultiplayerGameManager
import com.example.rockpaperscissor_gui.ui.customdialog.CustomDialog
import com.example.rockpaperscissor_gui.ui.customdialog.OnMenuSelectedListener
import com.example.rockpaperscissor_gui.ui.menu.MenuActivity
import com.example.rockpaperscissor_gui.user.Player

class GameActivity : AppCompatActivity(), GameListener {
    private val binding: ActivityGameBinding by lazy {
        ActivityGameBinding.inflate(layoutInflater)
    }

    private val gameManager: GameManager by lazy {
        if (isMultiplayerModeOn) {
            MultiplayerGameManager(this)
        } else {
            GameManager(this)
        }
    }

    private val nameExtra: String? by lazy {
        intent.getStringExtra("NAME")
    }

    private val isMultiplayerModeOn: Boolean by lazy {
        intent.getBooleanExtra(EXTRAS_MULTIPLAYER_MODE, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()
        setNameTitle()
        gameManager.initGame()
        setOnClickListener()
    }

    override fun onChoosingOrClearWeapon(
        playerOneWeapon: Weapon,
        playerTwoWeapon: Weapon,
        gameState: GameState
    ) {
        val color = if (gameState == GameState.STARTED || gameState == GameState.PLAYER_TWO_TURN) {
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
        showDialog(winner)
//        when (winner?.playerSide) {
//            PlayerSide.PLAYER_ONE -> {
//                binding.tvResult.text = getString(R.string.text_you_won)
//            }
//            PlayerSide.PLAYER_TWO -> {
//                binding.tvResult.text = getString(R.string.text_computer_won)
//            }
//            null -> {
//                binding.tvResult.text = getString(R.string.text_draw)
//            }
//        }
//        binding.tvResult.setBackgroundColor(getColor(R.color.orange))
//        binding.tvResult.textSize = 24.toFloat()
    }

    override fun showChosenWeapon(weapon: Weapon, playerSide: PlayerSide) {
        val player =
            if (playerSide == PlayerSide.PLAYER_ONE) nameExtra else getString(R.string.text_player_2)
        val chosenWeapon = when (weapon) {
            ROCK -> "Batu"
            PAPER -> "Kertas"
            SCISSOR -> "Gunting"
        }
        Toast.makeText(
            this,
            getString(R.string.text_toast_chosen_weapon, player, chosenWeapon),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun setOnClickListener() {
        binding.apply {
            batuButtonLeft.setOnClickListener {
                gameManager.chooseWeaponAndShowResult(ROCK, PlayerSide.PLAYER_ONE)
            }
            kertasButtonLeft.setOnClickListener {
                gameManager.chooseWeaponAndShowResult(PAPER, PlayerSide.PLAYER_ONE)
            }
            guntingButtonLeft.setOnClickListener {
                gameManager.chooseWeaponAndShowResult(SCISSOR, PlayerSide.PLAYER_ONE)
            }
            ivRestart.setOnClickListener {
                gameManager.restartGame()
            }
            ivCloseButton.setOnClickListener {
                Intent(this@GameActivity, MenuActivity::class.java).apply {
                    this.putExtra("NAME", nameExtra)
                    startActivity(this)
                }
            }


            if (isMultiplayerModeOn) {
                batuButtonRight.setOnClickListener {
                    gameManager.chooseWeaponAndShowResult(ROCK, PlayerSide.PLAYER_TWO)
                }
                kertasButtonRight.setOnClickListener {
                    gameManager.chooseWeaponAndShowResult(PAPER, PlayerSide.PLAYER_TWO)

                }
                guntingButtonRight.setOnClickListener {
                    gameManager.chooseWeaponAndShowResult(SCISSOR, PlayerSide.PLAYER_TWO)

                }
            }
        }
    }

    private fun setNameTitle() {
        binding.tvTitlePlayerLeft.text = intent.getStringExtra("NAME")
        binding.tvTitlePlayerRight.text =
            if (isMultiplayerModeOn) getString(R.string.text_player_2) else "CPU"
    }

    private fun getResultString(winner: Player?): String {
        val playerTwoString = if (isMultiplayerModeOn) getString(R.string.text_player_2) else "CPU"

        return when (winner?.playerSide) {
            PlayerSide.PLAYER_ONE -> "$nameExtra\nMENANG"
            PlayerSide.PLAYER_TWO -> "$playerTwoString\nMENANG"
            null -> "SERI!"
        }
    }

    private fun showDialog(winner: Player?) {
        CustomDialog().apply {
            setOnMenuSelectedListener(object : OnMenuSelectedListener {
                override fun onPlayAgainClicked(dialog: DialogFragment) {
                    gameManager.restartGame()
                    dialog.dismiss()
                }

                override fun onBackToMenuClicked(dialog: DialogFragment) {
                    dialog.dismiss()
                    Intent(this@GameActivity, MenuActivity::class.java).also {
                        it.putExtra("NAME", nameExtra)
                        startActivity(it)
                    }
                }
            })
            isCancelable = false
            arguments = Bundle().apply {
                this.putString("RESULT", getResultString(winner))

            }
        }.show(supportFragmentManager, null)
    }

    override fun showAndHideChosenWeapon(weapon: Weapon) {
        val timer: CountDownTimer

        val backgroundOpaque = AppCompatResources.getDrawable(this, R.drawable.img_rounded_background)
        val backgroundTransparent = AppCompatResources.getDrawable(this, R.drawable.img_rounded_background_transparent)

        binding.apply {
            when (weapon) {
                ROCK -> batuButtonLeft.background = backgroundOpaque
                PAPER -> kertasButtonLeft.background = backgroundOpaque
                SCISSOR -> guntingButtonLeft.background = backgroundOpaque
            }
        }

        timer = object : CountDownTimer(500, 1000) {
            override fun onTick(p0: Long) {}
            override fun onFinish() {
                binding.apply {
                    when (weapon) {
                        ROCK -> batuButtonLeft.background = backgroundTransparent
                        PAPER -> kertasButtonLeft.background = backgroundTransparent
                        SCISSOR -> guntingButtonLeft.background = backgroundTransparent
                    }
                }
            }
        }
        timer.start()
    }

    companion object {
        private const val EXTRAS_MULTIPLAYER_MODE = "EXTRAS_MULTIPLAYER_MODE"

        fun startActivity(context: Context, isMultiplayerModeOn: Boolean, extraData: String?) {
            context.startActivity(Intent(context, GameActivity::class.java).apply {
                putExtra(EXTRAS_MULTIPLAYER_MODE, isMultiplayerModeOn)
                putExtra("NAME", extraData)
            })
        }
    }
}