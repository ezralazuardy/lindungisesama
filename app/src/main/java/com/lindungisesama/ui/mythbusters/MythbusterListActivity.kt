package com.lindungisesama.ui.mythbusters

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lindungisesama.R
import com.lindungisesama.model.raw.mythbusters.Result
import com.lindungisesama.model.response.RetrofitStatus
import com.lindungisesama.ui.main.MainViewModel
import com.lindungisesama.ui.main.fragment.home.adapter.MythbustersItemClickListener
import com.lindungisesama.ui.mythbusters.MythbusterDetailActivity.Companion.MYTHBUSTER_DETAIL_DATA
import com.lindungisesama.ui.mythbusters.adapter.MythbusterListAdapter
import com.lindungisesama.utils.ConnectionState
import com.lindungisesama.utils.ErrorHandler
import kotlinx.android.synthetic.main.activity_mythbuster_list.*
import kotlinx.android.synthetic.main.layout_mythbusters_list.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MythbusterListActivity : AppCompatActivity(), MythbustersItemClickListener {

	private val mainViewModel: MainViewModel by viewModel()
	private val connectionState: ConnectionState by inject()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_mythbuster_list)
		showLoading()
		val mythbusterListAdapter = MythbusterListAdapter(this, this)
		back_button.setOnClickListener {
			super.onBackPressed()
		}
		recycler_view.apply {
			adapter = mythbusterListAdapter
			layoutManager = LinearLayoutManager(this@MythbusterListActivity)
			setHasFixedSize(true)
		}
		mainViewModel.allMythbusters.observe(this, Observer {
			when (it.status) {
				RetrofitStatus.SUCCESS -> it.data?.let { data ->
					mythbusterListAdapter.setData(data)
					hideLoading()
				}
				else -> ErrorHandler.show(supportFragmentManager, it.status)
			}
		})
		mainViewModel.prismicResponse.observe(this, Observer {
			if (it.status != RetrofitStatus.SUCCESS)
				ErrorHandler.show(supportFragmentManager, it.status)
			else
				if (!mainViewModel.allMythbustersIsExecuted && !mainViewModel.allMythbustersLoaded)
					mainViewModel.getAllMythbusters()
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

	override fun onMythbustersItemClick(result: Result) {
		startActivity(
			Intent(this, MythbusterDetailActivity::class.java)
				.putExtra(MYTHBUSTER_DETAIL_DATA, result)
		)
	}
}
