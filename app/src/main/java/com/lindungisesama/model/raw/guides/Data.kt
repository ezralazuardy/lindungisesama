package com.lindungisesama.model.raw.guides

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Data(
	val content: List<Content>? = null,
	val created_by: String? = null,
	val image: Image? = null,
	val title: String? = null
) : Parcelable