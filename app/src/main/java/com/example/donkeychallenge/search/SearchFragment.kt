package com.example.donkeychallenge.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.donkeychallenge.R
import com.example.donkeychallenge.databinding.FragmentSearchBinding
import com.example.donkeychallenge.main.MainViewModel
import com.example.donkeychallenge.model.SearchResult
import com.example.donkeychallenge.utils.EventObserver
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*
import kotlin.concurrent.schedule

class SearchFragment : Fragment(R.layout.fragment_search) {

    private val binding: FragmentSearchBinding by viewBinding()
    private val viewModel by sharedViewModel<MainViewModel>()
    private val hubAdapter = HubAdapter(::onHubClicked)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        prepareLiveDataObserver()
    }

    private fun onHubClicked(hub: SearchResult) {
        viewModel.pickHub(hub)
        activity?.supportFragmentManager?.popBackStack()
    }

    private fun initView() = binding.apply {
        textInput.addTextChangedListener(object : TextWatcher {
            var timer: Timer? = null
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {} //not used
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                timer?.apply {
                    cancel()
                    purge()
                }
            }

            override fun afterTextChanged(text: Editable?) {
                timer = Timer().apply {
                    schedule(TEXT_WATCHER_DELAY) {
                        viewModel.search(text.toString())
                    }
                }
            }
        })

        searchResultRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = hubAdapter
        }
    }

    private fun showError(throwable: Throwable) {
        Log.e("Donkey", "Coroutine error: ${throwable.message}")
        Toast.makeText(requireContext(), "Some error here!", Toast.LENGTH_SHORT).show()
    }

    private fun prepareLiveDataObserver() = with(viewModel) {
        searchResult.observe(viewLifecycleOwner, EventObserver {
            hubAdapter.updateList(it) })
        error.observe(viewLifecycleOwner, EventObserver { showError(it) })
    }

    companion object {

        private const val TEXT_WATCHER_DELAY = 500L

        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}
