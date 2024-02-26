package com.imranmelikov.folt.presentation.account

import androidx.lifecycle.ViewModel
import com.imranmelikov.folt.domain.repository.FoltRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(private val repository: FoltRepository):ViewModel() {

}