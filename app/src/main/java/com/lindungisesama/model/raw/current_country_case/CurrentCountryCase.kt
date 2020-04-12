package com.lindungisesama.model.raw.current_country_case

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CurrentCountryCase(
	val active: Int? = null,
	val cases: Int? = null,
	val casesPerOneMillion: Int? = null,
	val country: String? = null,
	val countryInfo: CountryInfo? = null,
	val critical: Int? = null,
	val deaths: Int? = null,
	val deathsPerOneMillion: Double? = null,
	val recovered: Int? = null,
	val todayCases: Int? = null,
	val todayDeaths: Int? = null,
	val updated: Long? = null
) : Parcelable