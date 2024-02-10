package com.imranmelikov.folt.presentation.search

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.imranmelikov.folt.databinding.FragmentSearchBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
   private lateinit var binding:FragmentSearchBinding
   private lateinit var viewModel: SearchViewModel
   private lateinit var searchAdapter: SearchAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentSearchBinding.inflate(inflater,container,false)
        viewModel=ViewModelProvider(requireActivity())[SearchViewModel::class.java]
        initialiseSearchRv()
        searchEditText()
        observeVenues()
        return binding.root
    }
    private fun searchEditText() {
        val searchText=binding.searchEdittext
        searchText.requestFocus()
        searchText.postDelayed({
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(searchText, InputMethodManager.SHOW_IMPLICIT)
        }, 250)
        var job: Job?=null
        searchText.addTextChangedListener{
            job?.cancel()
            job =lifecycleScope.launch {
                it?.let {
                    if (it.toString().isNotEmpty()){
                        binding.searchClose.visibility=View.VISIBLE
                        binding.searchFilter.visibility=View.VISIBLE
                        delay(1000)
                        viewModel.getVenues(it.toString())
                    }else{
                        binding.searchClose.visibility=View.GONE
                        binding.searchFilter.visibility=View.GONE
                    }

                }
            }
            binding.searchClose.setOnClickListener {_->
                searchText.text.clear()
                binding.searchClose.visibility=View.GONE
                binding.searchFilter.visibility=View.GONE
            }
        }
    }
    private fun observeVenues(){
        viewModel.venueLiveData.observe(viewLifecycleOwner){
            searchAdapter.venueList=it
        }
    }
    private fun initialiseSearchRv(){
        searchAdapter= SearchAdapter()
        binding.searchRv.layoutManager=LinearLayoutManager(requireContext())
        binding.searchRv.adapter=searchAdapter
    }
}