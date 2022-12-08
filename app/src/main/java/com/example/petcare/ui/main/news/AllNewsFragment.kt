package com.example.petcare.ui.main.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petcare.data.remote.Result
import com.example.petcare.data.remote.response.News
import com.example.petcare.data.remote.response.NewsResponse
import com.example.petcare.databinding.FragmentAllNewsBinding
import com.example.petcare.helper.showToast

class AllNewsFragment : Fragment() {
    private var _binding: FragmentAllNewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 1)

        viewModel.getNewsHandler(index!!)

        /*
        * Commented for possibility using it in the future, do not erase it just yet.
        * */
//        when(index){
//            1 -> viewModel.getNewsResponseLiveData()
////                viewModel.getNewsResponseLiveData().observe(viewLifecycleOwner){
////                    prepareData(it)
////                }
//
//            2 ->
//                viewModel.getHealthNewsResponseLiveData().observe(viewLifecycleOwner){
//                    prepareData(it)
//                }
//
//            3 ->
//                viewModel.getFunNewsResponseLiveData().observe(viewLifecycleOwner){
//                    prepareData(it)
//                }
//
//            4 ->
//                viewModel.getTipsTrickResponseLiveData().observe(viewLifecycleOwner){
//                    prepareData(it)
//                }
//        }


        viewModel.listNews.observe(viewLifecycleOwner){
            it?.let{
                prepareData(it)
            }
        }

    }

    private fun prepareData(result: Result<NewsResponse>){
        when (result) {
            is Result.Loading -> {
                showLoading(true)
            }
            is Result.Success -> {
                showLoading(false)
                result.data.news?.let{
                    setNewsData(it)
                }
            }
            is Result.Error -> {
                showLoading(false)
                showToast(result.error)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setNewsData(newsList: List<News>) {
        val newsAdapter = NewsAdapter(newsList)
        val numberOfElements = 5
        val randomNews = newsList.asSequence().shuffled().take(numberOfElements).toList()
        val carouselNewsAdapter = CarouselNewsAdapter(randomNews)
        binding.apply {
            recyclerNews.layoutManager = LinearLayoutManager(requireActivity())
            recyclerNews.adapter = newsAdapter
            carouselNews.adapter = carouselNewsAdapter
            carouselNews.setAlpha(true)
            carouselNews.setInfinite(true)
            carouselNews.setIsScrollingEnabled(true)
        }

        carouselNewsAdapter.setOnItemClickCallback(object :NewsAdapter.OnItemClickCallback {
            override fun onItemClicked(data: String) {
                val toDetailNewsFragment = NewsFragmentDirections.actionActionNewsToNewsDetailFragment()
                toDetailNewsFragment.link = data
                findNavController().navigate(toDetailNewsFragment)
            }
        })

        newsAdapter.setOnItemClickCallback(object :NewsAdapter.OnItemClickCallback {
            override fun onItemClicked(data: String) {
                val toDetailNewsFragment = NewsFragmentDirections.actionActionNewsToNewsDetailFragment()
                toDetailNewsFragment.link = data
                findNavController().navigate(toDetailNewsFragment)
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "All News Fragment"
        const val ARG_SECTION_NUMBER = "section_number"
    }

}