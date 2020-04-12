package com.lindungisesama.repository

import com.lindungisesama.dao.FaqDAO
import com.lindungisesama.model.raw.faq.FAQ
import retrofit2.Callback

class FAQRepository(private val faqDAO: FaqDAO) {

	fun getFAQ(ref: String, callback: Callback<FAQ>) {
		faqDAO.getFAQ(ref = ref).enqueue(callback)
	}
}