package com.lindungisesama.model.raw.mythbusters

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SpanData(
	val link_type: String? = null,
	val url: String? = null
) : Parcelable