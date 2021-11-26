package hu.bme.ttk.lijia8.rgbledcontroller.singletons

import android.view.LayoutInflater
import hu.bme.ttk.lijia8.rgbledcontroller.databinding.ActivityMainBinding

object CurrentRGB {
    private var r=0
    private var g=0
    private var b=0
    private var br=0
    val red: Int
        get() = this.r
    val green: Int
        get() = this.g
    val blue: Int
        get() = this.b
    var brightness: Int
        get() = this.br
        set (value){
            br = value
        }
    val bred: Int
        get() = (this.r*(br.toDouble()/100)).toInt()
    val bgreen: Int
        get() = (this.g*(br.toDouble()/100)).toInt()
    val bblue: Int
        get() = (this.b*(br.toDouble()/100)).toInt()

    fun setRGB(red:Int,green:Int,blue:Int){
        var norm = 0
        if (red == 0 && green == 0 && blue == 0){
            r = 255
            g = 255
            b = 255
            br = 0
        } else {
            if (red >= green && red >= blue){
                norm = red
            } else if (green >= red && green >= blue) {
                norm = green
            } else if (blue >= red && blue >= green){
                norm = blue
            }
            var factor = 255.0/norm.toDouble()
            r = (red*factor).toInt()
            g = (green*factor).toInt()
            b = (blue*factor).toInt()
            if (factor < 1){
                factor = 1.0
            }
            br = ((1/factor)*100).toInt()
        }
    }
}