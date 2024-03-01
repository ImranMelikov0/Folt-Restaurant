package com.imranmelikov.folt.presentation.photogalleryFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.imranmelikov.folt.constants.AccountConstants
import com.imranmelikov.folt.constants.ErrorMsgConstants
import com.imranmelikov.folt.databinding.FragmentPhotoBinding
import com.imranmelikov.folt.domain.model.User
import com.imranmelikov.folt.presentation.account.AccountViewModel
import com.imranmelikov.folt.util.Status
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class PhotoFragment : Fragment() {
    private lateinit var binding:FragmentPhotoBinding
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var storage:StorageReference
    private lateinit var fireBaseStorage:FirebaseStorage
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentPhotoBinding.inflate(inflater,container,false)
        accountViewModel= ViewModelProvider(requireActivity())[AccountViewModel::class.java]

        fireBaseStorage=FirebaseStorage.getInstance()
        storage=fireBaseStorage.reference

        getControlArg()
        observeCRUD()
        binding.closeBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun getControlArg(){
        val getArg=arguments?.getSerializable(AccountConstants.user) as? User
        getArg?.let {user->
           pickImage(user)
            deleteImg(user)
        }
    }

    private fun pickImage(user: User){
        // Registers a photo picker activity launcher in single-select mode.
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                binding.deleteProfilePhoto.setOnClickListener {  }
                binding.photoLibrary.setOnClickListener {  }
                binding.closeBtn.setOnClickListener {  }
                requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object :
                    OnBackPressedCallback(true){
                    override fun handleOnBackPressed() {

                    }
                })
                val storageRef = storage
                val imageRef = storageRef.child("Images/${user.email}.jpg")

                val uploadTask = imageRef.putFile(uri)

                uploadTask.addOnSuccessListener { _->
                    imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        user.imageUrl=downloadUri.toString()
                        accountViewModel.updateUser(user)
                        findNavController().popBackStack()
                    }.addOnFailureListener { exception ->
                        Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.d("PhotoPicker", ErrorMsgConstants.noMedia)
            }
        }
        binding.photoLibrary.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun deleteImg(user: User){
        binding.deleteProfilePhoto.setOnClickListener {
            user.imageUrl=""
            accountViewModel.updateUser(user)
            findNavController().popBackStack()
        }
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