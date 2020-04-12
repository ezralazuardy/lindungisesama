package com.lindungisesama.model.raw.current_country_case

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountryInfo(
	val _id: Int? = null,
	val flag: String? = null,
	val iso2: String? = null,
	val iso3: String? = null,
	val lat: Int? = null,
	val long: Int? = null
) : Parcelable