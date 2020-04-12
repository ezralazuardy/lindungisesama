package com.lindungisesama.model.raw.prismic

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Experiments(
	val draft: List<String>? = null,
	val running: List<String>? = null
) : Parcelable