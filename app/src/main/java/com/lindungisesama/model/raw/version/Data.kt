package com.lindungisesama.model.raw.version

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Data(
	val build_version: String? = null,
	val download_link: String? = null,
	val published_date: String? = null,
	val version_code: String? = null
) : Parcelable