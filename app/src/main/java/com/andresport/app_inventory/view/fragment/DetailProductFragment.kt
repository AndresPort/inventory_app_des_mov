package com.andresport.app_inventory.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.andresport.app_inventory.R
import com.andresport.app_inventory.databinding.FragmentProductDetailBinding
import com.andresport.app_inventory.viewmodel.ProductViewModel
import androidx.lifecycle.ViewModelProvider
import com.andresport.app_inventory.repository.ProductRepository
import com.andresport.app_inventory.data.AppDatabase
import com.andresport.app_inventory.viewmodel.ViewModelFactory

class DetailProductFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)

        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Inicializar ViewModel con tu repositorio y base de datos
        val dao = AppDatabase.getInstance(requireContext()).productDao()
        val repository = ProductRepository(dao)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(repository)
        )[ProductViewModel::class.java]

        val productRef = arguments?.getString("productRef")
        productRef?.let {
            viewModel.loadProductByRef(it)
        }
        // Observar los datos
        viewModel.selectedProduct.observe(viewLifecycleOwner) { product ->
            binding.txtProductName.text = product.productName
            binding.txtUnitPrice.text = String.format("$ %, .2f", product.unitPrice)
            binding.txtStock.text = product.stock.toString()
            binding.txtTotal.text = String.format("$ %, .2f", product.total)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}