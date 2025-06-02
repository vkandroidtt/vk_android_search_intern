package com.vk.usersapp.feature.feed.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vk.usersapp.R
import com.vk.usersapp.core.ViewModelProvider
import com.vk.usersapp.core.asFlow
import com.vk.usersapp.feature.feed.presentation.UserListAction
import com.vk.usersapp.feature.feed.presentation.UserListFeature
import com.vk.usersapp.feature.feed.presentation.UserListViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserListFragment : Fragment() {

    private val adapter: UserListAdapter by lazy { UserListAdapter() }
    private var recycler: RecyclerView? = null
    private var queryView: EditText? = null
    private var errorView: TextView? = null
    private var loaderView: ProgressBar? = n

    private var feature: UserListFeature? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(requireContext()).inflate(R.layout.fr_user_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        suggestionsView = view.findViewById(R.id.suggestions_recycler)  // Инициализация
        suggestionsView?.layoutManager = LinearLayoutManager(requireContext())
        suggestionsView?.adapter = SuggestionsAdapter { query ->
            queryView?.setText(query)
            feature?.submitAction(UserListAction.QueryChanged(query))
        }
        recycler = view.findViewById(R.id.recycler)
        queryView = view.findViewById(R.id.search_input)
        errorView = view.findViewById(R.id.error)
        loaderView = view.findViewById(R.id.loader)
        recycler?.adapter = adapter
        recycler?.layoutManager = LinearLayoutManager(view.context)

        feature = ViewModelProvider.obtainFeature {
            UserListFeature()
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                queryView?.asFlow()
                    .debounce(300)  // Задержка для избежания спама
                    .collect { query ->
                        feature?.submitAction(UserListAction.FetchSuggestions(query))
                    launch {
                        feature?.viewStateFlow?.collect {
                            renderState(it)
                        }
                    }
                    queryView?.asFlow()?.collect {
                        feature?.submitAction(UserListAction.QueryChanged(it))
                    }
                }

        }

        viewLifecycleOwner.lifecycleScope.launch {
            feature?.submitAction(UserListAction.Init)
            }
        }
    }

    override fun onDestroy() {
        feature?.let {
            if (activity?.isFinishing == true) {
                ViewModelProvider.destroyFeature(it.javaClass)
            }
        }
        super.onDestroy()
    }

    private fun renderState(viewState: UserListViewState) {
        when (viewState) {
            is UserListViewState.Error -> {
                errorView?.isVisible = true
                errorView?.text = viewState.errorText
                loaderView?.isVisible = false
                recycler?.isVisible = false
            }
            is UserListViewState.List -> {
                loaderView?.isVisible = false
                if (viewState.itemsList.isEmpty()) {
                    errorView?.isVisible = true
                    recycler?.isVisible = false
                    errorView?.text = requireContext().getString(R.string.nothing_found)
                } else {
                    errorView?.isVisible = false
                    recycler?.isVisible = true
                    adapter.setUsers(viewState.itemsList)
                }
            }
            is UserListViewState.Loading -> {
                errorView?.isVisible = false
                loaderView?.isVisible = true
                recycler?.isVisible = false
            }
                is UserListViewState.Suggestions -> {
                    suggestionsView?.isVisible = true
                    (suggestionsView?.adapter as? SuggestionsAdapter)?.submitList(viewState.suggestions)
                }
            }
        }
    }
}