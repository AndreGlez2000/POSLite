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
        dialog?.window?.apply {
            val width = resources.displayMetrics.widthPixels - (32.dp() * 2)
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
        val tilName = view.findViewById<TextInputLayout>(R.id.til_name)
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
            findNavController().navigate(R.id.action_addCategoryProductFragment_to_barcodeScannerPick)
        }

        setFragmentResultListener("requestKey") { _, bundle ->
            val result = bundle.getString("bundleKey")
            etSku.setText(result)
        }

        btnSave.setOnClickListener {
            val tilName = view.findViewById<TextInputLayout>(R.id.til_name)
            val tilSku = view.findViewById<TextInputLayout>(R.id.til_sku)
            
            val name = etName.text.toString()
            if (name.isBlank()) {
                tilName.error = "Campo requerido"
                return@setOnClickListener
            } else {
                tilName.error = null
            }

            if (mode == "product") {
                val priceStr = etPrice.text.toString()
                val sku = etSku.text.toString()
                val categoryText = etCategory.text.toString()

                if (priceStr.isBlank()) {
                    tilPrice.error = "Campo requerido"
                    return@setOnClickListener
                } else {
                    tilPrice.error = null
                }
                
                if (sku.isBlank()) {
                    tilSku.error = "Campo requerido"
                    return@setOnClickListener
                }
                
                if (categoryText.isBlank()) {
                    tilCategory.error = "Campo requerido"
                    return@setOnClickListener
                } else {
                    tilCategory.error = null
                }

                val price = priceStr.toDoubleOrNull()
                if (price == null) {
                    tilPrice.error = "Precio inválido"
                    return@setOnClickListener
                }
                
                if (selectedCategoryId == 0) {
                    tilCategory.error = "Selecciona una categoría"
                    return@setOnClickListener
                }
                
                // Validate SKU is unique
                val existingProducts = inventoryViewModel.products.value ?: emptyList()
                val skuExists = existingProducts.any { it.sku == sku }
                
                if (skuExists) {
                    tilSku.error = "Este SKU ya existe en la base de datos"
                    Toast.makeText(
                        context,
                        "Ya existe un producto con el SKU: $sku",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                } else {
                    tilSku.error = null
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