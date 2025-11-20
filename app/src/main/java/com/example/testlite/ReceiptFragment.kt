package com.example.testlite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ReceiptFragment : Fragment() {
    
    private val ticketViewModel: TicketViewModel by activityViewModels()
    private lateinit var adapter: TicketAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_receipt, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val rvTickets: RecyclerView = view.findViewById(R.id.rv_tickets)
        
        // Setup RecyclerView
        adapter = TicketAdapter()
        rvTickets.adapter = adapter
        rvTickets.layoutManager = LinearLayoutManager(requireContext())
        
        // Observe tickets
        ticketViewModel.allTickets.observe(viewLifecycleOwner) { tickets ->
            adapter.updateList(tickets)
        }
    }
}