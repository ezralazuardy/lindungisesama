package com.lindungisesama.ui.main.fragment.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lindungisesama.R
import com.lindungisesama.model.raw.references.Result
import com.lindungisesama.model.response.RetrofitStatus
import com.lindungisesama.ui.main.MainViewModel
import com.lindungisesama.ui.main.fragment.about.adapter.ReferencesDetailAdapter
import com.lindungisesama.ui.main.fragment.about.adapter.ReferencesItemClickListener
import com.lindungisesama.utils.ErrorHandler
import com.thefinestartist.finestwebview.FinestWebView
import kotlinx.android.synthetic.main.layout_references_2.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class ReferencesDetailFragment : BottomSheetDialogFragment(),
	ReferencesItemClickListener {

	private val mainViewModel: MainViewModel by sharedViewModel()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? = inflater.inflate(R.layout.layout_references_2, container, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		showLoading()
		context?.let { context ->
			val referencesDetailAdapter =
				ReferencesDetailAdapter(
					context,
					this
				)
			recycler_view.apply {
				adapter = referencesDetailAdapter
				layoutManager = LinearLayoutManager(context)
				setHasFixedSize(true)
			}
			mainViewModel.allReferences.observe(viewLifecycleOwner, Observer {
				when (it.status) {
					RetrofitStatus.SUCCESS -> it.data?.let { data ->
						referencesDetailAdapter.setData(data)
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
				} else if (!mainViewModel.allReferencesIsExecuted
					&& !mainViewModel.allReferencesLoaded
				) mainViewModel.getAllReferences()
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

	override fun onReferenceItemClick(result: Result) {
		context?.let { context ->
			result.data?.link?.let { link ->
				if (link.isNotEmpty()) FinestWebView.Builder(context).show(link)
			}
		}
	}
}