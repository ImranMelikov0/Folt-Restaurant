package com.imranmelikov.folt.presentation.newaddress

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.imranmelikov.folt.R
import com.imranmelikov.folt.constants.AddressConstants
import com.imranmelikov.folt.constants.ApiKeyConstants
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.constants.OrderConstants
import com.imranmelikov.folt.databinding.FragmentNewAddressBinding
import com.imranmelikov.folt.domain.model.Venue
import com.imranmelikov.folt.presentation.MainActivity
import com.imranmelikov.folt.presentation.account.AccountViewModel
import com.imranmelikov.folt.presentation.bottomsheetfragments.CountryFragmentBottomSheet
import com.imranmelikov.folt.util.Status

@Suppress("DEPRECATION")
class NewAddressFragment : Fragment() {
   private lateinit var binding:FragmentNewAddressBinding
   private lateinit var bundle: Bundle
   private lateinit var accountViewModel: AccountViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentNewAddressBinding.inflate(inflater,container,false)
        accountViewModel=ViewModelProvider(requireActivity())[AccountViewModel::class.java]

        accountViewModel.getUser()
        bundle= Bundle()
        clickBackBtn()
        controlArguments()
        clickAutoCompleteBtn()
        clickNextBtn()
        observeUser()
        return binding.root
    }

    private fun clickBackBtn() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        })
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun selectCountry(countryName:String){
        binding.countryText.text=countryName
        binding.countryCardView.setOnClickListener {
            val bottomSheetFragment= CountryFragmentBottomSheet()
            if (!bottomSheetFragment.isAdded) {
                bottomSheetFragment.show((requireActivity() as AppCompatActivity).supportFragmentManager, bottomSheetFragment.tag)
            }
            bottomSheetFragment.countryName=countryName
        }
    }
    private fun clickNextBtn(){
        binding.nextBtn.setOnClickListener {
            findNavController().navigate(R.id.action_newAddressFragment_to_addressDetailsFragment,bundle)
        }
    }
    private fun observeUser(){
        accountViewModel.userLiveData.observe(viewLifecycleOwner){result->
            when(result.status){
                Status.SUCCESS->{
                    result.data?.let { user->
                        bundle.apply {
                            putString(AddressConstants.country,user.country.countryName)
                        }
                        selectCountry(user.country.countryName)
                        binding.progress.visibility=View.GONE
                        binding.noResultText.visibility=View.GONE
                    }
                }
                Status.LOADING->{
                    binding.progress.visibility=View.VISIBLE
                    binding.noResultText.visibility=View.GONE
                }
                Status.ERROR->{
                    Toast.makeText(requireContext(), ErrorMsgConstants.errorForUser, Toast.LENGTH_SHORT).show()
                    binding.progress.visibility=View.GONE
                    binding.noResultText.visibility=View.VISIBLE
                }
            }
        }
    }
    private fun controlArguments(){
        val getStringArg=arguments?.getString(AddressConstants.newAddressBundleKey)
        getStringArg?.let { string->
            when(string){
                AddressConstants.orderDetailNewAddress->{
                    val receivedVenue=arguments?.getSerializable(OrderConstants.venueForOrder) as? Venue
                    receivedVenue?.let {
                        bundle.apply {
                            putSerializable(OrderConstants.venueForOrder,it)
                            putString(AddressConstants.addressDetailBundleKey,AddressConstants.orderDetail)
                        }
                    }
                }
                else->{
                    bundle.apply {
                        putString(AddressConstants.addressDetailBundleKey,AddressConstants.newAddress)
                    }
                }
            }
        }
    }
    private fun clickAutoCompleteBtn(){

        val ai: ApplicationInfo = (activity as MainActivity).applicationContext.packageManager
            .getApplicationInfo((activity as MainActivity).applicationContext.packageName, PackageManager.GET_META_DATA)
        val value = ai.metaData[ApiKeyConstants.googlePlaces]

        val apiKey=value.toString()
        if (!Places.isInitialized()){
            Places.initialize(requireActivity(),apiKey)
        }
        binding.autoCompleteCardView.setOnClickListener {
            // Set the fields to specify which types of place data to
            // return after the user has made a selection.
            val fields = listOf(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS,Place.Field.LAT_LNG)

            // Start the autocomplete intent.
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(requireActivity())
            startAutocomplete.launch(intent)
        }
    }
    private val startAutocomplete = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent != null) {
                    val place = Autocomplete.getPlaceFromIntent(intent)
                    binding.addressText.text=place.name
                    binding.nextBtn.isEnabled=true
                    bundle.apply {
                        putString(AddressConstants.streetAddress,place.name)
                    }
                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
                Log.i(TAG, ErrorMsgConstants.errorForUser)
            }
        }
}