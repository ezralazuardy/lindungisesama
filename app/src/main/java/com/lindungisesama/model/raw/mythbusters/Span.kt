package com.lindungisesama.model.raw.mythbusters

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Span(
	val end: Int? = null,
	val start: Int? = null,
	val type: String? = null,
	val data: SpanData? = null
) : Parcelable