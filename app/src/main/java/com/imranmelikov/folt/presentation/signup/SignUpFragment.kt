package com.imranmelikov.folt.presentation.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.imranmelikov.folt.R
import com.imranmelikov.folt.constants.EditTextEmptyConstants
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.constants.FireStoreConstants
import com.imranmelikov.folt.data.local.entity.CountryRoom
import com.imranmelikov.folt.data.local.entity.LanguageRoom
import com.imranmelikov.folt.databinding.FragmentSignUpBinding
import com.imranmelikov.folt.domain.model.Country
import com.imranmelikov.folt.domain.model.Language
import com.imranmelikov.folt.domain.model.Location
import com.imranmelikov.folt.domain.model.User
import com.imranmelikov.folt.presentation.LoginActivity
import com.imranmelikov.folt.presentation.MainActivity
import com.imranmelikov.folt.presentation.account.CountryViewModel
import com.imranmelikov.folt.presentation.bottomsheetfragments.CountryFragmentBottomSheet
import com.imranmelikov.folt.presentation.bottomsheetfragments.LanguageBottomSheetFragment
import com.imranmelikov.folt.presentation.language.LanguageViewModel
import com.imranmelikov.folt.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var viewModel: SignUpViewModel
    private lateinit var countryViewModel: CountryViewModel
    private lateinit var languageViewModel:LanguageViewModel
    private lateinit var bottomSheetFragment:CountryFragmentBottomSheet
    private lateinit var languageBottomSheetFragment: LanguageBottomSheetFragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentSignUpBinding.inflate(inflater,container,false)
        viewModel=ViewModelProvider(requireActivity())[SignUpViewModel::class.java]
        countryViewModel=ViewModelProvider(requireActivity())[CountryViewModel::class.java]
        languageViewModel=ViewModelProvider(requireActivity())[LanguageViewModel::class.java]

        bottomSheetFragment= CountryFragmentBottomSheet()
        languageBottomSheetFragment= LanguageBottomSheetFragment()
        getFunctions()
        return binding.root
    }

    private fun getFunctions(){
        countryViewModel.getCountry()
        languageViewModel.getLanguage()
        clickBtn()
        getControlArguments()
        observeCRUD()
    }

    private fun clickBtn(){
        clickBackBtn()
        clickAlreadyAccount()
        clickCountryLinear()
        clickLanguageLinear()
    }

    private fun clickBackBtn(){
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun clickCountryLinear(){
        binding.countryLinear.setOnClickListener {
            if (!bottomSheetFragment.isAdded) {
                bottomSheetFragment.show((requireActivity() as AppCompatActivity).supportFragmentManager, bottomSheetFragment.tag)
            }
        }
    }

    private fun clickLanguageLinear(){
        binding.languageLinear.setOnClickListener {
            if (!languageBottomSheetFragment.isAdded) {
                languageBottomSheetFragment.show((requireActivity() as AppCompatActivity).supportFragmentManager, languageBottomSheetFragment.tag)
            }
        }
    }

    private fun getControlArguments(){
        val lat=arguments?.getDouble(FireStoreConstants.lat)
        val lng=arguments?.getDouble(FireStoreConstants.lng)
        lat?.let { latitude->
            lng?.let {longitude->
                getCountry(latitude,longitude)
            }
        }
    }

    private fun clickNextBtn(countryRoom: List<CountryRoom>,languageRoom: List<LanguageRoom>,lat: Double,lng: Double){
        val email=binding.emailEdittext
        val firstName=binding.firstNameEdittext
        val lastName=binding.lastNameEdittext
        val password=binding.passwordEdittext
        val tel=binding.telEdittext
        binding.saveBtn.setOnClickListener {
            when {
                tel.text.isNotEmpty()&&firstName.text.isNotEmpty()&&lastName.text.isNotEmpty()
                        &&password.text.isNotEmpty()&&email.text.isNotEmpty()&&tel.text.length==11&&firstName.text.length<100
                        &&lastName.text.length<100&&countryRoom.isNotEmpty()&&languageRoom.isNotEmpty()-> {
                   countryRoom.map {countryRoom->
                                languageRoom.map {languageRoom->
                                    val country=Country(countryRoom.countryName,countryRoom.capital)
                                    val language= Language(languageRoom.language,languageRoom.languageCode)
                                    val locationLatLng= Location(lat, lng)
                                    val user=User("",email.text.toString(),password.text.toString(),tel.text.toString()
                                        ,"",firstName.text.toString(),lastName.text.toString(),country,language,locationLatLng)
                                    viewModel.signUp(user)
                                }
                            }
                }
                tel.text.isEmpty() -> {
                    Toast.makeText(requireContext(),EditTextEmptyConstants.telEmpty,Toast.LENGTH_SHORT).show()
                }
                firstName.text.isEmpty() || lastName.text.isEmpty() -> {
                    Toast.makeText(requireContext(),EditTextEmptyConstants.firstLastNameEmpty,Toast.LENGTH_SHORT).show()
                }
                password.text.isEmpty()->{
                    Toast.makeText(requireContext(),EditTextEmptyConstants.passwordEmpty,Toast.LENGTH_SHORT).show()
                }
                tel.text.length>11 || tel.text.length<11->{
                    Toast.makeText(requireContext(),EditTextEmptyConstants.telLength,Toast.LENGTH_SHORT).show()
                }
                email.text.isEmpty()->{
                    Toast.makeText(requireContext(),EditTextEmptyConstants.emailEmpty,Toast.LENGTH_SHORT).show()
                }
                firstName.text.length>100 || lastName.text.length>100->{
                    Toast.makeText(requireContext(),EditTextEmptyConstants.firstLastNameLength,Toast.LENGTH_SHORT).show()
                }
                countryRoom.isEmpty()->{
                    Toast.makeText(requireContext(),EditTextEmptyConstants.selectCountry,Toast.LENGTH_SHORT).show()
                }
                languageRoom.isEmpty()->{
                    Toast.makeText(requireContext(),EditTextEmptyConstants.selectLanguage,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun getCountry(lat:Double,lng:Double){
        countryViewModel.countryLiveData.observe(viewLifecycleOwner){countryRoom->
            getLanguage(countryRoom,lat,lng)
            if (countryRoom.isNotEmpty()) {
                countryRoom.map {
                    binding.countryText.text = it.countryName
                    bottomSheetFragment.countryName=it.countryName
                }
            }
        }
    }
    private fun getLanguage(countryRoom: List<CountryRoom>,lat: Double,lng: Double){
        languageViewModel.languageLiveData.observe(viewLifecycleOwner){languageRoom->
            clickNextBtn(countryRoom,languageRoom,lat,lng)
            if (languageRoom.isNotEmpty()) {
                languageRoom.map {
                    binding.languageText.text = it.language
                    languageBottomSheetFragment.languageName=it.language
                }
            }
        }
    }
    private fun clickAlreadyAccount(){
        binding.alreadyAccountText.setOnClickListener{
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }
    }
    private fun observeCRUD(){
        viewModel.msgLiveData.observe(viewLifecycleOwner){result->
            when(result.status){
                Status.SUCCESS->{
                    result.data?.let {
                        val intent=Intent(requireActivity(),MainActivity::class.java)
                        startActivity(intent)
                        (activity as LoginActivity).finish()
                        countryViewModel.deleteCountries()
                        languageViewModel.deleteLanguages()
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
