package com.example.rockpaperscissor_gui.ui.onboarding

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.rockpaperscissor_gui.R
import com.example.rockpaperscissor_gui.ui.onboarding.formname.FormNameFragment
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroFragment

class OnboardingActivity : AppIntro2() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setupSliderFragment()
    }

    private fun setupSliderFragment() {
        isSkipButtonEnabled = false

        setIndicatorColor(
            selectedIndicatorColor = getColor(R.color.teal_700),
            unselectedIndicatorColor = getColor(com.google.android.material.R.color.material_grey_600)
        )

        addSlide(AppIntroFragment.createInstance(
            description = getString(R.string.text_splash_desc_1),
            imageDrawable = R.drawable.ic_landing_page1,
            descriptionTypefaceFontRes = R.font.hellounicorn_font,
            descriptionColorRes = if (isDarkTheme(this@OnboardingActivity)) R.color.white else R.color.black
        ))

        addSlide(AppIntroFragment.createInstance(
            description = getString(R.string.text_splash_desc_2),
            descriptionTypefaceFontRes = R.font.hellounicorn_font,
            imageDrawable = R.drawable.ic_landing_page2,
            descriptionColorRes = if (isDarkTheme(this@OnboardingActivity)) R.color.white else R.color.black

        ))

        addSlide(FormNameFragment())
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        if (currentFragment is OnFinishNavigateListener) {
            currentFragment.onFinishNavigate()
        }
    }

    private fun isDarkTheme(activity: Activity): Boolean {
        return activity.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
}

interface OnFinishNavigateListener {
    fun onFinishNavigate()
}