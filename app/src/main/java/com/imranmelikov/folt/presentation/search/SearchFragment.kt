package com.imranmelikov.folt.presentation.search

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.databinding.FragmentSearchBinding
import com.imranmelikov.folt.presentation.MainActivity
import com.imranmelikov.folt.util.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
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
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPress()
        initialiseSearchRv()
        searchEditText()
        observeVenues()
    }
    private fun onBackPress(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                (activity as MainActivity).finish()
            }
        })
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
                        viewModel.searchVenues(it.toString())
                        observeVenues()
                    }else{
                        searchAdapter.venueList= emptyList()
                        binding.searchClose.visibility=View.GONE
                        binding.searchFilter.visibility=View.GONE
                    }

                }
            }
            binding.searchClose.setOnClickListener {_->
                searchText.text.clear()
                searchAdapter.venueList= emptyList()
                binding.searchClose.visibility=View.GONE
                binding.searchFilter.visibility=View.GONE
            }
        }
    }
    private fun observeVenues(){
        viewModel.venueLiveData.observe(requireActivity()){result->
            when(result.status){
                Status.SUCCESS->{
                    result.data?.let {
                        searchAdapter.venueList=it
                        binding.noResultText.visibility=View.GONE
                        binding.searchProgress.visibility=View.GONE
                    }
                }
                Status.LOADING->{
                    binding.noResultText.visibility=View.GONE
                    binding.searchProgress.visibility=View.VISIBLE
                }
                Status.ERROR->{
                    Toast.makeText(requireContext(), ErrorMsgConstants.errorForUser, Toast.LENGTH_SHORT).show()
                    binding.noResultText.visibility=View.VISIBLE
                    binding.searchProgress.visibility=View.GONE
                }
            }

        }
    }
    private fun initialiseSearchRv(){
        searchAdapter= SearchAdapter()
        binding.searchRv.layoutManager=LinearLayoutManager(requireContext())
        binding.searchRv.adapter=searchAdapter
    }
}