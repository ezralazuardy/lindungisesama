package com.lindungisesama.model.raw.guides

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SpanData(
	val link_type: String? = null,
	val url: String? = null
) : Parcelable