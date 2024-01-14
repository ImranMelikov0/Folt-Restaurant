package com.imranmelikov.folt.presentation.stores

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.imranmelikov.folt.databinding.FragmentStoreBinding

class StoreFragment : Fragment() {
  private lateinit var binding:FragmentStoreBinding
    private lateinit var viewModel:StoreViewModel
    private lateinit var storeAdapter: StoreAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentStoreBinding.inflate(inflater,container,false)
        viewModel= ViewModelProvider(requireActivity())[StoreViewModel::class.java]
        viewModel.getVenues()
        initialiseStoreRv()
        observeVenues()
        return binding.root
    }
    private fun initialiseStoreRv(){
        storeAdapter= StoreAdapter()
        binding.storesRv.layoutManager= LinearLayoutManager(requireContext())
        binding.storesRv.adapter=storeAdapter
    }

    private fun observeVenues(){
        viewModel.venueLiveData.observe(viewLifecycleOwner, Observer {
            storeAdapter.venueList=it
        })
    }
}