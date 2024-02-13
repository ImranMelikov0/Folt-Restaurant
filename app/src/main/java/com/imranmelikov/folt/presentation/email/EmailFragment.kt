package com.imranmelikov.folt.presentation.email

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.imranmelikov.folt.databinding.FragmentEmailBinding

class EmailFragment : Fragment() {
 private lateinit var binding:FragmentEmailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentEmailBinding.inflate(inflater,container,false)

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }
    private fun editText(){
        val emailText=binding.emailEdittext.text.toString()
    }
}