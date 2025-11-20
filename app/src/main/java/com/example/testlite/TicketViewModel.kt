package com.example.testlite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.testlite.database.AppDatabase
import com.example.testlite.database.entities.TicketEntity
import com.example.testlite.repository.TicketRepository
import kotlinx.coroutines.launch

class TicketViewModel(application: Application) : AndroidViewModel(application) {
    private val ticketRepository: TicketRepository
    val allTickets: LiveData<List<TicketEntity>>
    
    init {
        val database = AppDatabase.getDatabase(application)
        ticketRepository = TicketRepository(database.ticketDao())
        allTickets = ticketRepository.allTickets.asLiveData()
    }
    
    fun completeSale(cartItems: List<Product>, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (cartItems.isEmpty()) {
            onError("El carrito está vacío")
            return
        }
        
        viewModelScope.launch {
            try {
                ticketRepository.createTicket(cartItems)
                onSuccess()
            } catch (e: Exception) {
                onError("Error al guardar la venta: ${e.message}")
            }
        }
    }
}
