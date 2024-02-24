package com.imranmelikov.folt.presentation.bottomsheetfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.databinding.FragmentCountryBottomSheetBinding
import com.imranmelikov.folt.presentation.account.CountryAdapter
import com.imranmelikov.folt.presentation.account.CountryViewModel
import com.imranmelikov.folt.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CountryFragmentBottomSheet:BottomSheetDialogFragment() {
    private lateinit var binding:FragmentCountryBottomSheetBinding
    private lateinit var countryAdapter: CountryAdapter
    private lateinit var countryViewModel: CountryViewModel
    var countryName=""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentCountryBottomSheetBinding.inflate(inflater,container,false)
        countryViewModel=ViewModelProvider(requireActivity())[CountryViewModel::class.java]

            initializeRv()
            countryViewModel.getCountries()
            observeCountry()

        binding.closeBtn.setOnClickListener {
            dismiss()
        }
        countryAdapter.onItemClick={
            ////////
            dismiss()
        }
        return binding.root
    }

    private fun observeCountry(){
        countryViewModel.countryLiveData.observe(viewLifecycleOwner){
            when(it.status){
                Status.ERROR->{
                    Toast.makeText(requireContext(), ErrorMsgConstants.errorForUser, Toast.LENGTH_SHORT).show()
                    binding.noResultText.visibility=View.VISIBLE
                    binding.progress.visibility=View.GONE
                }
                Status.SUCCESS->{
                    it.data?.let {countryList->
                        countryAdapter.countryList=countryList
                    }
                    binding.noResultText.visibility=View.GONE
                    binding.progress.visibility=View.GONE
                }
                Status.LOADING->{
                    binding.noResultText.visibility=View.GONE
                    binding.progress.visibility=View.VISIBLE
                }
            }
        }
    }
    private fun initializeRv(){
        countryAdapter= CountryAdapter()
        countryAdapter.countryName=countryName
        binding.countryRv.layoutManager=LinearLayoutManager(requireContext())
        binding.countryRv.adapter=countryAdapter
    }
}