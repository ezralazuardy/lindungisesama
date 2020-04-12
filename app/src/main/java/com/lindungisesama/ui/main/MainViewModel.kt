package com.lindungisesama.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lindungisesama.model.raw.current_country_case.CurrentCountryCase
import com.lindungisesama.model.raw.faq.FAQ
import com.lindungisesama.model.raw.guides.Guides
import com.lindungisesama.model.raw.important_links.ImportantLinks
import com.lindungisesama.model.raw.mythbusters.Mythbusters
import com.lindungisesama.model.raw.preventions.Preventions
import com.lindungisesama.model.raw.prismic.Prismic
import com.lindungisesama.model.raw.references.References
import com.lindungisesama.model.response.*
import com.lindungisesama.repository.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(
	application: Application,
	private val prismicRepository: PrismicRepository,
	private val countryRepository: CountryRepository,
	private val guidesRepository: GuidesRepository,
	private val mythbustersRepository: MythbustersRepository,
	private val preventionsRepository: PreventionsRepository,
	private val faqRepository: FAQRepository,
	private val referencesRepository: ReferencesRepository,
	private val importantLinksRepository: ImportantLinksRepository
) : AndroidViewModel(application), AnkoLogger {

	private var updatedRef: String? = null
	private val _prismicResponse = MutableLiveData<PrismicResponse>()
	val prismicResponse: LiveData<PrismicResponse> = _prismicResponse
	var refUpdateIsExecuted = false
	var refUpdated = false

	fun getUpdatedRef() = viewModelScope.launch(Dispatchers.IO) {
		refUpdateIsExecuted = true
		prismicRepository.getUpdatedRef(object : Callback<Prismic> {
			override fun onFailure(call: Call<Prismic>, t: Throwable) {
				t.printStackTrace()
				error { t }
				_prismicResponse.postValue(PrismicResponse(RetrofitStatus.NETWORK_ERROR))
			}

			override fun onResponse(call: Call<Prismic>, response: Response<Prismic>) {
				if (response.isSuccessful) {
					if (response.body() != null) {
						response.body()?.let {
							updatedRef = it.refs?.singleOrNull { i -> i.isMasterRef == true }?.ref
							_prismicResponse.postValue(PrismicResponse(RetrofitStatus.SUCCESS, it))
							refUpdated = true
						}
					} else {
						error { response }
						_prismicResponse.postValue(PrismicResponse(RetrofitStatus.EMPTY))
					}
				} else {
					error { response }
					_prismicResponse.postValue(PrismicResponse(RetrofitStatus.FAILURE))
				}
			}
		})
	}

	private val _currentCaseResponse = MutableLiveData<CurrentCountryCaseResponse>()
	val currentCaseResponse: LiveData<CurrentCountryCaseResponse> = _currentCaseResponse
	var currentCaseIsExecuted = false
	var currentCaseLoaded = false

	fun getCurrentCase() = viewModelScope.launch(Dispatchers.IO) {
		currentCaseIsExecuted = true
		countryRepository.getCurrentCase(object : Callback<CurrentCountryCase> {
			override fun onFailure(call: Call<CurrentCountryCase>, t: Throwable) {
				t.printStackTrace()
				error { t }
				_currentCaseResponse.postValue(CurrentCountryCaseResponse(RetrofitStatus.NETWORK_ERROR))
			}

			override fun onResponse(
				call: Call<CurrentCountryCase>,
				response: Response<CurrentCountryCase>
			) {
				if (response.isSuccessful) {
					if (response.body() != null) {
						_currentCaseResponse.postValue(
							CurrentCountryCaseResponse(
								RetrofitStatus.SUCCESS,
								response.body()
							)
						)
						currentCaseLoaded = true
					} else {
						error { response }
						_currentCaseResponse.postValue(CurrentCountryCaseResponse(RetrofitStatus.EMPTY))
					}
				} else {
					error { response }
					_currentCaseResponse.postValue(CurrentCountryCaseResponse(RetrofitStatus.FAILURE))
				}
			}
		})
	}

	private val _guides = MutableLiveData<GuidesResponse>()
	val guides: LiveData<GuidesResponse> = _guides
	var guidesIsExecuted = false
	var guidesLoaded = false

	fun getGuides() = viewModelScope.launch(Dispatchers.IO) {
		updatedRef?.let { ref ->
			guidesIsExecuted = true
			guidesRepository.getGuides(ref, object : Callback<Guides> {
				override fun onFailure(call: Call<Guides>, t: Throwable) {
					t.printStackTrace()
					error { t }
					_guides.postValue(GuidesResponse(RetrofitStatus.NETWORK_ERROR))
				}

				override fun onResponse(call: Call<Guides>, response: Response<Guides>) {
					if (response.isSuccessful) {
						if (response.body()?.results != null) {
							_guides.postValue(
								GuidesResponse(
									RetrofitStatus.SUCCESS,
									response.body()?.results
								)
							)
							guidesLoaded = true
						} else {
							error { response }
							_guides.postValue(GuidesResponse(RetrofitStatus.EMPTY))
						}
					} else {
						error { response }
						_guides.postValue(GuidesResponse(RetrofitStatus.FAILURE))
					}
				}
			})
		}
	}

	private val _allGuides = MutableLiveData<GuidesResponse>()
	val allGuides: LiveData<GuidesResponse> = _allGuides
	var allGuidesIsExecuted = false
	var allGuidesLoaded = false

	fun getAllGuides() = viewModelScope.launch(Dispatchers.IO) {
		updatedRef?.let { ref ->
			allGuidesIsExecuted = true
			guidesRepository.getAllGuides(ref, object : Callback<Guides> {
				override fun onFailure(call: Call<Guides>, t: Throwable) {
					t.printStackTrace()
					error { t }
					_allGuides.postValue(GuidesResponse(RetrofitStatus.NETWORK_ERROR))
				}

				override fun onResponse(call: Call<Guides>, response: Response<Guides>) {
					if (response.isSuccessful) {
						if (response.body()?.results != null) {
							_allGuides.postValue(
								GuidesResponse(
									RetrofitStatus.SUCCESS,
									response.body()?.results
								)
							)
							allGuidesLoaded = true
						} else {
							error { response }
							_allGuides.postValue(GuidesResponse(RetrofitStatus.EMPTY))
						}
					} else {
						error { response }
						_allGuides.postValue(GuidesResponse(RetrofitStatus.FAILURE))
					}
				}
			})
		}
	}

	private val _mythbusters = MutableLiveData<MythbustersResponse>()
	val mythbusters: LiveData<MythbustersResponse> = _mythbusters
	var mythbustersIsExecuted = false
	var mythbustersLoaded = false

	fun getMythbusters() = viewModelScope.launch(Dispatchers.IO) {
		updatedRef?.let { ref ->
			mythbustersIsExecuted = true
			mythbustersRepository.getMythbusters(ref, object : Callback<Mythbusters> {
				override fun onFailure(call: Call<Mythbusters>, t: Throwable) {
					t.printStackTrace()
					error { t }
					_mythbusters.postValue(MythbustersResponse(RetrofitStatus.NETWORK_ERROR))
				}

				override fun onResponse(call: Call<Mythbusters>, response: Response<Mythbusters>) {
					if (response.isSuccessful) {
						if (response.body()?.results != null) {
							_mythbusters.postValue(
								MythbustersResponse(
									RetrofitStatus.SUCCESS,
									response.body()?.results
								)
							)
							mythbustersLoaded = true
						} else {
							error { response }
							_mythbusters.postValue(MythbustersResponse(RetrofitStatus.EMPTY))
						}
					} else {
						error { response }
						_mythbusters.postValue(MythbustersResponse(RetrofitStatus.FAILURE))
					}
				}
			})
		}
	}

	private val _allMythbusters = MutableLiveData<MythbustersResponse>()
	val allMythbusters: LiveData<MythbustersResponse> = _allMythbusters
	var allMythbustersIsExecuted = false
	var allMythbustersLoaded = false

	fun getAllMythbusters() = viewModelScope.launch(Dispatchers.IO) {
		updatedRef?.let { ref ->
			allMythbustersIsExecuted = true
			mythbustersRepository.getAllMythbusters(ref, object : Callback<Mythbusters> {
				override fun onFailure(call: Call<Mythbusters>, t: Throwable) {
					t.printStackTrace()
					error { t }
					_allMythbusters.postValue(MythbustersResponse(RetrofitStatus.NETWORK_ERROR))
				}

				override fun onResponse(call: Call<Mythbusters>, response: Response<Mythbusters>) {
					if (response.isSuccessful) {
						if (response.body()?.results != null) {
							_allMythbusters.postValue(
								MythbustersResponse(
									RetrofitStatus.SUCCESS,
									response.body()?.results
								)
							)
							allMythbustersLoaded = true
						} else {
							error { response }
							_allMythbusters.postValue(MythbustersResponse(RetrofitStatus.EMPTY))
						}
					} else {
						error { response }
						_allMythbusters.postValue(MythbustersResponse(RetrofitStatus.FAILURE))
					}
				}
			})
		}
	}

	private val _preventions = MutableLiveData<PreventionsResponse>()
	val preventions: LiveData<PreventionsResponse> = _preventions
	var preventionsIsExecuted = false
	var preventionsLoaded = false

	fun getPreventions() = viewModelScope.launch(Dispatchers.IO) {
		updatedRef?.let { ref ->
			preventionsIsExecuted = true
			preventionsRepository.getPreventions(ref, object : Callback<Preventions> {
				override fun onFailure(call: Call<Preventions>, t: Throwable) {
					t.printStackTrace()
					error { t }
					_preventions.postValue(PreventionsResponse(RetrofitStatus.NETWORK_ERROR))
				}

				override fun onResponse(call: Call<Preventions>, response: Response<Preventions>) {
					if (response.isSuccessful) {
						if (response.body()?.results != null) {
							_preventions.postValue(
								PreventionsResponse(
									RetrofitStatus.SUCCESS,
									response.body()?.results
								)
							)
							preventionsLoaded = true
						} else {
							error { response }
							_preventions.postValue(PreventionsResponse(RetrofitStatus.EMPTY))
						}
					} else {
						error { response }
						_preventions.postValue(PreventionsResponse(RetrofitStatus.FAILURE))
					}
				}
			})
		}
	}

	private val _faqResponse = MutableLiveData<FAQResponse>()
	val faqResponse: LiveData<FAQResponse> = _faqResponse
	var faqIsExecuted = false
	var faqLoaded = false

	fun getFAQ() = viewModelScope.launch(Dispatchers.IO) {
		updatedRef?.let { ref ->
			faqIsExecuted = true
			faqRepository.getFAQ(ref, object : Callback<FAQ> {
				override fun onFailure(call: Call<FAQ>, t: Throwable) {
					t.printStackTrace()
					error { t }
					_faqResponse.postValue(FAQResponse(RetrofitStatus.NETWORK_ERROR))
				}

				override fun onResponse(
					call: Call<FAQ>,
					response: Response<FAQ>
				) {
					if (response.isSuccessful) {
						if (response.body()?.results != null) {
							_faqResponse.postValue(
								FAQResponse(
									RetrofitStatus.SUCCESS,
									response.body()?.results
								)
							)
							faqLoaded = true
						} else {
							error { response }
							_faqResponse.postValue(FAQResponse(RetrofitStatus.EMPTY))
						}
					} else {
						error { response }
						_faqResponse.postValue(FAQResponse(RetrofitStatus.FAILURE))
					}
				}
			})
		}
	}

	private val _references = MutableLiveData<ReferencesResponse>()
	val references: LiveData<ReferencesResponse> = _references
	var referencesIsExecuted = false
	var referencesLoaded = false

	fun getReferences() = viewModelScope.launch(Dispatchers.IO) {
		updatedRef?.let { ref ->
			referencesIsExecuted = true
			referencesRepository.getReferences(ref, object : Callback<References> {
				override fun onFailure(call: Call<References>, t: Throwable) {
					t.printStackTrace()
					error { t }
					_references.postValue(ReferencesResponse(RetrofitStatus.NETWORK_ERROR))
				}

				override fun onResponse(call: Call<References>, response: Response<References>) {
					if (response.isSuccessful) {
						if (response.body()?.results != null) {
							_references.postValue(
								ReferencesResponse(
									RetrofitStatus.SUCCESS,
									response.body()?.results
								)
							)
							referencesLoaded = true
						} else {
							error { response }
							_references.postValue(ReferencesResponse(RetrofitStatus.EMPTY))
						}
					} else {
						error { response }
						_references.postValue(ReferencesResponse(RetrofitStatus.FAILURE))
					}
				}
			})
		}
	}

	private val _allReferences = MutableLiveData<ReferencesResponse>()
	val allReferences: LiveData<ReferencesResponse> = _allReferences
	var allReferencesIsExecuted = false
	var allReferencesLoaded = false

	fun getAllReferences() = viewModelScope.launch(Dispatchers.IO) {
		updatedRef?.let { ref ->
			allReferencesIsExecuted = true
			referencesRepository.getAllReferences(ref, object : Callback<References> {
				override fun onFailure(call: Call<References>, t: Throwable) {
					t.printStackTrace()
					error { t }
					_allReferences.postValue(ReferencesResponse(RetrofitStatus.NETWORK_ERROR))
				}

				override fun onResponse(call: Call<References>, response: Response<References>) {
					if (response.isSuccessful) {
						if (response.body()?.results != null) {
							_allReferences.postValue(
								ReferencesResponse(
									RetrofitStatus.SUCCESS,
									response.body()?.results
								)
							)
							allReferencesLoaded = true
						} else {
							error { response }
							_allReferences.postValue(ReferencesResponse(RetrofitStatus.EMPTY))
						}
					} else {
						error { response }
						_allReferences.postValue(ReferencesResponse(RetrofitStatus.FAILURE))
					}
				}
			})
		}
	}

	private val _importantLinks = MutableLiveData<ImportantLinksResponse>()
	val importantLinks: LiveData<ImportantLinksResponse> = _importantLinks
	var importantLinksIsExecuted = false
	var importantLinksLoaded = false

	fun getImportantLinks() = viewModelScope.launch(Dispatchers.IO) {
		updatedRef?.let { ref ->
			importantLinksIsExecuted = true
			importantLinksRepository.getImportantLinks(ref, object : Callback<ImportantLinks> {
				override fun onFailure(call: Call<ImportantLinks>, t: Throwable) {
					t.printStackTrace()
					error { t }
					_importantLinks.postValue(ImportantLinksResponse(RetrofitStatus.NETWORK_ERROR))
				}

				override fun onResponse(
					call: Call<ImportantLinks>,
					response: Response<ImportantLinks>
				) {
					if (response.isSuccessful) {
						if (response.body()?.results != null) {
							_importantLinks.postValue(
								ImportantLinksResponse(
									RetrofitStatus.SUCCESS,
									response.body()?.results
								)
							)
							importantLinksLoaded = true
						} else {
							error { response }
							_importantLinks.postValue(ImportantLinksResponse(RetrofitStatus.EMPTY))
						}
					} else {
						error { response }
						_importantLinks.postValue(ImportantLinksResponse(RetrofitStatus.FAILURE))
					}
				}
			})
		}
	}

	private val _allImportantLinks = MutableLiveData<ImportantLinksResponse>()
	val allImportantLinks: LiveData<ImportantLinksResponse> = _allImportantLinks
	var allImportantLinksIsExecuted = false
	var allImportantLinksLoaded = false

	fun getAllImportantLinks() = viewModelScope.launch(Dispatchers.IO) {
		updatedRef?.let { ref ->
			allImportantLinksIsExecuted = true
			importantLinksRepository.getAllImportantLinks(ref, object : Callback<ImportantLinks> {
				override fun onFailure(call: Call<ImportantLinks>, t: Throwable) {
					t.printStackTrace()
					error { t }
					_allImportantLinks.postValue(ImportantLinksResponse(RetrofitStatus.NETWORK_ERROR))
				}

				override fun onResponse(
					call: Call<ImportantLinks>,
					response: Response<ImportantLinks>
				) {
					if (response.isSuccessful) {
						if (response.body()?.results != null) {
							_allImportantLinks.postValue(
								ImportantLinksResponse(
									RetrofitStatus.SUCCESS,
									response.body()?.results
								)
							)
							allImportantLinksLoaded = true
						} else {
							error { response }
							_allImportantLinks.postValue(ImportantLinksResponse(RetrofitStatus.EMPTY))
						}
					} else {
						error { response }
						_allImportantLinks.postValue(ImportantLinksResponse(RetrofitStatus.FAILURE))
					}
				}
			})
		}
	}
}