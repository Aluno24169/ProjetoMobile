package com.example.ProjetoMobileStocks

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ProjetoMobileStocks.databinding.ActivityMainBinding

/**
 * Atividade principal da aplicação responsável pelo ciclo de vida global
 * e pela configuração do sistema de navegação por separadores (BottomNavigationView).
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Oculta a barra de ação superior padrão
        supportActionBar?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // Define os destinos de nível superior da aplicação
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_news
            )
        )
        if (supportActionBar != null) {
            setupActionBarWithNavController(navController, appBarConfiguration)
        }
        navView.setupWithNavController(navController)
    }

    /**
     * Lógica para gerir o retrocesso de ecrãs na pilha de navegação.
     */
    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        val desiredTag = "SimpleFragment"
        val fragment = fragmentManager.findFragmentByTag(desiredTag)

        if (fragment != null) {
            fragmentManager.popBackStack(desiredTag, 0)
        } else {
            super.onBackPressed()
        }
    }
}
