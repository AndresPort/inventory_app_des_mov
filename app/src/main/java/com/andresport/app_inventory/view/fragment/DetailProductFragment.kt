package com.andresport.app_inventory.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.andresport.app_inventory.R
import com.andresport.app_inventory.databinding.FragmentProductDetailBinding

class DetailProductFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)

        // Configurar Toolbar
        binding.toolbarDetalle.setNavigationOnClickListener {
            // Navegar a HU 3.0: Home Inventario
            findNavController().navigate(R.id.action_productDetailFragment_to_inventarioFragment)
        }

        // Opcional: si usas título dinámico
        binding.toolbarTitle.text = getString(R.string.product_detail_title)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}