package com.andresport.app_inventory.view.fragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.andresport.app_inventory.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class addProductFragment : Fragment() {

    companion object {
        fun newInstance() = addProductFragment()
    }


    private lateinit var returnIc: ImageView

    private val viewModel: AddProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_add_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✅ Aquí ya existe la vista, por eso puedes acceder a los elementos
        returnIc = view.findViewById(R.id.returnIc)
        returnIc.setOnClickListener {
            returnInventoryPage()
        }
    }


    fun returnInventoryPage(){
        findNavController().navigate(R.id.action_addProductFragment_to_inventarioFragment)
    }
}