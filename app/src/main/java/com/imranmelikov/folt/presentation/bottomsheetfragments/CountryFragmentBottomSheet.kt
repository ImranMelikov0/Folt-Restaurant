package com.imranmelikov.folt.presentation.bottomsheetfragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.data.local.entity.CountryRoom
import com.imranmelikov.folt.databinding.FragmentCountryBottomSheetBinding
import com.imranmelikov.folt.domain.model.Country
import com.imranmelikov.folt.presentation.account.AccountViewModel
import com.imranmelikov.folt.presentation.account.CountryAdapter
import com.imranmelikov.folt.presentation.account.CountryViewModel
import com.imranmelikov.folt.util.Resource
import com.imranmelikov.folt.util.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CountryFragmentBottomSheet:BottomSheetDialogFragment() {
    private lateinit var binding:FragmentCountryBottomSheetBinding
    private lateinit var countryAdapter: CountryAdapter
    private lateinit var countryViewModel: CountryViewModel
    private lateinit var accountViewModel: AccountViewModel
    var countryName=""
    @Inject lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentCountryBottomSheetBinding.inflate(inflater,container,false)
        countryViewModel=ViewModelProvider(requireActivity())[CountryViewModel::class.java]
        accountViewModel=ViewModelProvider(requireActivity())[AccountViewModel::class.java]

            initializeRv()
            accountViewModel.getUser()
            countryViewModel.getCountries()
            observeCountry()
            observeCRUD()

        binding.closeBtn.setOnClickListener {
            dismiss()
        }
        onItemClick()

        return binding.root
    }

    private fun onItemClick(){
        countryAdapter.onItemClick={
            if (auth.currentUser!=null){
                observeUser(it)
            }else{
                countryViewModel.deleteCountries()
                val countryRoom=CountryRoom(it.countryName,it.capital)
                countryViewModel.insertCountry(countryRoom)
            }
            dismiss()
            countryViewModel.getCountry()
            accountViewModel.getUser()
        }
    }
    private fun observeUser(country: Country){
        accountViewModel.userLiveData.observe(viewLifecycleOwner){
            handleResult(it){user->
                user.country=country
                accountViewModel.updateUser(user)
            }
        }
    }
    private fun observeCountry(){
        countryViewModel.countriesLiveData.observe(viewLifecycleOwner){
            handleResult(it){ countryList->
                    countryAdapter.countryList=countryList
            }
        }
    }
    private fun <T> handleResult(result: Resource<T>, actionOnSuccess: (T) -> Unit) {
        when (result.status) {
            Status.ERROR -> {
                errorResult()
            }
            Status.SUCCESS -> {
                result.data?.let(actionOnSuccess)
                successResult()
            }
            Status.LOADING -> {
                loadingResult()
            }
        }
    }

    private fun loadingResult() {
        binding.noResultText.visibility=View.GONE
        binding.progress.visibility=View.VISIBLE
    }

    private fun successResult() {
        binding.noResultText.visibility=View.GONE
        binding.progress.visibility=View.GONE
    }

    private fun errorResult() {
        Toast.makeText(requireContext(), ErrorMsgConstants.errorForUser, Toast.LENGTH_SHORT).show()
        binding.noResultText.visibility=View.VISIBLE
        binding.progress.visibility=View.GONE
    }

    private fun initializeRv(){
        countryAdapter= CountryAdapter()
        countryAdapter.countryName=countryName
        binding.countryRv.layoutManager=LinearLayoutManager(requireContext())
        binding.countryRv.adapter=countryAdapter
    }

    private fun observeCRUD(){
        accountViewModel.msgLiveData.observe(viewLifecycleOwner){result->
            when(result.status){
                Status.SUCCESS->{
                    result.data?.let {
                        Log.d(it.message,it.success.toString())
                    }
                }
                Status.LOADING->{
                }
                Status.ERROR->{
                    Toast.makeText(requireContext(), ErrorMsgConstants.errorForUser, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}