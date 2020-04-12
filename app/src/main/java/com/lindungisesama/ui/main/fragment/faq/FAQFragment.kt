package com.lindungisesama.ui.main.fragment.faq

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lindungisesama.R
import com.lindungisesama.model.raw.faq.Result
import com.lindungisesama.model.response.RetrofitStatus
import com.lindungisesama.ui.main.MainViewModel
import com.lindungisesama.ui.main.fragment.faq.adapter.FAQAdapter
import com.lindungisesama.ui.main.fragment.faq.adapter.FAQItemClickListener
import com.lindungisesama.utils.ErrorHandler
import kotlinx.android.synthetic.main.layout_faq.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class FAQFragment : Fragment(),
	FAQItemClickListener {

	private val mainViewModel: MainViewModel by sharedViewModel()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? = inflater.inflate(R.layout.fragment_faq, container, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		retainInstance = true
		showLoading()
		context?.let { context ->
			val faqAdapter =
				FAQAdapter(
					context,
					this@FAQFragment
				)
			recycler_view.apply {
				adapter = faqAdapter
				layoutManager = LinearLayoutManager(context)
				setHasFixedSize(true)
			}
			mainViewModel.faqResponse.observe(viewLifecycleOwner, Observer {
				when (it.status) {
					RetrofitStatus.SUCCESS -> it.data?.let { data ->
						faqAdapter.setData(data)
						hideLoading()
					}
					else -> activity?.let { activity ->
						ErrorHandler.show(activity.supportFragmentManager, it.status)
					}
				}
			})
			mainViewModel.prismicResponse.observe(viewLifecycleOwner, Observer {
				if (it.status != RetrofitStatus.SUCCESS) activity?.let { activity ->
					ErrorHandler.show(activity.supportFragmentManager, it.status)
				} else if (!mainViewModel.faqIsExecuted
					&& !mainViewModel.faqLoaded
				) mainViewModel.getFAQ()
			})
		}
	}

	private fun showLoading() {
		recycler_view.visibility = View.GONE
		loading.visibility = View.VISIBLE
	}

	private fun hideLoading() {
		loading.visibility = View.GONE
		recycler_view.visibility = View.VISIBLE
	}

	override fun onItemClick(result: Result) {
		activity?.let {
			FAQDetailFragment(result)
				.show(it.supportFragmentManager, FAQDetailFragment::class.java.simpleName)
		}
	}
}
