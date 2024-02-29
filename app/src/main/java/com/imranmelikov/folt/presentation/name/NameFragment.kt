package com.imranmelikov.folt.presentation.name

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.imranmelikov.folt.constants.AccountConstants
import com.imranmelikov.folt.constants.EditTextEmptyConstants
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.databinding.FragmentNameBinding
import com.imranmelikov.folt.domain.model.User
import com.imranmelikov.folt.presentation.account.AccountViewModel
import com.imranmelikov.folt.util.Status

@Suppress("DEPRECATION")
class NameFragment : Fragment() {
  private lateinit var binding:FragmentNameBinding
  private lateinit var accountViewModel: AccountViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentNameBinding.inflate(inflater,container,false)
        accountViewModel=ViewModelProvider(requireActivity())[AccountViewModel::class.java]

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        observeCRUD()
        nameEditText()
        return binding.root
    }
    private fun nameEditText(){
        val getArg=arguments?.getSerializable(AccountConstants.user) as? User
        getArg?.let { user ->
            val firstName=binding.firstNameEdittext
            val lastName=binding.lastNameEdittext
            firstName.setText(user.firstName)
            lastName.setText(user.lastName)
            binding.saveBtn.setOnClickListener {
                if (firstName.text.isNotEmpty()&&lastName.text.isNotEmpty()){
                    user.firstName=firstName.text.toString()
                    user.lastName=lastName.text.toString()
                    accountViewModel.updateUser(user)
                }else{
                    Toast.makeText(requireContext(),"${EditTextEmptyConstants.firstLastNameEmpty} ${EditTextEmptyConstants.firstLastNameEmpty}",Toast.LENGTH_SHORT).show()
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