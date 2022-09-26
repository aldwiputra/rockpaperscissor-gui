package com.example.rockpaperscissor_gui.user

import com.example.rockpaperscissor_gui.enum.PlayerSide
import com.example.rockpaperscissor_gui.enum.Weapon

class Player(val playerSide: PlayerSide) {
    lateinit var chosenWeapon: Weapon
}