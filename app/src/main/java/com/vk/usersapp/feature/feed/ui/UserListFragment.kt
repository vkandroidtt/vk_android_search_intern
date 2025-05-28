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

    val adapter: UserListAdapter by lazy { UserListAdapter() }
    var recycler: RecyclerView? = null
    var queryView: EditText? = null
    var errorView: TextView? = null
    var loaderView: ProgressBar? = null

    var feature: UserListFeature? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(requireContext())
            .inflate(R.layout.fr_user_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
                launch {
                    feature?.viewStateFlow?.collect {
                        renderState(it)
                    }
                }

                launch {
                    queryView?.asFlow()?.collect {
                        feature?.submitAction(UserListAction.QueryChanged(it))
                    }
                }
            }
        }

        lifecycleScope.launch(Dispatchers.Default) {
            feature?.submitAction(UserListAction.Init)
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

            UserListViewState.Loading -> {
                errorView?.isVisible = false
                loaderView?.isVisible = true
                recycler?.isVisible = false
            }
        }
    }
}