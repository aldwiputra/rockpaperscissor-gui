package com.example.rockpaperscissor_gui.manager

import com.example.rockpaperscissor_gui.ui.game.GameActivity
import com.example.rockpaperscissor_gui.enum.GameState
import com.example.rockpaperscissor_gui.enum.PlayerSide
import com.example.rockpaperscissor_gui.enum.Weapon
import com.example.rockpaperscissor_gui.user.Player
import kotlin.random.Random

class GameManager(private val listener: GameListener) {
    private lateinit var playerOne: Player
    private lateinit var playerTwo: Player
    private lateinit var gameState: GameState

    fun initGame() {
        gameState = GameState.IDLE
        playerOne = Player(PlayerSide.PLAYER_ONE)
        playerTwo = Player(PlayerSide.PLAYER_TWO)
        gameState = GameState.STARTED
    }

    fun chooseWeaponAndShowResult(weapon: Weapon) {
        if (gameState !== GameState.FINISHED && gameState !== GameState.IDLE) {
            playerOne.chosenWeapon = weapon
            val randomNumber = Random.nextInt(0, until = Weapon.values().size)
            playerTwo.chosenWeapon = Weapon.values()[randomNumber]

            listener.onChoosingOrClearWeapon(playerOne.chosenWeapon, playerTwo.chosenWeapon,gameState)
            checkWinner(playerOne.chosenWeapon, playerTwo.chosenWeapon)
        }
    }

    private fun checkWinner(playerOneWeapon: Weapon, playerTwoWeapon: Weapon) {
        val playerOneWeaponNum = transformWeaponIntoNumber(playerOneWeapon)
        val playerTwoWeaponNum = transformWeaponIntoNumber(playerTwoWeapon)

        val winner = if ((playerTwoWeaponNum + 1) % 3 == playerOneWeaponNum) {
            playerOne
        } else if ((playerOneWeaponNum + 1) % 3 == playerTwoWeaponNum) {
            playerTwo
        } else {
            null
        }

        gameState = GameState.FINISHED
        listener.onGameFinished(winner)
    }

    private fun transformWeaponIntoNumber (weapon: Weapon): Int {
        return weapon.ordinal
    }

    fun restartGame() {
        if (gameState == GameState.FINISHED) {
            listener.onChoosingOrClearWeapon(
                playerOne.chosenWeapon,
                playerTwo.chosenWeapon,
                gameState
            )
            initGame()
        }
    }

    private fun setGameState(state: GameState) {
        gameState = state
    }
}

interface GameListener {
    fun onChoosingOrClearWeapon(playerOneWeapon: Weapon, playerTwoWeapon: Weapon, gameState: GameState)
    fun onGameFinished(winner: Player?)
}