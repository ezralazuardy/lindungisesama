package com.lindungisesama.utils

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.os.Build
import androidx.lifecycle.LiveData

@Suppress("DEPRECATION")
class ConnectionState(val context: Context) : LiveData<Boolean>() {

	private lateinit var connectivityManagerCallback: ConnectivityManager.NetworkCallback
	private var connectivityManager: ConnectivityManager =
		context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
	private val networkReceiver = object : BroadcastReceiver() {
		override fun onReceive(context: Context, intent: Intent) {
			updateConnection()
		}
	}

	override fun onActive() {
		super.onActive()
		updateConnection()
		when {
			Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ->
				connectivityManager.registerDefaultNetworkCallback(getConnectivityManagerCallback())
			Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ->
				lollipopNetworkAvailableRequest()
			Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ->
				context.registerReceiver(
					networkReceiver,
					IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
				)
		}
	}

	override fun onInactive() {
		super.onInactive()
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
			connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
		else
			context.unregisterReceiver(networkReceiver)
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private fun lollipopNetworkAvailableRequest() {
		val builder = NetworkRequest.Builder()
			.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
			.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
		connectivityManager.registerNetworkCallback(
			builder.build(),
			getConnectivityManagerCallback()
		)
	}

	private fun getConnectivityManagerCallback(): ConnectivityManager.NetworkCallback {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			connectivityManagerCallback = object : ConnectivityManager.NetworkCallback() {
				override fun onAvailable(network: Network?) {
					postValue(true)
				}

				override fun onLost(network: Network?) {
					postValue(false)
				}
			}
			return connectivityManagerCallback
		} else {
			throw IllegalAccessError()
		}
	}

	private fun updateConnection() {
		val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
		activeNetwork?.let { postValue(it.isConnected) }
	}
}