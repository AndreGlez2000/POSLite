package com.example.testlite.utils

import android.content.Context
import com.example.testlite.database.relations.TicketWithItems
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class PdfGenerator(private val context: Context) {
    
    fun generateTicketPdf(
        ticketWithItems: TicketWithItems,
        productNames: Map<String, String>
    ): File {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val fileNameFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        
        // Create PDF file in cache directory
        val fileName = "Ticket_${ticketWithItems.ticket.ticketId}_${fileNameFormat.format(Date())}.pdf"
        val file = File(context.cacheDir, fileName)
        
        val writer = PdfWriter(file)
        val pdfDoc = PdfDocument(writer)
        val document = Document(pdfDoc)
        
        try {
            // Header
            document.add(
                Paragraph("TICKET DE VENTA")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(18f)
                    .setBold()
            )
            
            document.add(Paragraph("\n"))
            
            // Ticket info
            document.add(
                Paragraph("Ticket #${ticketWithItems.ticket.ticketId}")
                    .setFontSize(14f)
                    .setBold()
            )
            
            document.add(
                Paragraph("Fecha: ${dateFormat.format(Date(ticketWithItems.ticket.fechaHora))}")
                    .setFontSize(12f)
            )
            
            document.add(Paragraph("\n"))
            document.add(
                Paragraph("PRODUCTOS")
                    .setFontSize(14f)
                    .setBold()
            )
            
            // Products table
            val table = Table(UnitValue.createPercentArray(floatArrayOf(40f, 15f, 20f, 25f)))
                .useAllAvailableWidth()
            
            // Table headers
            table.addHeaderCell("Producto")
            table.addHeaderCell("Cant.")
            table.addHeaderCell("Precio")
            table.addHeaderCell("Subtotal")
            
            // Table rows
            ticketWithItems.items.forEach { item ->
                val productName = productNames[item.skuProductoFk] ?: "Producto desconocido"
                table.addCell(productName)
                table.addCell(item.cantidad.toString())
                table.addCell("$${String.format("%.2f", item.precioEnEseMomento)}")
                table.addCell("$${String.format("%.2f", item.cantidad * item.precioEnEseMomento)}")
            }
            
            document.add(table)
            
            document.add(Paragraph("\n"))
            
            // Total
            document.add(
                Paragraph("TOTAL: $${String.format("%.2f", ticketWithItems.ticket.total)}")
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontSize(16f)
                    .setBold()
            )
            
            document.add(Paragraph("\n"))
            document.add(
                Paragraph("Gracias por su compra")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(12f)
            )
            
        } finally {
            document.close()
        }
        
        return file
    }
}
