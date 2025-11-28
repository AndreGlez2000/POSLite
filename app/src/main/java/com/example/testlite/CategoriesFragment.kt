package com.example.testlite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CategoriesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter
    private val inventoryViewModel: InventoryViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels()
    
    private var allProducts: List<Product> = emptyList()
    private var allCategories: List<Category> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view_categories)
        val fabAdd = view.findViewById<FloatingActionButton>(R.id.add_btn)
        val speedDialContainer = view.findViewById<View>(R.id.speed_dial_container)
        val fabAddProduct = view.findViewById<FloatingActionButton>(R.id.fab_add_product)
        val fabAddCategory = view.findViewById<FloatingActionButton>(R.id.fab_add_category)
        val btnBack = view.findViewById<android.widget.ImageButton>(R.id.btn_back)
        val searchView = view.findViewById<androidx.appcompat.widget.SearchView>(R.id.search_bar)
        
        btnBack.visibility = View.GONE

        // Initialize Category Adapter
        categoryAdapter = CategoryAdapter(
            items = emptyList(),
            onItemClick = { category ->
                val action = CategoriesFragmentDirections.actionCategoriesFragmentToProductsFragment(category.id.toString())
                findNavController().navigate(action)
            },
            onDeleteClick = { category ->
                // Primero obtenemos el conteo de productos
                inventoryViewModel.getProductCountByCategory(category.id) { productCount ->
                    val message = if (productCount > 0) {
                        "¿Estás seguro de que deseas eliminar '${category.name}'?\n\n" +
                        "Esta acción también eliminará $productCount producto(s) asociado(s)."
                    } else {
                        "¿Estás seguro de que deseas eliminar '${category.name}'?"
                    }
                    
                    android.app.AlertDialog.Builder(requireContext())
                        .setTitle("Eliminar Categoría")
                        .setMessage(message)
                        .setPositiveButton("Sí") { _, _ ->
                            inventoryViewModel.deleteCategory(
                                id = category.id,
                                onSuccess = {
                                    val successMessage = if (productCount > 0) {
                                        "Categoría y $productCount producto(s) eliminado(s)"
                                    } else {
                                        "Categoría eliminada"
                                    }
                                    android.widget.Toast.makeText(requireContext(), successMessage, android.widget.Toast.LENGTH_SHORT).show()
                                },
                                onError = { error ->
                                    android.widget.Toast.makeText(requireContext(), error, android.widget.Toast.LENGTH_LONG).show()
                                }
                            )
                        }
                        .setNegativeButton("No", null)
                        .show()
                }
            }
        )

        // Initialize Product Adapter (for search)
        productAdapter = ProductAdapter(
            items = emptyList(),
            onItemClick = { product ->
                cartViewModel.addToCart(product)
                android.widget.Toast.makeText(requireContext(), "${product.name} agregado al carrito", android.widget.Toast.LENGTH_SHORT).show()
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

        recyclerView.adapter = categoryAdapter

        inventoryViewModel.categories.observe(viewLifecycleOwner) { categories ->
            allCategories = categories
            if (recyclerView.adapter == categoryAdapter) {
                categoryAdapter.updateList(categories)
            }
        }

        inventoryViewModel.products.observe(viewLifecycleOwner) { products ->
            allProducts = products
        }
        
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val query = newText?.lowercase()?.trim() ?: ""
                
                if (query.isEmpty()) {
                    if (recyclerView.adapter != categoryAdapter) {
                        recyclerView.adapter = categoryAdapter
                    }
                    categoryAdapter.updateList(allCategories)
                } else {
                    if (recyclerView.adapter != productAdapter) {
                        recyclerView.adapter = productAdapter
                    }
                    val searchResults = allProducts.filter { 
                        it.name.lowercase().contains(query) || it.sku.lowercase().contains(query)
                    }
                    productAdapter.updateList(searchResults)
                }
                return true
            }
        })

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
            val action = CategoriesFragmentDirections.actionGlobalToAddCategoryProductFragment("product")
            findNavController().navigate(action)
        }

        fabAddCategory.setOnClickListener {
            speedDialContainer.visibility = View.GONE
            fabAdd.setImageResource(android.R.drawable.ic_input_add)
            val action = CategoriesFragmentDirections.actionGlobalToAddCategoryProductFragment("category")
            findNavController().navigate(action)
        }
    }
}