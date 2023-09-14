package com.example.dessertclicker.ui

import androidx.lifecycle.ViewModel
import com.example.dessertclicker.data.DessertUiState
import com.example.dessertclicker.data.Datasource.dessertList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class DessertViewModel : ViewModel() {
    private val _dessertUiState = MutableStateFlow(DessertUiState())
    val dessertUiState: StateFlow<DessertUiState> = _dessertUiState.asStateFlow()

    private fun determineDessertIndex (
        dessertsSold: Int
    ): Int {
        var dessertIndex = 0
        for (i in dessertList.indices) {
            if (dessertsSold >= dessertList[i].startProductionAmount) {
                dessertIndex = i
            } else {
                // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
                // you'll start producing more expensive desserts as determined by startProductionAmount
                // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
                // than the amount sold.
                break
            }
        }
        return dessertIndex
    }

    fun onDessertClicked() {
        _dessertUiState.update { currentState ->
            val dessertSold = currentState.dessertsSold + 1
            val nextIndex = determineDessertIndex(dessertSold)
            currentState.copy(
                revenue = currentState.revenue + currentState.currentDessertPrice,
                dessertsSold = dessertSold,
                currentDessertIndex = nextIndex,
                currentDessertPrice = dessertList[nextIndex].price,
                currentDessertImageId = dessertList[nextIndex].imageId
            )
        }
    }
}