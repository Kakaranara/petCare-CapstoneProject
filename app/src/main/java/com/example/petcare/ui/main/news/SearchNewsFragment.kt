package com.example.petcare.ui.main.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petcare.data.remote.Result
import com.example.petcare.data.remote.response.News
import com.example.petcare.data.remote.response.NewsResponse
import com.example.petcare.databinding.FragmentSearchNewsBinding
import com.example.petcare.helper.showToast


class SearchNewsFragment : Fragment() {
    private var _binding: FragmentSearchNewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        binding.svNews.setIconified(false)

        binding.svNews.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.getSearchNewsLiveData(query)
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        viewModel.searchListNews.observe(viewLifecycleOwner){
            prepareData(it)
        }
    }


    private fun prepareData(result: Result<NewsResponse>){
        when (result) {
            is Result.Loading -> {
                showLoading(true)
                showEmptyIndicator(false)
            }
            is Result.Success -> {
                showLoading(false)
                if(result.data.news.isNullOrEmpty()){
                    showEmptyIndicator(true)
                }
                result.data.news?.let{
                    setNewsData(it)
                }
            }
            is Result.Error -> {
                showLoading(false)
                showEmptyIndicator(false)
                showToast(result.error)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.searchBackground.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.progressBarSearch.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyIndicator(isEmpty: Boolean) {
        binding.emptySearchBackground.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.emptySearchText.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun setNewsData(newsList: List<News>) {
        val newsAdapter = NewsAdapter(newsList)
        binding.apply {
            recyclerSearchNews.layoutManager = LinearLayoutManager(requireActivity())
            recyclerSearchNews.adapter = newsAdapter
        }

        newsAdapter.setOnItemClickCallback(object :NewsAdapter.OnItemClickCallback {
            override fun onItemClicked(data: String) {
                val toDetailNewsFragment = SearchNewsFragmentDirections.actionSearchNewsFragmentToNewsDetailFragment()
                toDetailNewsFragment.link = data
                findNavController().navigate(toDetailNewsFragment)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}