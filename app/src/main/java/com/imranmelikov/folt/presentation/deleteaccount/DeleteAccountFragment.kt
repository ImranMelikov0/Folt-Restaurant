package com.imranmelikov.folt.presentation.deleteaccount

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
import com.google.firebase.auth.FirebaseAuth
import com.imranmelikov.folt.constants.AccountConstants
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.databinding.FragmentDeleteAccountBinding
import com.imranmelikov.folt.domain.model.User
import com.imranmelikov.folt.presentation.LoginActivity
import com.imranmelikov.folt.presentation.MainActivity
import com.imranmelikov.folt.presentation.account.AccountViewModel
import com.imranmelikov.folt.util.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class DeleteAccountFragment : Fragment() {
  private lateinit var binding:FragmentDeleteAccountBinding
  private lateinit var accountViewModel: AccountViewModel
  @Inject lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentDeleteAccountBinding.inflate(inflater,container,false)
        accountViewModel=ViewModelProvider(requireActivity())[AccountViewModel::class.java]
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        observeCRUD()
        val getArg=arguments?.getSerializable(AccountConstants.user) as? User
        getArg?.let { user->
            binding.saveBtn.setOnClickListener {
                accountViewModel.deleteUserFromFireStore(user)
                accountViewModel.deleteUser()
                auth.signOut()
                val intent= Intent(requireActivity(), LoginActivity::class.java)
                startActivity(intent)
                (activity as MainActivity).finish()
            }
        }
        return binding.root
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