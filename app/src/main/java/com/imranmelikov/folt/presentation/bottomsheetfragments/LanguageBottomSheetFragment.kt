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
import com.imranmelikov.folt.data.local.entity.LanguageRoom
import com.imranmelikov.folt.databinding.FragmentLanguageBottomSheetBinding
import com.imranmelikov.folt.domain.model.Language
import com.imranmelikov.folt.presentation.account.AccountViewModel
import com.imranmelikov.folt.presentation.language.LanguageAdapter
import com.imranmelikov.folt.presentation.language.LanguageViewModel
import com.imranmelikov.folt.util.Resource
import com.imranmelikov.folt.util.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LanguageBottomSheetFragment:BottomSheetDialogFragment() {
    private lateinit var binding: FragmentLanguageBottomSheetBinding
    private lateinit var languageAdapter: LanguageAdapter
    private lateinit var languageViewModel: LanguageViewModel
    private lateinit var accountViewModel:AccountViewModel
    var languageName=""
    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentLanguageBottomSheetBinding.inflate(inflater,container,false)
        languageViewModel= ViewModelProvider(requireActivity())[LanguageViewModel::class.java]
        accountViewModel=ViewModelProvider(requireActivity())[AccountViewModel::class.java]

        initializeRv()
        languageViewModel.getLanguages()
        accountViewModel.getUser()
        observeLanguage()
        observeCRUD()

        binding.closeBtn.setOnClickListener {
            dismiss()
        }
         onItemClick()
        return binding.root
    }
    private fun onItemClick(){
        languageAdapter.onItemClick={
            if (auth.currentUser!=null){
                observeUser(it)
            }else{
                languageViewModel.deleteLanguages()
                val languageRoom= LanguageRoom(it.language,it.languageCode)
                languageViewModel.insertLanguage(languageRoom)
            }
            dismiss()
            accountViewModel.getUser()
            languageViewModel.getLanguage()
        }
    }
    private fun observeUser(language: Language){
        accountViewModel.userLiveData.observe(viewLifecycleOwner){
            handleResult(it){user->
                user.language=language
                accountViewModel.updateUser(user)
            }
        }
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