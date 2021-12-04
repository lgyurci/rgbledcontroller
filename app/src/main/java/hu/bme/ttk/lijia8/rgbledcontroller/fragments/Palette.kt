package hu.bme.ttk.lijia8.rgbledcontroller.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import hu.bme.ttk.lijia8.rgbledcontroller.MainActivity
import hu.bme.ttk.lijia8.rgbledcontroller.R
import hu.bme.ttk.lijia8.rgbledcontroller.adapter.PaletteRvAdapter
import hu.bme.ttk.lijia8.rgbledcontroller.database.RoomRGB
import hu.bme.ttk.lijia8.rgbledcontroller.databinding.FragmentPaletteBinding
import hu.bme.ttk.lijia8.rgbledcontroller.databinding.FragmentRgbcodeBinding
import hu.bme.ttk.lijia8.rgbledcontroller.singletons.CurrentRGB
import hu.bme.ttk.lijia8.rgbledcontroller.viewmodel.RGBViewModel

class Palette : DialogFragment(),PaletteRvAdapter.OnColorClickedListener {

    private lateinit var binding: FragmentPaletteBinding

    private lateinit var rgbViewModel: RGBViewModel

    private lateinit var adapter: PaletteRvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rgbViewModel = ViewModelProvider(this).get(RGBViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaletteBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        adapter = PaletteRvAdapter()
        adapter.listener = this
        binding.rvPalette.layoutManager = GridLayoutManager(context, 4)
        binding.rvPalette.adapter = adapter
        binding.newPreset.setOnClickListener{
         //   adapter.addColor(CurrentRGB.red,CurrentRGB.green,CurrentRGB.blue)
            rgbViewModel.insert(RoomRGB(0,CurrentRGB.red,CurrentRGB.green,CurrentRGB.blue))
        }

        rgbViewModel.allColors.observe(this,{
            adapter.reloadColors(it)
        })
    }

    override fun onColorClicked(r: Int, g: Int, b: Int) {
        CurrentRGB.setAbsoluteRGB(r,g,b)
        (activity as MainActivity).refreshRGBIndicator()
        (activity as MainActivity).update()
        this.dismiss()
    }

    override fun onColorLongClick(color : RoomRGB): Boolean {
        rgbViewModel.delete(color)
        return true
    }

}