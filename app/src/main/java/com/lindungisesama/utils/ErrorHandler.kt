package com.lindungisesama.utils

import android.util.Log
import androidx.fragment.app.FragmentManager
import com.lindungisesama.model.response.RetrofitStatus
import com.lindungisesama.ui.error.ErrorHandlerFragment

object ErrorHandler {

	var isShown = false

	fun show(manager: FragmentManager, retrofitStatus: RetrofitStatus): Any =
		if (isShown) Log.w(this::javaClass.name, "Error handler dialog is already shown")
		else ErrorHandlerFragment(retrofitStatus)
			.show(manager, ErrorHandlerFragment::class.java.simpleName)
}