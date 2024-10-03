package com.anto.wp_sh.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anto.wp_sh.data.model.SchoolDetails
import com.anto.wp_sh.data.repo.SchoolRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: SchoolRepository) : ViewModel() {

    private val _data = MutableLiveData<SchoolDetails>()
    val data: LiveData<SchoolDetails> get() = _data

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun getSchoolDetails() {
        println("<< Called getSchool Details")
        viewModelScope.launch {
            try {
                val response = repository.fetchData()
                if (response.isSuccessful) {
                    _data.postValue(response.body())
                } else {
                    _error.postValue("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _error.postValue("Exception: ${e.message}")
            }
        }

        println("<<API call issued")
    }

}