package com.example.rockpaperscissor_gui.ui.customdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.rockpaperscissor_gui.databinding.FragmentCustomDialogBinding


class CustomDialog : DialogFragment() {
    private lateinit var binding: FragmentCustomDialogBinding

    private var listener: OnMenuSelectedListener? = null

    fun setOnMenuSelectedListener(listener: OnMenuSelectedListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCustomDialogBinding.inflate(inflater, container, false)
        binding.tvWinnerAnnouncement.text = arguments?.getString("RESULT")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonPlayAgain.setOnClickListener {
            listener?.onPlayAgainClicked(this)
        }
        binding.buttonBackToMenu.setOnClickListener {
            listener?.onBackToMenuClicked(this)
        }
    }
}

interface OnMenuSelectedListener {
    fun onPlayAgainClicked(dialog : DialogFragment)
    fun onBackToMenuClicked(dialog : DialogFragment)
}