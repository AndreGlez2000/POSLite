package com.example.testlite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CategoriesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_categories)

        // Datos de prueba
        val categories = listOf(
            "Papas",
            "Sodas",
            "Paletas",
            "Dulces",
            "Galletas",
            "Chocolates",
            "Jugos",
            "Agua",
            "Cerveza",
            "Vinos",
            "Licores",
            "Cigarros",
            "Botanas",
            "Pan",
            "Lácteos"
        )

        // Configurar adapter
        adapter = CategoryAdapter(categories) { categoryName ->
            // Click listener (puedes agregar funcionalidad aquí)
        }

        recyclerView.adapter = adapter
    }

}