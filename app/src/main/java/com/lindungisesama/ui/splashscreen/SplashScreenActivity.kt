package com.lindungisesama.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lindungisesama.ui.main.MainActivity

class SplashScreenActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		startActivity(Intent(this, MainActivity::class.java))
	}
}