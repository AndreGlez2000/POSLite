package com.example.testlite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testlite.database.relations.TicketWithItems
import com.example.testlite.utils.PdfGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class TicketDetailDialogFragment : DialogFragment() {
    
    private val ticketViewModel: TicketViewModel by activityViewModels()
    private val inventoryViewModel: InventoryViewModel by activityViewModels()
    private var ticketId: Int = 0
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ticketId = arguments?.getInt("ticketId") ?: 0
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_ticket_detail, container, false)
    }
    
    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            val width = resources.displayMetrics.widthPixels - (16.dp() * 2)
            setLayout(
                width,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawableResource(android.R.color.transparent)
        }
    }
    
    private fun Int.dp(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val tvTicketId: TextView = view.findViewById(R.id.tv_detail_ticket_id)
        val tvTicketDate: TextView = view.findViewById(R.id.tv_detail_ticket_date)
        val tvTicketTotal: TextView = view.findViewById(R.id.tv_detail_ticket_total)
        val rvItems: RecyclerView = view.findViewById(R.id.rv_ticket_items)
        val btnClose: android.widget.Button = view.findViewById(R.id.btn_close)
        val btnShare: android.widget.Button = view.findViewById(R.id.btn_share)
        
        val adapter = TicketDetailAdapter()
        rvItems.adapter = adapter
        rvItems.layoutManager = LinearLayoutManager(requireContext())
        
        var currentTicketWithItems: TicketWithItems? = null
        var currentProductNames: Map<String, String> = emptyMap()
        
        ticketViewModel.getTicketWithItems(ticketId) { ticketWithItems ->
            if (ticketWithItems != null) {
                currentTicketWithItems = ticketWithItems
                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                
                tvTicketId.text = "Ticket #${ticketWithItems.ticket.ticketId}"
                tvTicketDate.text = dateFormat.format(Date(ticketWithItems.ticket.fechaHora))
                tvTicketTotal.text = "Total: $${String.format("%.2f", ticketWithItems.ticket.total)}"
                
                // Get product names from inventory
                val products = inventoryViewModel.products.value.orEmpty()
                currentProductNames = products.associate { it.sku to it.name }
                
                val itemsWithNames = ticketWithItems.items.map { item ->
                    val product = products.find { it.sku == item.skuProductoFk }
                    TicketDetailItem(
                        productName = product?.name ?: "Producto desconocido",
                        sku = item.skuProductoFk,
                        quantity = item.cantidad,
                        price = item.precioEnEseMomento,
                        subtotal = item.cantidad * item.precioEnEseMomento
                    )
                }
                
                adapter.updateList(itemsWithNames)
            }
        }
        
        btnShare.setOnClickListener {
            currentTicketWithItems?.let { ticket ->
                sharePdf(ticket, currentProductNames)
            }
        }
        
        btnClose.setOnClickListener {
            dismiss()
        }
    }
    
    private fun sharePdf(ticketWithItems: TicketWithItems, productNames: Map<String, String>) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val pdfGenerator = PdfGenerator(requireContext())
                val pdfFile = pdfGenerator.generateTicketPdf(ticketWithItems, productNames)
                
                withContext(Dispatchers.Main) {
                    val uri = FileProvider.getUriForFile(
                        requireContext(),
                        "${requireContext().packageName}.fileprovider",
                        pdfFile
                    )
                    
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "application/pdf"
                        putExtra(Intent.EXTRA_STREAM, uri)
                        putExtra(Intent.EXTRA_SUBJECT, "Ticket #${ticketWithItems.ticket.ticketId}")
                        putExtra(Intent.EXTRA_TEXT, "Ticket de venta")
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    
                    startActivity(Intent.createChooser(shareIntent, "Compartir Ticket"))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Error al generar PDF: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    
    companion object {
        fun newInstance(ticketId: Int): TicketDetailDialogFragment {
            val fragment = TicketDetailDialogFragment()
            val args = Bundle()
            args.putInt("ticketId", ticketId)
            fragment.arguments = args
            return fragment
        }
    }
}

data class TicketDetailItem(
    val productName: String,
    val sku: String,
    val quantity: Int,
    val price: Double,
    val subtotal: Double
)
