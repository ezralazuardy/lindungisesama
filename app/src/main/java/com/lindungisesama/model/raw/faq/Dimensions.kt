package com.lindungisesama.model.raw.faq

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Dimensions(
	val height: Int? = null,
	val width: Int? = null
) : Parcelable