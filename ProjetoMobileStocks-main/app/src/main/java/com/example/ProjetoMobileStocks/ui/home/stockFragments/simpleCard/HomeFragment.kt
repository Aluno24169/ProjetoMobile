package com.example.ProjetoMobileStocks.ui.home.stockFragments.simpleCard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.ProjetoMobileStocks.R
import com.example.ProjetoMobileStocks.databinding.FragmentHomeBinding

/**
 * Fragmento contentor principal da secção do mercado financeiro, responsável por alojar
 * e gerir as transições entre a vista da carteira, a vista de seleção e os detalhes da ação.
 */
class HomeFragment : Fragment() {
    val simpleFragment = SimpleCardFragment(mutableListOf(), false)
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("HomeFragment", "Fragmento Home - onCreateView()")

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        if (childFragmentManager.findFragmentById(R.id.display_fragment) == null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.display_fragment, simpleFragment)
                .commit()
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura o tratamento personalizado do evento de retrocesso
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentFragment = childFragmentManager.findFragmentById(R.id.display_fragment)
                Log.d("HomeFragment", "Fragmento atual na pilha: $currentFragment")
                if (currentFragment is SimpleCardFragment) {
                    isEnabled = false
                    return
                }

                if (childFragmentManager.backStackEntryCount > 0) {
                    childFragmentManager.popBackStack()
                } else {
                    isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d("HomeFragment", "Fragmento Home - onDestroyView()")
    }
}