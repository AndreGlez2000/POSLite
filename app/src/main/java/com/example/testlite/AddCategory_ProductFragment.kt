package com.example.testlite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

import androidx.fragment.app.DialogFragment

class AddCategory_ProductFragment : DialogFragment() {

    private val args: AddCategory_ProductFragmentArgs by navArgs()
    private val inventoryViewModel: InventoryViewModel by activityViewModels()

    private lateinit var etName: TextInputEditText
    private lateinit var etPrice: TextInputEditText
    private lateinit var etSku: TextInputEditText
    private lateinit var etCategory: android.widget.AutoCompleteTextView
    private lateinit var tilPrice: TextInputLayout
    private lateinit var tilCategory: TextInputLayout
    private lateinit var llSku: View
    private var selectedCategoryId: Int = 0

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_category__product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvTitle = view.findViewById<TextView>(R.id.tv_title)
        etName = view.findViewById(R.id.et_name)
        etPrice = view.findViewById(R.id.et_price)
        etSku = view.findViewById(R.id.et_sku)
        etCategory = view.findViewById(R.id.et_category)
        tilPrice = view.findViewById(R.id.til_price)
        tilCategory = view.findViewById(R.id.til_category)
        llSku = view.findViewById(R.id.ll_sku)
        val btnScan = view.findViewById<ImageButton>(R.id.btn_scan)
        val btnSave = view.findViewById<Button>(R.id.btn_save)

        val mode = args.mode // "category" or "product"
        val categoryId = args.categoryId?.toIntOrNull() ?: 0

        if (mode == "product") {
            tvTitle.text = "Agregar Producto"
            tilPrice.visibility = View.VISIBLE
            tilCategory.visibility = View.VISIBLE
            llSku.visibility = View.VISIBLE
            
            // Populate category dropdown
            inventoryViewModel.categories.observe(viewLifecycleOwner) { categories ->
                val categoryNames = categories.map { it.name }
                val adapter = android.widget.ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    categoryNames
                )
                etCategory.setAdapter(adapter)
                
                // Set default selection if categoryId is provided
                if (categoryId > 0) {
                    val category = categories.find { it.id == categoryId }
                    category?.let {
                        etCategory.setText(it.name, false)
                        selectedCategoryId = it.id
                    }
                }
            }
            
            // Handle category selection
            etCategory.setOnItemClickListener { _, _, position, _ ->
                val categories = inventoryViewModel.categories.value ?: emptyList()
                if (position < categories.size) {
                    selectedCategoryId = categories[position].id
                }
            }
        } else {
            tvTitle.text = "Agregar Categoría"
            tilPrice.visibility = View.GONE
            tilCategory.visibility = View.GONE
            llSku.visibility = View.GONE
        }

        btnScan.setOnClickListener {
            // Use ID directly or updated SafeArgs if available. Using ID for safety since SafeArgs might not be regenerated yet in my mind.
            // Actually, I'll use the ID resource to be safe.
            findNavController().navigate(R.id.action_addCategoryProductFragment_to_barcodeScannerPick)
        }

        setFragmentResultListener("requestKey") { _, bundle ->
            val result = bundle.getString("bundleKey")
            etSku.setText(result)
        }

        btnSave.setOnClickListener {
            val name = etName.text.toString()
            if (name.isBlank()) {
                etName.error = "Requerido"
                return@setOnClickListener
            }

            if (mode == "product") {
                val priceStr = etPrice.text.toString()
                val sku = etSku.text.toString()
                val categoryText = etCategory.text.toString()

                if (priceStr.isBlank()) {
                    etPrice.error = "Requerido"
                    return@setOnClickListener
                }
                if (sku.isBlank()) {
                    etSku.error = "Requerido"
                    return@setOnClickListener
                }
                if (categoryText.isBlank()) {
                    etCategory.error = "Requerido"
                    return@setOnClickListener
                }

                val price = priceStr.toDoubleOrNull()
                if (price == null) {
                    etPrice.error = "Inválido"
                    return@setOnClickListener
                }
                
                if (selectedCategoryId == 0) {
                    etCategory.error = "Selecciona una categoría"
                    return@setOnClickListener
                }

                inventoryViewModel.addProduct(name, price, sku, selectedCategoryId)
                Toast.makeText(context, "Producto agregado", Toast.LENGTH_SHORT).show()
            } else {
                inventoryViewModel.addCategory(name)
                Toast.makeText(context, "Categoría agregada", Toast.LENGTH_SHORT).show()
            }

            findNavController().popBackStack()
        }
    }
}