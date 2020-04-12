package com.lindungisesama.model.raw.mythbusters

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Content(
	val alt: String? = null,
	val copyright: String? = null,
	val dimensions: Dimensions? = null,
	val spans: List<Span>? = null,
	val text: String? = null,
	val type: String? = null,
	val url: String? = null
) : Parcelable