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
    fun showChosenWeapon(weapon: Weapon, playerSide: PlayerSide)
    fun showAndHideChosenWeapon(weapon: Weapon)
}

open class GameManager(protected val listener: GameListener): RPSGameManager {
    protected lateinit var playerOne: Player
    protected lateinit var playerTwo: Player
    protected lateinit var state: GameState

    override fun initGame() {
        state = GameState.IDLE
        playerOne = Player(PlayerSide.PLAYER_ONE)
        playerTwo = Player(PlayerSide.PLAYER_TWO)
        state = GameState.STARTED
    }

    open fun chooseWeaponAndShowResult(weapon: Weapon, playerSide: PlayerSide) {
        if (state == GameState.STARTED && playerSide == PlayerSide.PLAYER_ONE) {
            playerOne.chosenWeapon = weapon
            val randomNumber = Random.nextInt(0, until = Weapon.values().size)
            playerTwo.chosenWeapon = Weapon.values()[randomNumber]

            listener.onChoosingOrClearWeapon(playerOne.chosenWeapon, playerTwo.chosenWeapon,state)
            checkWinner(playerOne.chosenWeapon, playerTwo.chosenWeapon)
        }
    }

    protected fun checkWinner(playerOneWeapon: Weapon, playerTwoWeapon: Weapon) {
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

    protected fun setGameState(state: GameState) {
        this.state = state
    }
}

class MultiplayerGameManager(listener: GameListener): GameManager(listener) {
    override fun initGame() {
        super.initGame()
        setGameState(GameState.PLAYER_ONE_TURN)
    }

    override fun chooseWeaponAndShowResult(weapon: Weapon, playerSide: PlayerSide) {
        if (state == GameState.PLAYER_ONE_TURN && playerSide == PlayerSide.PLAYER_ONE) {
            playerOne.chosenWeapon = weapon
            listener.showAndHideChosenWeapon(weapon)
            listener.showChosenWeapon(playerOne.chosenWeapon, playerSide)
            setGameState(GameState.PLAYER_TWO_TURN)
        } else if (state == GameState.PLAYER_TWO_TURN && playerSide == PlayerSide.PLAYER_TWO) {
            playerTwo.chosenWeapon = weapon
            listener.showChosenWeapon(playerTwo.chosenWeapon, playerSide)
            listener.onChoosingOrClearWeapon(playerOne.chosenWeapon, playerTwo.chosenWeapon,state)
            checkWinner(playerOne.chosenWeapon, playerTwo.chosenWeapon)
        }
    }
}

