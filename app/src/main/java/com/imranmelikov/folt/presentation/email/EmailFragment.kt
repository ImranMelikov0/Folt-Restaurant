package com.imranmelikov.folt.presentation.email

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.imranmelikov.folt.constants.AccountConstants
import com.imranmelikov.folt.constants.EditTextEmptyConstants
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.databinding.FragmentEmailBinding
import com.imranmelikov.folt.domain.model.User
import com.imranmelikov.folt.presentation.account.AccountViewModel
import com.imranmelikov.folt.util.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@Suppress("DEPRECATION")
class EmailFragment : Fragment() {
 private lateinit var binding:FragmentEmailBinding
 @Inject lateinit var auth: FirebaseAuth
 private lateinit var accountViewModel: AccountViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentEmailBinding.inflate(inflater,container,false)
        accountViewModel=ViewModelProvider(requireActivity())[AccountViewModel::class.java
        ]
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        observeCRUD()
        updateEmail()
        return binding.root
    }
    private fun updateEmail(){
        val getArg=arguments?.getSerializable(AccountConstants.user) as? User
        getArg?.let { user->
            val emailText=binding.emailEdittext
            emailText.setText(user.email)
            binding.saveBtn.setOnClickListener {
                if (emailText.text.isNotEmpty()){
                    val credential = EmailAuthProvider.getCredential(emailText.text.toString(), user.password)
                    auth.currentUser?.reauthenticate(credential)?.addOnSuccessListener {
                                user.email = emailText.text.toString()
                                accountViewModel.updateUser(user)
                    }?.addOnFailureListener {
                        Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(requireContext(),EditTextEmptyConstants.emailEmpty,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun observeCRUD(){
        accountViewModel.msgLiveData.observe(viewLifecycleOwner){result->
            when(result.status){
                Status.SUCCESS->{
                    result.data?.let {
                        findNavController().popBackStack()
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