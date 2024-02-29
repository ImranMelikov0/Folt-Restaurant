package com.imranmelikov.folt.presentation.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.imranmelikov.folt.constants.EditTextEmptyConstants
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.databinding.FragmentSignInBinding
import com.imranmelikov.folt.presentation.LoginActivity
import com.imranmelikov.folt.presentation.MainActivity
import com.imranmelikov.folt.presentation.account.CountryViewModel
import com.imranmelikov.folt.presentation.language.LanguageViewModel
import com.imranmelikov.folt.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {
    private lateinit var binding:FragmentSignInBinding
    private lateinit var signInViewModel: SignInViewModel
    private lateinit var countryViewModel:CountryViewModel
    private lateinit var languageViewModel:LanguageViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentSignInBinding.inflate(inflater,container,false)
        signInViewModel=ViewModelProvider(requireActivity())[SignInViewModel::class.java]
        countryViewModel=ViewModelProvider(requireActivity())[CountryViewModel::class.java]
        languageViewModel=ViewModelProvider(requireActivity())[LanguageViewModel::class.java]
        signIn()
        observeCRUD()
        clickDontHaveAccount()
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

    private fun signIn(){
        val email=binding.emailEdittext
        val password=binding.passwordEdittext
        binding.saveBtn.setOnClickListener {
            when{
                email.text.isNotEmpty()&&password.text.isNotEmpty()->{
                    signInViewModel.signIn(email.text.toString(), password.text.toString())
                }
                email.text.isEmpty()->{
                    Toast.makeText(requireContext(),EditTextEmptyConstants.emailEmpty,Toast.LENGTH_SHORT).show()
                }
                password.text.isEmpty()->{
                    Toast.makeText(requireContext(), EditTextEmptyConstants.passwordEmpty, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun clickDontHaveAccount(){
        binding.alreadyAccountText.setOnClickListener{
            findNavController().popBackStack()
        }
    }
    private fun observeCRUD(){
        signInViewModel.msgLiveData.observe(viewLifecycleOwner){result->
            when(result.status){
                Status.SUCCESS->{
                    result.data?.let {
                        val intent= Intent(requireActivity(), MainActivity::class.java)
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