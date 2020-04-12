package com.lindungisesama.model.raw.preventions

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Data(
	val content: List<Content>? = null,
	val image: Image? = null,
	val source: String? = null,
	val title: String? = null
) : Parcelable