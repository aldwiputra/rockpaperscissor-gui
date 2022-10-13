package com.example.rockpaperscissor_gui.ui.onboarding.formname

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.rockpaperscissor_gui.databinding.FragmentFormNameBinding
import com.example.rockpaperscissor_gui.ui.menu.MenuActivity
import com.example.rockpaperscissor_gui.ui.onboarding.OnFinishNavigateListener


class FormNameFragment : Fragment(), OnFinishNavigateListener {
    private lateinit var binding: FragmentFormNameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFormNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onFinishNavigate() {
        val name = binding.etName.text.toString().trim().lowercase().replaceFirstChar { it.uppercase() }
        if(name.isEmpty()){
            Toast.makeText(requireContext(), "Please input your name !", Toast.LENGTH_SHORT).show()
        }else{
            navigateToMenu(name)
        }
    }

    private fun navigateToMenu(name: String) {
        Intent(activity, MenuActivity::class.java).also {
            it.putExtra("NAME", name)
            startActivity(it)
        }
    }
}