package hu.bme.ttk.lijia8.rgbledcontroller.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import hu.bme.ttk.lijia8.rgbledcontroller.MainActivity
import hu.bme.ttk.lijia8.rgbledcontroller.R
import hu.bme.ttk.lijia8.rgbledcontroller.adapter.PaletteRvAdapter
import hu.bme.ttk.lijia8.rgbledcontroller.databinding.FragmentPaletteBinding
import hu.bme.ttk.lijia8.rgbledcontroller.databinding.FragmentRgbcodeBinding
import hu.bme.ttk.lijia8.rgbledcontroller.singletons.CurrentRGB

class Palette : DialogFragment(),PaletteRvAdapter.OnColorClickedListener {

    private lateinit var binding: FragmentPaletteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaletteBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        val adapter = PaletteRvAdapter()
        adapter.listener = this
        binding.rvPalette.layoutManager = GridLayoutManager(context, 4)
        binding.rvPalette.adapter = adapter
    }

    override fun onColorClicked(r: Int, g: Int, b: Int) {
        CurrentRGB.setRGB(r,g,b)
        (activity as MainActivity).refreshRGBIndicator()
        (activity as MainActivity).update()
        this.dismiss()
    }

}