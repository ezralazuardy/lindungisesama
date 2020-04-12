package com.lindungisesama.model.raw.guides

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Span(
	val end: Int? = null,
	val start: Int? = null,
	val type: String? = null,
	val data: SpanData? = null
) : Parcelable