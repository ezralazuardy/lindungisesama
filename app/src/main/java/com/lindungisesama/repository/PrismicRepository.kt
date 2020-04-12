package com.lindungisesama.repository

import com.lindungisesama.dao.PrismicDAO
import com.lindungisesama.model.raw.prismic.Prismic
import retrofit2.Callback

class PrismicRepository(private val prismicDAO: PrismicDAO) {

	fun getUpdatedRef(callback: Callback<Prismic>) =
		prismicDAO.getPrismic().enqueue(callback)
}