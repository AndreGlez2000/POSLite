package com.example.testlite

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
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
import androidx.core.graphics.toColorInt

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val cartViewModel: CartViewModel by viewModels()
    private lateinit var navController: androidx.navigation.NavController
    
    private var topInset = 0
    private var bottomInset = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Global dark status bar icons
        val windowInsetsController = androidx.core.view.WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = true

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            topInset = systemBars.top
            bottomInset = systemBars.bottom
            
            // Apply insets based on current destination (if available) or default
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
            val nc = navHostFragment?.navController
            val currentId = nc?.currentDestination?.id
            
            if (currentId == R.id.cartFragment) {
                v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            } else {
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            }
            insets
        }


        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController

        binding.bottomNavBar.background = null
        binding.bottomNavBar.menu.getItem(1).isEnabled = false
        binding.bottomNavBar.setupWithNavController(navController)

        // Initial state: shrink
        binding.fab.shrink()

        val isTablet = resources.getBoolean(R.bool.is_tablet)

        if (isTablet) {
            // Tablet Logic
            // 1. Add CartFragment to the right container
            if (savedInstanceState == null) {
                val cartFragment = CartFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.cart_fragment_container, cartFragment)
                    .commit()
            }

            // 2. Sidebar Navigation Logic
            val btnMenu = findViewById<View>(R.id.btn_sidebar_menu)
            val btnReceipts = findViewById<View>(R.id.btn_sidebar_receipts)

            btnMenu?.setOnClickListener {
                // Simple navigation to Categories
                navController.navigate(R.id.categoriesFragment)
            }

            btnReceipts?.setOnClickListener {
                // Navigate to Receipts
                navController.navigate(R.id.receiptFragment)
            }

            // 3. Configure FAB for Scan (Tablet Mode) - static, always opens scanner
            binding.fab.setOnClickListener {
                navController.navigate(R.id.action_global_to_barcodeScannerFragment)
            }

        } else {
            // Phone Logic (Existing)
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
                        navController.navigate(R.id.action_barcodeScannerFragment_to_cartFragment)
                    }
                    else -> {
                        navController.navigate(R.id.action_global_to_cartFragment)
                    }
                }
            }

            navController.addOnDestinationChangedListener { _, destination, _ ->
                updateFabState(destination.id, cartViewModel.cartItems.value)
                
                // Update padding dynamically
                val mainView = findViewById<View>(R.id.main)
                if (destination.id == R.id.cartFragment) {
                    mainView.setPadding(mainView.paddingLeft, 0, mainView.paddingRight, bottomInset)
                } else {
                    mainView.setPadding(mainView.paddingLeft, topInset, mainView.paddingRight, bottomInset)
                }
            }
        }
    }

    private fun updateFabState(destinationId: Int?, cartItems: List<CartItem>?) {
        // Tablet early return - no dynamic FAB behavior
        if (resources.getBoolean(R.bool.is_tablet)) return

        // Phone Logic - ORIGINAL BEHAVIOR RESTORED
        val items = cartItems ?: emptyList()
        val total = items.sumOf { it.subtotal }
        val count = items.sumOf { it.quantity }

        when (destinationId) {
            R.id.barcodeScannerFragment -> {
                // Scanner: show nav bar, show cart button (green, extended or shrink)
                binding.bottomNavBar.visibility = View.VISIBLE
                binding.bottomNavBar.menu.findItem(R.id.placeholder)?.isChecked = true
                binding.fab.backgroundTintList = android.content.res.ColorStateList.valueOf(
                    android.graphics.Color.parseColor("#00BFA5")
                )
                
                window.statusBarColor = ContextCompat.getColor(this, R.color.white)

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
                // Cart: show nav bar, scanner button (pink, shrink)
                binding.bottomNavBar.visibility = View.VISIBLE
                binding.bottomNavBar.menu.findItem(R.id.placeholder)?.isChecked = true
                binding.fab.shrink()
                binding.fab.setIconResource(R.drawable.barcode_scanner)
                binding.fab.backgroundTintList = android.content.res.ColorStateList.valueOf(
                    android.graphics.Color.parseColor("#666666")
                )
                
                window.addFlags(android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(this, R.color.cart_status_bar_color)
            }
            else -> {
                // Default (Categories, Products, etc): show nav bar, cart button
                binding.bottomNavBar.visibility = View.VISIBLE
                binding.fab.backgroundTintList = android.content.res.ColorStateList.valueOf(
                    android.graphics.Color.parseColor("#00BFA5")
                )
                
                window.statusBarColor = ContextCompat.getColor(this, R.color.white)

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
