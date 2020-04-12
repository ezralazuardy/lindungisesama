package com.lindungisesama.database

import com.lindungisesama.config.AppConfig.COVID_API_ENDPOINT
import com.lindungisesama.config.AppConfig.PRISMIC_ENDPOINT
import com.lindungisesama.dao.*
import com.lindungisesama.utils.Retrofit

object RemoteDatabase {

	fun prismicDao(): PrismicDAO =
		Retrofit.getClient(PRISMIC_ENDPOINT).create(PrismicDAO::class.java)

	fun countryDao(): CountryDAO =
		Retrofit.getClient(COVID_API_ENDPOINT).create(CountryDAO::class.java)

	fun guidesDao(): GuidesDAO = Retrofit.getClient(PRISMIC_ENDPOINT).create(GuidesDAO::class.java)

	fun mythbustersDao(): MythbustersDAO =
		Retrofit.getClient(PRISMIC_ENDPOINT).create(MythbustersDAO::class.java)

	fun preventionsDao(): PreventionsDAO =
		Retrofit.getClient(PRISMIC_ENDPOINT).create(PreventionsDAO::class.java)

	fun faqDao(): FaqDAO = Retrofit.getClient(PRISMIC_ENDPOINT).create(FaqDAO::class.java)

	fun referencesDao(): ReferencesDAO =
		Retrofit.getClient(PRISMIC_ENDPOINT).create(ReferencesDAO::class.java)

	fun importantLinksDao(): ImportantLinksDAO =
		Retrofit.getClient(PRISMIC_ENDPOINT).create(ImportantLinksDAO::class.java)

	fun versionDao(): VersionDAO =
		Retrofit.getClient(PRISMIC_ENDPOINT).create(VersionDAO::class.java)
}