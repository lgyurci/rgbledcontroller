package hu.bme.ttk.lijia8.rgbledcontroller

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.preference.PreferenceManager
import hu.bme.ttk.lijia8.rgbledcontroller.databinding.ActivityMainBinding
import hu.bme.ttk.lijia8.rgbledcontroller.fragments.MainActivityPreferences
import hu.bme.ttk.lijia8.rgbledcontroller.fragments.Palette
import hu.bme.ttk.lijia8.rgbledcontroller.fragments.RGBCode
import hu.bme.ttk.lijia8.rgbledcontroller.network.ArduinoNetworkAPI
import hu.bme.ttk.lijia8.rgbledcontroller.notification.NotificationHelper
import hu.bme.ttk.lijia8.rgbledcontroller.singletons.CurrentRGB
import hu.bme.ttk.lijia8.rgbledcontroller.viewmodel.RGBViewModel
import kotlin.math.*
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Volatile
    var asyncRunning = false

    private val api : ArduinoNetworkAPI = ArduinoNetworkAPI(this)

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     //   preferenceManager.findPreference<EditTextPreference>("ip")?.text.toString()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NotificationHelper.createNotificationChannels(this)

        binding.imageButtonLights.setOnClickListener{
            if (!asyncRunning){
                async(CurrentRGB.bred,CurrentRGB.bgreen,CurrentRGB.bblue) { api.switchRelay() }
            }
        }

        binding.imageView3.setOnClickListener {
            RGBCode().show(supportFragmentManager,null)
        }

        val camb = PreferenceManager.getDefaultSharedPreferences( this ).getBoolean("cambutton",true) //.edit().putString("otherKey","value").commit();
        if (camb){
            binding.imageButtoncam.visibility = View.GONE
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
                CurrentRGB.setAbsoluteRGB(re,gr,bl)
            }

            refreshRGBIndicator()
            update()

            //   return view?.onTouchEvent(event) ?: true
            true
        }

        binding.seekBar2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, i: Int, fromUser: Boolean) {
                CurrentRGB.brightness = binding.seekBar2.progress
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
            if (!binding.switch1.isChecked) {
                with(NotificationManagerCompat.from(this.applicationContext)) {
                    cancelAll()
                }
            }
        }

        binding.imageButtoncam.setOnClickListener{
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePictureIntent, 101)
        }

        asyncFetch{api.fetchCurrentRGB()}

        turnOffLedsByNotification(intent)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == 101) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap ?: return
            val pixelcount = imageBitmap.height*imageBitmap.width
            var redsum = 0
            var greensum = 0
            var bluesum = 0
            for (i: Int in 0 until imageBitmap.height){
                for (j: Int in 0 until imageBitmap.width){
                    redsum += imageBitmap.getPixel(j,i).red
                    greensum += imageBitmap.getPixel(j,i).green
                    bluesum += imageBitmap.getPixel(j,i).blue
                }
            }
            val red = (redsum.toDouble()/pixelcount).toInt()
            val green = (greensum.toDouble()/pixelcount).toInt()
            val blue = (bluesum.toDouble()/pixelcount).toInt()
            val brightness = CurrentRGB.brightness
            CurrentRGB.setRGB(red,green,blue)
            CurrentRGB.brightness = brightness
            refreshRGBIndicator()
            update()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_settings){
            val intent = Intent(this, MainSettingsActivity::class.java)
            startActivity(intent)
        }
        if (item.itemId == R.id.item_clear_presets){
            val model = RGBViewModel()
            model.deleteAll()
        }
        return super.onOptionsItemSelected(item)
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
        Log.d("cupdate","r:$rts b:$bts g:$gts brightness: ${CurrentRGB.brightness}")
        if (!binding.switch1.isChecked){
            rts = 0
            gts = 0
            bts = 0
        }
        if (!asyncRunning){
            async(rts,gts,bts) { api.sendRGB(rts,gts,bts) }
        }
    }

    private fun handleNetworkReturn(rcode: Int){

    }

    private fun async(r:Int,g:Int,b:Int,call: () -> Int) {
        asyncRunning = true
        Thread {
            val ret = try {
                call()
            } catch (e: Exception){
                Log.d("axis",e.message?:"none")
            }
            runOnUiThread {
                handleNetworkReturn(ret)
                asyncRunning = false
                if (!binding.switch1.isChecked){
                    if (r != 0 || b!= 0 || g != 0) {
                        Log.d("network", "Critical packet miss, re-sending current settings. Last settings sent | Current settings: r:$r|${CurrentRGB.bred} g:$g|${CurrentRGB.bgreen} b:$b|${CurrentRGB.bblue}")
                        update()
                    }
                } else {
                    if (r != CurrentRGB.bred || g != CurrentRGB.bgreen || b != CurrentRGB.bblue){
                        Log.d("network", "Packet miss, re-sending current settings. Last settings sent | Current settings: r:$r|${CurrentRGB.bred} g:$g|${CurrentRGB.bgreen} b:$b|${CurrentRGB.bblue}")
                        update()
                    }
                }
            }
        }.start()
    }

    private fun asyncFetch(call: () -> Int) {
        Thread {
            var ret = 0
            try {
                ret = call()
                runOnUiThread {
                    if (CurrentRGB.brightness != 0){
                        binding.switch1.isChecked = true
                    } else {
                        CurrentRGB.brightness = 100
                        binding.switch1.isChecked = false
                    }
                    refreshRGBIndicator()
                    updateSeekBar()
                }
            } catch (e: Exception){
                1
            }
            handleNetworkReturn(ret)
        }.start()
    }

    private fun turnOffLedsByNotification(intent: Intent) {
        if (intent.action == NotificationHelper.ACTION_SHOW_LEDS_ACTIVE) {
            binding.switch1.isChecked = false
            with(NotificationManagerCompat.from(this.applicationContext)) {
                cancelAll()
            }
            update()
            this.finishAffinity()
        }
    }

    override fun onPause() {
        super.onPause()
        if (binding.switch1.isChecked && PreferenceManager.getDefaultSharedPreferences( this ).getBoolean("notifications",true)){
            NotificationHelper.createLedsActiveNotification(this)
        }
    }

    override fun onResume() {
        super.onResume()
        with(NotificationManagerCompat.from(this.applicationContext)) {
            cancelAll()
        }
    }

}