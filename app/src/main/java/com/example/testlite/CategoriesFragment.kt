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
    private lateinit var adapter: CategoryAdapter
    private val inventoryViewModel: InventoryViewModel by activityViewModels()

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
        btnBack.visibility = View.GONE

        adapter = CategoryAdapter(emptyList()) { category ->
            val action = CategoriesFragmentDirections.actionCategoriesFragmentToProductsFragment(category.id.toString())
            findNavController().navigate(action)
        }
        recyclerView.adapter = adapter

        inventoryViewModel.categories.observe(viewLifecycleOwner) { categories ->
            adapter.updateList(categories)
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