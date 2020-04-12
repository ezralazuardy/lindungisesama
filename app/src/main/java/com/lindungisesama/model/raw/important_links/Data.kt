package com.lindungisesama.model.raw.important_links

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Data(
	val link: String? = null,
	val name: String? = null
) : Parcelable