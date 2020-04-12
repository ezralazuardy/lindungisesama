package com.lindungisesama.model.raw.preventions

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Preventions(
	val license: String? = null,
	val next_page: String? = null,
	val page: Int? = null,
	val prev_page: String? = null,
	val results: List<Result>? = null,
	val results_per_page: Int? = null,
	val results_size: Int? = null,
	val total_pages: Int? = null,
	val total_results_size: Int? = null,
	val version: String? = null
) : Parcelable