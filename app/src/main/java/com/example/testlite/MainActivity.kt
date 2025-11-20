package com.example.testlite

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.core.view.get
import androidx.core.view.size
import androidx.navigation.findNavController
import com.example.testlite.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val cartViewModel: CartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = navHostFragment.navController

        binding.bottomNavBar.background = null
        binding.bottomNavBar.menu.getItem(1).isEnabled = false
        binding.bottomNavBar.setupWithNavController(navController)

        // Initial state: shrink
        binding.fab.shrink()

        cartViewModel.cartItems.observe(this) { items ->
            updateFabState(navController.currentDestination?.id, items)
        }

        cartViewModel.totalPrice.observe(this) { total ->
             updateFabState(navController.currentDestination?.id, cartViewModel.cartItems.value)
        }

        binding.fab.setOnClickListener {
            when (navController.currentDestination?.id) {
                R.id.cartFragment -> {
                    navController.navigate(R.id.action_cartFragment_to_barcodeScannerFragment)
                }
                R.id.barcodeScannerFragment -> {
                    // Fix: Use popBackStack instead of navigate to avoid loop or stack issues if possible
                    // But since we want to go to Cart, navigate is fine if action exists.
                    navController.navigate(R.id.action_barcodeScannerFragment_to_cartFragment)
                }
                else -> {
                    navController.navigate(R.id.action_global_to_cartFragment)
                }
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateFabState(destination.id, cartViewModel.cartItems.value)
        }
    }

    private fun updateFabState(destinationId: Int?, cartItems: List<CartItem>?) {
        val items = cartItems ?: emptyList()
        val total = items.sumOf { it.subtotal }
        val count = items.sumOf { it.quantity }

        when (destinationId) {
            R.id.barcodeScannerFragment -> {
                // Escáner: ocultar nav, mostrar carrito rojo (shrink)
                binding.bottomNavBar.visibility = View.VISIBLE
                binding.bottomNavBar.menu.findItem(R.id.placeholder)?.isChecked = true
                binding.fab.backgroundTintList = ColorStateList.valueOf(
                    android.graphics.Color.parseColor("#00BFA5")
                )
                
                if (items.isNotEmpty()) {
                    binding.fab.extend()
                    binding.fab.setIconResource(R.drawable.cart_icon)
                    binding.fab.text = "Proceder Venta ($count) - $${String.format("%.2f", total)}"
                } else {
                    binding.fab.shrink()
                    binding.fab.setIconResource(R.drawable.cart_icon)
                }
            }
            R.id.cartFragment -> {
                // Carrito: mostrar nav, scanner verde azulado (shrink)
                binding.bottomNavBar.visibility = View.VISIBLE
                binding.bottomNavBar.menu.findItem(R.id.placeholder)?.isChecked = true
                binding.fab.shrink()
                binding.fab.setIconResource(R.drawable.barcode_scanner)
                binding.fab.backgroundTintList = ColorStateList.valueOf(
                    android.graphics.Color.parseColor("#C2185B")
                )
            }
            else -> {
                // Default
                binding.bottomNavBar.visibility = View.VISIBLE
                binding.fab.backgroundTintList = ColorStateList.valueOf(
                    android.graphics.Color.parseColor("#00BFA5")
                )

                if (items.isNotEmpty()) {
                    binding.fab.extend()
                    binding.fab.setIconResource(R.drawable.cart_icon)
                    binding.fab.text = "Proceder Venta ($count) - $${String.format("%.2f", total)}"
                } else {
                    binding.fab.shrink()
                    binding.fab.setIconResource(R.drawable.cart_icon)
                }
            }
        }
    }

    private fun getColorFromAttr(attrColor: Int) : Int {
        val typedValue = TypedValue()
        theme.resolveAttribute(attrColor, typedValue, true)
        return typedValue.data
    }
}
