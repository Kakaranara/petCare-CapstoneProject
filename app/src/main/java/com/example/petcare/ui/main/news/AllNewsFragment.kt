package com.example.petcare.ui.main.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petcare.data.remote.response.News
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

        viewModel = ViewModelProvider(this)
            .get(NewsViewModel::class.java)

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)

        viewModel.getNewsResponseLiveData().observe(requireActivity()){
            it.news?.let { newsList ->
                setNewsData(newsList)
            }
            it.exception?.let { exceptions ->
                showToast(exceptions.toString())
            }
        }
    }

    private fun setNewsData(newsList: List<News>) {
        binding.recyclerNews.layoutManager = LinearLayoutManager(requireActivity())
        val newsAdapter = NewsAdapter(newsList)
        binding.recyclerNews.adapter = newsAdapter

        newsAdapter.setOnItemClickCallback(object :NewsAdapter.OnItemClickCallback {
            override fun onItemClicked(data: String) {
//                val intentToDetail = Intent(this@MainActivity, DetailActivity::class.java)
//                intentToDetail.putExtra("DATA", data)
//                startActivity(intentToDetail)
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