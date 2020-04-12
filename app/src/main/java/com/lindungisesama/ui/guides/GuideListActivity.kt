package com.lindungisesama.ui.guides

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lindungisesama.R
import com.lindungisesama.model.raw.guides.Result
import com.lindungisesama.model.response.RetrofitStatus
import com.lindungisesama.ui.guides.GuideDetailActivity.Companion.GUIDE_DETAIL_DATA
import com.lindungisesama.ui.guides.adapter.GuideListAdapter
import com.lindungisesama.ui.main.MainViewModel
import com.lindungisesama.ui.main.fragment.home.adapter.GuidesItemClickListener
import com.lindungisesama.utils.ConnectionState
import com.lindungisesama.utils.ErrorHandler
import kotlinx.android.synthetic.main.activity_guide_list.*
import kotlinx.android.synthetic.main.layout_guides_list.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class GuideListActivity : AppCompatActivity(), GuidesItemClickListener {

	private val mainViewModel: MainViewModel by viewModel()
	private val connectionState: ConnectionState by inject()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_guide_list)
		showLoading()
		val guideListAdapter = GuideListAdapter(this, this)
		back_button.setOnClickListener {
			super.onBackPressed()
		}
		recycler_view.apply {
			adapter = guideListAdapter
			layoutManager = LinearLayoutManager(this@GuideListActivity)
			setHasFixedSize(true)
		}
		mainViewModel.allGuides.observe(this, Observer {
			when (it.status) {
				RetrofitStatus.SUCCESS -> it.data?.let { data ->
					guideListAdapter.setData(data)
					hideLoading()
				}
				else -> ErrorHandler.show(supportFragmentManager, it.status)
			}
		})
		mainViewModel.prismicResponse.observe(this, Observer {
			if (it.status != RetrofitStatus.SUCCESS)
				ErrorHandler.show(supportFragmentManager, it.status)
			else
				if (!mainViewModel.allGuidesIsExecuted && !mainViewModel.allGuidesLoaded)
					mainViewModel.getAllGuides()
		})
		connectionState.observe(this, Observer { connected ->
			if (connected)
				if (!mainViewModel.refUpdateIsExecuted && !mainViewModel.refUpdated)
					mainViewModel.getUpdatedRef()
				else
					ErrorHandler.show(supportFragmentManager, RetrofitStatus.NETWORK_ERROR)
		})
	}

	private fun showLoading() {
		recycler_view.visibility = View.GONE
		loading.visibility = View.VISIBLE
	}

	private fun hideLoading() {
		loading.visibility = View.GONE
		recycler_view.visibility = View.VISIBLE
	}

	override fun onGuidesItemClick(result: Result) {
		startActivity(
			Intent(this, GuideDetailActivity::class.java)
				.putExtra(GUIDE_DETAIL_DATA, result)
		)
	}
}
