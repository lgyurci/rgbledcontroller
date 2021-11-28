package hu.bme.ttk.lijia8.rgbledcontroller

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import hu.bme.ttk.lijia8.rgbledcontroller.databinding.ActivityMainBinding
import hu.bme.ttk.lijia8.rgbledcontroller.fragments.Palette
import hu.bme.ttk.lijia8.rgbledcontroller.fragments.RGBCode
import hu.bme.ttk.lijia8.rgbledcontroller.singletons.CurrentRGB
import kotlin.math.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageView3.setOnClickListener {
            RGBCode().show(supportFragmentManager,null)
        }

        val bitmap = (binding.imageView.drawable as BitmapDrawable).bitmap

        binding.imageView.setOnTouchListener { _, event ->
            var x = event?.x?.toInt() ?: 0
            var y = event?.y?.toInt() ?: 0

            val ra = binding.imageView.width * 0.5
            val d = sqrt(
                (x - (binding.imageView.width * 0.5)).pow(2.0) + (y - (binding.imageView.height * 0.5)).pow(2.0)
            )
            var two = false
            if (d + 5 > ra) {
                val ang =
                    atan2(y - binding.imageView.height * 0.5, x - binding.imageView.width * 0.5)
                x = binding.imageView.width / 2 + ((ra - 4) * cos(ang)).toInt()
                y = binding.imageView.height / 2 + ((ra - 4) * sin(ang)).toInt()
                two = true
            }

            val pixel: Int = bitmap.getPixel(
                (x * (bitmap.width.toDouble() / binding.imageView.width)).toInt(),
                (y * (bitmap.height.toDouble() / binding.imageView.height)).toInt()
            )
            if (Color.red(pixel) != 0 || Color.blue(pixel) != 0 || Color.green(pixel) != 0) {
                var re = Color.red(pixel)
                var gr = Color.green(pixel)
                var bl = Color.blue(pixel)

                if (two) {
                    if (re <= bl && re <= gr) {
                        re = 0
                    }
                    if (bl <= re && bl <= gr) {
                        bl = 0
                    }
                    if (gr <= re && gr <= bl) {
                        gr = 0
                    }
                }
                CurrentRGB.setRGB(re,gr,bl)
            }

            refreshRGBIndicator()
            update()

            //   return view?.onTouchEvent(event) ?: true
            true
        }

        binding.seekBar2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, i: Int, fromUser: Boolean) {
                if (fromUser){
                    update()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

        binding.imageButtoncolor.setOnClickListener{
            Palette().show(supportFragmentManager,null)
        }

        binding.switch1.setOnClickListener{
            update()
        }
    }

    fun refreshRGBIndicator(){
        binding.imageView3.setColorFilter(Color.rgb(CurrentRGB.red,CurrentRGB.green,CurrentRGB.blue))
    }

    fun updateSeekBar(){
        binding.seekBar2.progress = CurrentRGB.brightness
    }
    fun update(){
        var rts = CurrentRGB.bred
        var gts = CurrentRGB.bgreen
        var bts = CurrentRGB.bblue
        if (!binding.switch1.isChecked){
            rts = 0
            gts = 0
            bts = 0
        }
    }

}