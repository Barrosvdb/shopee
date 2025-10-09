package com.example.shopee

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.example.shopee.databinding.ActivityMainBinding

// Certifique-se de que todas as classes de Fragmento existam.

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var fragmentManager: FragmentManager
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicialização e Fragmento Padrão
        fragmentManager = supportFragmentManager
        if (savedInstanceState == null) {
            openFragment(HomeFragment())
        }

        // Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        // Oculta a status bar
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView)
            .hide(WindowInsetsCompat.Type.statusBars())

        // Configuração do DrawerLayout e do Toggle
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.nav_open,
            R.string.nav_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // NavigationView (Menu Lateral)
        binding.navigationDrawer.setNavigationItemSelectedListener(this)

        // Configura a navegação Bottom e FAB
        setupNavigationListeners()

        // Trata o botão "Voltar"
        handleBackPress()
    }

    private fun setupNavigationListeners() {
        // CORREÇÃO: O bottom_menu.xml JÁ usa bottom_home, bottom_cart, bottom_profile e bottom_menu.
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> openFragment(HomeFragment())
                R.id.bottom_cart -> openFragment(CartFragment())
                R.id.bottom_profile -> openFragment(ProfileFragment())
                R.id.bottom_menu -> openFragment(MenuFragment())
                else -> false
            }
            true
        }

        // Clique no botão flutuante (FAB)
        binding.fab.setOnClickListener {
            Toast.makeText(this, "Categorias", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleBackPress() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                } else if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                } else {
                    finish()
                }
            }
        })
    }

    // Implementação da interface de navegação do Drawer (NavigationView)
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // CORREÇÃO: Mapeando os IDs do seu nav_menu.xml para os Fragments/Toasts
        when (item.itemId) {
            // Mapeando os novos IDs do XML para abrir Fragments
            R.id.nav_carro -> openFragment(CartFragment()) // Exemplo de mapeamento
            R.id.nav_comida -> openFragment(MenuFragment()) // Exemplo de mapeamento
            R.id.nav_brinquedo -> openFragment(PrimeFragment()) // Exemplo de mapeamento
            R.id.nav_eletronicos -> openFragment(ElectronicsFragment())
            R.id.nav_games -> openFragment(FashionFragment()) // Exemplo de mapeamento
            R.id.nav_domesticos -> openFragment(HomeFragment()) // Exemplo de mapeamento

            // As referências antigas (nav_prime, nav_fashion, etc.) foram removidas
            // e substituídas pelos seus IDs reais (nav_carro, nav_comida, etc.)
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
    }

}