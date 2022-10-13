package com.example.rockpaperscissor_gui.manager

import com.example.rockpaperscissor_gui.enum.GameState
import com.example.rockpaperscissor_gui.enum.PlayerSide
import com.example.rockpaperscissor_gui.enum.Weapon
import com.example.rockpaperscissor_gui.user.Player
import kotlin.random.Random

interface RPSGameManager {
    fun initGame()
    fun restartGame()
}

interface GameListener {
    fun onChoosingOrClearWeapon(playerOneWeapon: Weapon, playerTwoWeapon: Weapon, gameState: GameState)
    fun onGameFinished(winner: Player?)
}

class GameManager(private val listener: GameListener): RPSGameManager {
    protected lateinit var playerOne: Player
    protected lateinit var playerTwo: Player
    protected lateinit var state: GameState

    override fun initGame() {
        state = GameState.IDLE
        playerOne = Player(PlayerSide.PLAYER_ONE)
        playerTwo = Player(PlayerSide.PLAYER_TWO)
        state = GameState.STARTED
    }

    fun chooseWeaponAndShowResult(weapon: Weapon) {
        if (state !== GameState.FINISHED && state !== GameState.IDLE) {
            playerOne.chosenWeapon = weapon
            val randomNumber = Random.nextInt(0, until = Weapon.values().size)
            playerTwo.chosenWeapon = Weapon.values()[randomNumber]

            listener.onChoosingOrClearWeapon(playerOne.chosenWeapon, playerTwo.chosenWeapon,state)
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

        state = GameState.FINISHED
        listener.onGameFinished(winner)
    }

    private fun transformWeaponIntoNumber (weapon: Weapon): Int {
        return weapon.ordinal
    }

    override fun restartGame() {
        if (state == GameState.FINISHED) {
            listener.onChoosingOrClearWeapon(
                playerOne.chosenWeapon,
                playerTwo.chosenWeapon,
                state
            )
            initGame()
        }
    }

    private fun setGameState(state: GameState) {
        this.state = state
    }
}

