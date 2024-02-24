package com.imranmelikov.folt.presentation.account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.imranmelikov.folt.R
import com.imranmelikov.folt.databinding.FragmentAccountBinding
import com.imranmelikov.folt.presentation.MainActivity
import com.imranmelikov.folt.presentation.bottomsheetfragments.CountryFragmentBottomSheet

class AccountFragment : Fragment() {
  private lateinit var binding:FragmentAccountBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentAccountBinding.inflate(inflater,container,false)
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
        binding.accountLanguageLinear.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_languageFragment)
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
        binding.accountCountryLinear.setOnClickListener {
            val bottomSheetFragment=CountryFragmentBottomSheet()
            if (!bottomSheetFragment.isAdded) {
                bottomSheetFragment.show((requireActivity() as AppCompatActivity).supportFragmentManager, bottomSheetFragment.tag)
            }
            /////////
            bottomSheetFragment.countryName="Azerbaijan"
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
}