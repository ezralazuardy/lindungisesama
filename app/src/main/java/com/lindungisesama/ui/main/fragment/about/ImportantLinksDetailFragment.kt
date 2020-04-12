package com.lindungisesama.ui.main.fragment.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lindungisesama.R
import com.lindungisesama.model.raw.important_links.Result
import com.lindungisesama.model.response.RetrofitStatus
import com.lindungisesama.ui.main.MainViewModel
import com.lindungisesama.ui.main.fragment.about.adapter.ImportantLinksDetailAdapter
import com.lindungisesama.ui.main.fragment.about.adapter.ImportantLinksItemClickListener
import com.lindungisesama.utils.ErrorHandler
import com.thefinestartist.finestwebview.FinestWebView
import kotlinx.android.synthetic.main.layout_important_links_2.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class ImportantLinksDetailFragment : BottomSheetDialogFragment(), ImportantLinksItemClickListener {

	private val mainViewModel: MainViewModel by sharedViewModel()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? = inflater.inflate(R.layout.layout_important_links_2, container, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		showLoading()
		context?.let { context ->
			val importantLinksDetailAdapter = ImportantLinksDetailAdapter(context, this)
			recycler_view.apply {
				adapter = importantLinksDetailAdapter
				layoutManager = LinearLayoutManager(context)
				setHasFixedSize(true)
			}
			mainViewModel.allImportantLinks.observe(viewLifecycleOwner, Observer {
				when (it.status) {
					RetrofitStatus.SUCCESS -> it.data?.let { data ->
						importantLinksDetailAdapter.setData(data)
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
				} else if (!mainViewModel.allImportantLinksIsExecuted
					&& !mainViewModel.allImportantLinksLoaded
				) mainViewModel.getAllImportantLinks()
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

	override fun onImportantLinkItemClick(result: Result) {
		context?.let { context ->
			result.data?.link?.let { link ->
				if (link.isNotEmpty()) FinestWebView.Builder(context).show(link)
			}
		}
	}
}