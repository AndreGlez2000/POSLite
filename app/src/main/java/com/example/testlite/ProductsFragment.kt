package com.example.testlite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProductsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private val inventoryViewModel: InventoryViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels()
    private val args: ProductsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view_products)
        val fabAdd = view.findViewById<FloatingActionButton>(R.id.add_btn)
        val speedDialContainer = view.findViewById<View>(R.id.speed_dial_container)
        val fabAddProduct = view.findViewById<FloatingActionButton>(R.id.fab_add_product)
        val fabAddCategory = view.findViewById<FloatingActionButton>(R.id.fab_add_category)
        val btnBack = view.findViewById<android.widget.ImageButton>(R.id.btn_back)
        val searchView = view.findViewById<androidx.appcompat.widget.SearchView>(R.id.search_bar)

        btnBack.visibility = View.VISIBLE
        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        adapter = ProductAdapter(
            items = emptyList(),
            onItemClick = { product ->
                cartViewModel.addToCart(product)
            },
            onDeleteClick = { product ->
                android.app.AlertDialog.Builder(requireContext())
                    .setTitle("Eliminar Producto")
                    .setMessage("¿Estás seguro de que deseas eliminar '${product.name}'?")
                    .setPositiveButton("Sí") { _, _ ->
                        inventoryViewModel.deleteProduct(
                            sku = product.sku,
                            onSuccess = {
                                android.widget.Toast.makeText(requireContext(), "Producto eliminado", android.widget.Toast.LENGTH_SHORT).show()
                            },
                            onError = { error ->
                                android.widget.Toast.makeText(requireContext(), error, android.widget.Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                    .setNegativeButton("No", null)
                    .show()
            }
        )
        recyclerView.adapter = adapter

        val categoryId = args.categoryId.toIntOrNull() ?: 0

        inventoryViewModel.products.observe(viewLifecycleOwner) { products ->
            val filteredProducts = products.filter { it.categoryId == categoryId }
            adapter.updateList(filteredProducts)
            
            searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    val query = newText?.lowercase() ?: ""
                    val searchResults = filteredProducts.filter { 
                        it.name.lowercase().contains(query) || it.sku.lowercase().contains(query)
                    }
                    adapter.updateList(searchResults)
                    return true
                }
            })
        }

        fabAdd.setOnClickListener {
            if (speedDialContainer.visibility == View.VISIBLE) {
                speedDialContainer.visibility = View.GONE
                fabAdd.setImageResource(android.R.drawable.ic_input_add)
            } else {
                speedDialContainer.visibility = View.VISIBLE
                fabAdd.setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
            }
        }

        fabAddProduct.setOnClickListener {
            speedDialContainer.visibility = View.GONE
            fabAdd.setImageResource(android.R.drawable.ic_input_add)
            val action = ProductsFragmentDirections.actionGlobalToAddCategoryProductFragment("product", categoryId.toString())
            findNavController().navigate(action)
        }

        fabAddCategory.setOnClickListener {
            speedDialContainer.visibility = View.GONE
            fabAdd.setImageResource(android.R.drawable.ic_input_add)
            val action = ProductsFragmentDirections.actionGlobalToAddCategoryProductFragment("category")
            findNavController().navigate(action)
        }
    }
}