package com.imranmelikov.folt.presentation.name

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.imranmelikov.folt.databinding.FragmentNameBinding

class NameFragment : Fragment() {
  private lateinit var binding:FragmentNameBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentNameBinding.inflate(inflater,container,false)

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        nameEditText()
        return binding.root
    }
    private fun nameEditText(){
        val firstName=binding.firstNameEdittext
        val lastName=binding.lastNameEdittext
        if(firstName.text.toString().isNotEmpty()){
            binding.firstCloseBtn.visibility=View.VISIBLE
            firstName.isEnabled=true
        }else{
           firstName.isEnabled=false
            binding.firstCloseBtn.visibility=View.GONE
        }
        if (lastName.text.toString().isNotEmpty()){
            binding.lastCloseBtn.visibility=View.VISIBLE
            lastName.isEnabled=true
        }else{
            binding.lastCloseBtn.visibility=View.GONE
            lastName.isEnabled=false
        }
        binding.firstCloseBtn.setOnClickListener{
            binding.firstCloseBtn.visibility=View.GONE
            firstName.text.clear()
        }
        binding.lastCloseBtn.setOnClickListener{
            binding.lastCloseBtn.visibility=View.GONE
            lastName.text.clear()
        }
    }
}