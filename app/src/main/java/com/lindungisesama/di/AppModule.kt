package com.lindungisesama.di

import com.lindungisesama.database.RemoteDatabase
import com.lindungisesama.repository.*
import com.lindungisesama.ui.main.MainViewModel
import com.lindungisesama.utils.ConnectionState
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
	// DAO
	single { RemoteDatabase.prismicDao() }
	single { RemoteDatabase.countryDao() }
	single { RemoteDatabase.guidesDao() }
	single { RemoteDatabase.mythbustersDao() }
	single { RemoteDatabase.preventionsDao() }
	single { RemoteDatabase.faqDao() }
	single { RemoteDatabase.referencesDao() }
	single { RemoteDatabase.importantLinksDao() }
	single { RemoteDatabase.versionDao() }

	// Repository
	single { PrismicRepository(get()) }
	single { CountryRepository(get()) }
	single { GuidesRepository(get()) }
	single { MythbustersRepository(get()) }
	single { PreventionsRepository(get()) }
	single { FAQRepository(get()) }
	single { ReferencesRepository(get()) }
	single { ImportantLinksRepository(get()) }
	single { VersionRepository(get()) }

	// Utils
	single { ConnectionState(get()) }

	// ViewModel
	viewModel {
		MainViewModel(
			get(),
			get(),
			get(),
			get(),
			get(),
			get(),
			get(),
			get(),
			get()
		)
	}
}