package com.lindungisesama.utils

import com.lindungisesama.config.AppConfig.IO_TIMEOUT
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Retrofit {

	fun getClient(baseUrl: String): Retrofit =
		Retrofit.Builder()
			.baseUrl(baseUrl)
			.client(
				OkHttpClient.Builder()
					.connectTimeout(IO_TIMEOUT, TimeUnit.SECONDS)
					.readTimeout(IO_TIMEOUT, TimeUnit.SECONDS)
					.writeTimeout(IO_TIMEOUT, TimeUnit.SECONDS)
					.build()
			)
			.addConverterFactory(GsonConverterFactory.create())
			.build()
}