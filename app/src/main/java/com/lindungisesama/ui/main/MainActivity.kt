package com.lindungisesama.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lindungisesama.R
import com.lindungisesama.model.response.RetrofitStatus
import com.lindungisesama.utils.ConnectionState
import com.lindungisesama.utils.ErrorHandler
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), AnkoLogger {

	private val mainViewModel: MainViewModel by viewModel()
	private val connectionState: ConnectionState by inject()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		nav_view.setupWithNavController(findNavController(R.id.nav_host_fragment))
		mainViewModel.prismicResponse.observe(this, Observer {
			if (it.status != RetrofitStatus.SUCCESS)
				ErrorHandler.show(supportFragmentManager, it.status)
		})
		connectionState.observe(this, Observer { connected ->
			if (connected)
				if (!mainViewModel.refUpdateIsExecuted && !mainViewModel.refUpdated)
					mainViewModel.getUpdatedRef()
				else
					ErrorHandler.show(supportFragmentManager, RetrofitStatus.NETWORK_ERROR)
		})
	}

	override fun onBackPressed() {
		MaterialAlertDialogBuilder(this)
			.setMessage(getString(R.string.dialog_confirm_exit_app))
			.setPositiveButton(getString(R.string.button_yes)) { _, _ ->
				finishAffinity()
			}.setNegativeButton(getString(R.string.button_cancel)) { dialog, _ ->
				dialog.dismiss()
			}.show()
	}
}
