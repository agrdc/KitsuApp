package com.example.kitsuapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


abstract class SingleEventViewModel<T> : ViewModel() {
    private val singleEventChannel = Channel<T>()
    val singleEventFlow = singleEventChannel.receiveAsFlow()

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            singleEventChannel.close()
        }
    }
}