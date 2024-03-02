package com.imranmelikov.folt.presentation.language

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.imranmelikov.folt.constants.AccountConstants
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.constants.FireStoreCollectionConstants
import com.imranmelikov.folt.databinding.FragmentLanguageBinding
import com.imranmelikov.folt.domain.model.Language
import com.imranmelikov.folt.domain.model.User
import com.imranmelikov.folt.languagemanager.LanguageManager
import com.imranmelikov.folt.presentation.account.AccountViewModel
import com.imranmelikov.folt.sharedpreferencesmanager.SharedPreferencesManager
import com.imranmelikov.folt.util.Resource
import com.imranmelikov.folt.util.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@Suppress("DEPRECATION")
class LanguageFragment : Fragment() {
    private lateinit var binding:FragmentLanguageBinding
    private lateinit var languageAdapter: LanguageAdapter
    private lateinit var languageViewModel: LanguageViewModel
    private lateinit var accountViewModel: AccountViewModel
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    private var languageName=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentLanguageBinding.inflate(inflater,container,false)
        languageViewModel= ViewModelProvider(requireActivity())[LanguageViewModel::class.java]
        accountViewModel= ViewModelProvider(requireActivity())[AccountViewModel::class.java]

        getControlArg()

        languageViewModel.getLanguages()
        observeLanguage()
        observeCRUD()

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }
    private fun onItemClick(user: User){
        languageAdapter.onItemClick={
                getUser(it,user)
            sharedPreferencesManager.save(FireStoreCollectionConstants.language,it.languageCode)
            LanguageManager.updateLanguage(requireContext(), it.languageCode)
            findNavController().popBackStack()
            accountViewModel.getUser()
        }
    }

    private fun getControlArg(){
        val getArg=arguments?.getSerializable(AccountConstants.user) as? User
        getArg?.let {user->
            languageName=user.language.language
            initializeRv()
            onItemClick(user)
        }
    }
    private fun getUser(language: Language,user: User){
            user.language=language
            accountViewModel.updateUser(user)
    }

    private fun observeLanguage(){
        languageViewModel.languagesLiveData.observe(viewLifecycleOwner){
            handleResult(it){languageList->
                languageAdapter.languageList=languageList
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
        languageAdapter= LanguageAdapter()
        languageAdapter.languageName=languageName
        binding.languageRv.layoutManager= LinearLayoutManager(requireContext())
        binding.languageRv.adapter=languageAdapter
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