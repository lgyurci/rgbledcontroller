package hu.bme.ttk.lijia8.rgbledcontroller.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import hu.bme.ttk.lijia8.rgbledcontroller.MainActivity
import hu.bme.ttk.lijia8.rgbledcontroller.databinding.FragmentRgbcodeBinding
import hu.bme.ttk.lijia8.rgbledcontroller.singletons.CurrentRGB

class RGBCode : DialogFragment() {
    private lateinit var binding: FragmentRgbcodeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRgbcodeBinding.inflate(inflater)

        binding.boc.setOnClickListener{
            this.dismiss()
        }
        binding.bok.setOnClickListener{
            CurrentRGB.setRGB(binding.er.text.toString().toInt(),binding.eg.text.toString().toInt(),binding.eb.text.toString().toInt())
            (activity as MainActivity).refreshRGBIndicator()
            (activity as MainActivity).updateSeekBar()
            (activity as MainActivity).update()
            this.dismiss()
        }
        binding.er.setText(CurrentRGB.bred.toString())
        binding.eg.setText(CurrentRGB.bgreen.toString())
        binding.eb.setText(CurrentRGB.bblue.toString())
        return binding.root
    }

}