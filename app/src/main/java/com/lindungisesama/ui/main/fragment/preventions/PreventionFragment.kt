package com.lindungisesama.ui.main.fragment.preventions

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lindungisesama.R
import com.lindungisesama.model.raw.preventions.Result
import com.lindungisesama.model.response.RetrofitStatus
import com.lindungisesama.ui.main.MainViewModel
import com.lindungisesama.ui.main.fragment.preventions.adapter.PreventionsAdapter
import com.lindungisesama.ui.main.fragment.preventions.adapter.PreventionsItemClickListener
import com.lindungisesama.ui.preventions.PreventionDetailActivity
import com.lindungisesama.ui.preventions.PreventionDetailActivity.Companion.PREVENTION_DETAIL_DATA
import com.lindungisesama.utils.ErrorHandler
import kotlinx.android.synthetic.main.layout_preventions.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class PreventionFragment : Fragment(), PreventionsItemClickListener {

	private val mainViewModel: MainViewModel by sharedViewModel()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? = inflater.inflate(R.layout.fragment_prevention, container, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		retainInstance = true
		showLoading()
		context?.let { context ->
			val preventionsAdapter = PreventionsAdapter(context, this)
			recycler_view.apply {
				adapter = preventionsAdapter
				layoutManager = LinearLayoutManager(context)
				setHasFixedSize(true)
			}
			mainViewModel.preventions.observe(viewLifecycleOwner, Observer {
				when (it.status) {
					RetrofitStatus.SUCCESS -> it.data?.let { data ->
						preventionsAdapter.setData(data)
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
				} else if (!mainViewModel.preventionsIsExecuted
					&& !mainViewModel.preventionsLoaded
				) mainViewModel.getPreventions()
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
		startActivity(
			Intent(activity, PreventionDetailActivity::class.java)
				.putExtra(PREVENTION_DETAIL_DATA, result)
		)
	}
}
