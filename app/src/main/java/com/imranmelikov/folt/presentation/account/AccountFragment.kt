package com.imranmelikov.folt.presentation.account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.imranmelikov.folt.R
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.databinding.FragmentAccountBinding
import com.imranmelikov.folt.presentation.MainActivity
import com.imranmelikov.folt.presentation.bottomsheetfragments.CountryFragmentBottomSheet
import com.imranmelikov.folt.presentation.bottomsheetfragments.LanguageBottomSheetFragment
import com.imranmelikov.folt.util.Status

class AccountFragment : Fragment() {
  private lateinit var binding:FragmentAccountBinding
  private lateinit var accountViewModel:AccountViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentAccountBinding.inflate(inflater,container,false)
        accountViewModel=ViewModelProvider(requireActivity())[AccountViewModel::class.java]

        accountViewModel.getUser()
        observeUser()
        clickEmail()
        clickName()
        clickBtn()
        clickBackBtn()
        return binding.root
    }

    private fun clickBtn(){
        binding.accountDeleteLinear.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_deleteAccountFragment)
        }
        binding.accountAppearanceLinear.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_appearanceFragment)
        }
        binding.accountShareLinear.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "https://Folt.com")
            }
            startActivity(Intent.createChooser(shareIntent, "Share"))
        }
        binding.accountLogOutLinear.setOnClickListener {

        }
    }
    private fun clickBackBtn(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object :
            OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
                (activity as MainActivity).showBottomNav()
            }
        })
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
            (activity as MainActivity).showBottomNav()
        }
    }
    private fun clickEmail(){
        binding.accountEmailLinear.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_emailFragment)
        }
    }
    private fun clickName(){
        binding.accountNameLinear.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_nameFragment)
        }
    }
    private fun observeUser(){
        accountViewModel.userLiveData.observe(viewLifecycleOwner){result->
            when(result.status){
                Status.SUCCESS->{
                    result.data?.let { user->
                        selectCountry(user.country.countryName)
                        selectLanguage(user.language.language)
                        binding.emailText.text=user.email
                        binding.telText.text=user.tel
                        "${user.firstName} ${user.lastName}".also { binding.nameText.text = it }
                        if (user.imageUrl!=""){
                            Glide.with(requireActivity())
                                .load(user.imageUrl)
                                .into(binding.profileImage)
                        }
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

    private fun selectLanguage(language:String){
        binding.languageText.text=language
        binding.accountLanguageLinear.setOnClickListener {
            val languageBottomSheetFragment=LanguageBottomSheetFragment()
            if (!languageBottomSheetFragment.isAdded) {
                languageBottomSheetFragment.show((requireActivity() as AppCompatActivity).supportFragmentManager, languageBottomSheetFragment.tag)
            }
            languageBottomSheetFragment.languageName=language
        }
    }

    private fun selectCountry(countryName: String) {
        binding.countryText.text=countryName
        binding.accountCountryLinear.setOnClickListener {
            val bottomSheetFragment=CountryFragmentBottomSheet()
            if (!bottomSheetFragment.isAdded) {
                bottomSheetFragment.show((requireActivity() as AppCompatActivity).supportFragmentManager, bottomSheetFragment.tag)
            }
            bottomSheetFragment.countryName=countryName
        }
    }
}