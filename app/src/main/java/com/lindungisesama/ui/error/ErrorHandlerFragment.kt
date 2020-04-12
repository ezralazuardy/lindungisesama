package com.lindungisesama.ui.error

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lindungisesama.R
import com.lindungisesama.model.response.RetrofitStatus
import com.lindungisesama.utils.ConnectionState
import com.lindungisesama.utils.ErrorHandler
import kotlinx.android.synthetic.main.layout_error_handler.*
import org.koin.android.ext.android.inject

class ErrorHandlerFragment(
	private val retrofitStatus: RetrofitStatus
) : BottomSheetDialogFragment() {

	private val connectionState: ConnectionState by inject()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		ErrorHandler.isShown = true
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? = inflater.inflate(R.layout.layout_error_handler, container, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		when (retrofitStatus) {
			RetrofitStatus.NETWORK_ERROR -> {
				connectionState.observe(viewLifecycleOwner, Observer { connected ->
					if (connected) {
						ErrorHandler.isShown = false
						this.dismiss()
					}
				})
				setNetworkErrorView()
			}
			else -> setUnknownErrorView()
		}
	}

	override fun onDismiss(dialog: DialogInterface) {
		super.onDismiss(dialog)
		ErrorHandler.isShown = false
	}

	private fun setNetworkErrorView() {
		txt_title.text = getString(R.string.title_error_handler_network_error)
		txt_description.text = getString(R.string.description_error_handler_network_error)
		button_open_network_settings.apply {
			visibility = View.VISIBLE
			setOnClickListener {
				startActivity(Intent(android.provider.Settings.ACTION_SETTINGS))
			}
		}
		button_report_error.visibility = View.GONE
	}

	private fun setUnknownErrorView() {
		txt_title.text = getString(R.string.title_error_handler_unknown_error)
		txt_description.text = getString(R.string.description_error_handler_unknown_error)
		button_open_network_settings.visibility = View.GONE
		button_report_error.apply {
			visibility = View.VISIBLE
			setOnClickListener {
				startActivity(
					Intent.createChooser(
						Intent(
							Intent.ACTION_SENDTO,
							Uri.fromParts("mailto", "ezralucio@gmail.com", null)
						).putExtra(
							Intent.EXTRA_SUBJECT,
							"[LINDUNGI SESAMA] Laporan Error & Bug Aplikasi Android"
						).putExtra(
							Intent.EXTRA_TEXT, "Deskripsi Masalah: "
						),
						"Send Email"
					)
				)
			}
		}
	}
}