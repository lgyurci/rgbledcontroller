package hu.bme.ttk.lijia8.rgbledcontroller.network

import android.content.Context
import android.util.Log
import androidx.preference.PreferenceManager
import hu.bme.ttk.lijia8.rgbledcontroller.singletons.CurrentRGB
import java.io.OutputStream
import java.net.Socket
import kotlin.experimental.and

class ArduinoNetworkAPI (private val context: Context) {
    fun sendRGB(r:Int,g:Int,b:Int) : Int{
        val sc = initOutConnection()
        val os = sc.getOutputStream()
        val rgb = byteArrayOf(
            'S'.toByte(),
            r.toByte(),
            g.toByte(),
            b.toByte()
        )
        os.write(rgb)
        os.flush()
        os.close()
        sc.close()
        return 0
    }

    fun fetchCurrentRGB():Int {
        val sc = initOutConnection()
        val oS = sc.getOutputStream()
        val iS = sc.getInputStream()
        oS.write(byteArrayOf('G'.toByte(), 0, 0, 0))
        oS.flush()
        val buff = ByteArray(4)
        val len: Int = iS.read(buff)
        if (buff[0] == 'S'.toByte()) {
            val red: Int = buff[1].toInt() and 0xff
            val green: Int = buff[2].toInt() and 0xff
            val blue: Int = buff[3].toInt() and 0xff
            Log.d("Nemtom","$red $green $blue")
            if (red != 0 || green != 0 || blue != 0) {
                CurrentRGB.setRGB(red, green, blue)
            } else {
                CurrentRGB.brightness = 0
            }
        } else {
            CurrentRGB.brightness = 0
        }

        return 0
    }

    private fun initOutConnection(): Socket {
        val connectionString = PreferenceManager.getDefaultSharedPreferences(context).getString("ip","")
        val portAndIp = connectionString?.split(":")
        val ip = portAndIp?.get(0)
        var port = 80
        if (portAndIp?.size ?: 1 > 1){
            port = try {
                Integer.parseInt(portAndIp?.get(1) ?: "80")
            } catch (e: Exception){
                80
            }
        }
     //   Log.d("network",ip.plus(":".plus(port.toString())))
        val sc = Socket(ip,port)
        sc.soTimeout = 1000
        return sc
    }

    fun checkConnection() : Boolean {
        val sc = initOutConnection()
        val oS = sc.getOutputStream()
        val iS = sc.getInputStream()
        oS.write(byteArrayOf('G'.toByte(), 0, 0, 0))
        oS.flush()
        val buff = ByteArray(4)
        val len: Int = iS.read(buff)
        if (len > 0){
            return true
        }
        return false
    }

    fun switchRelay() : Int{
        val sc = initOutConnection()
        val os = sc.getOutputStream()
        val rgb = byteArrayOf(
            'L'.toByte(),
            0.toByte(),
            0.toByte(),
            1.toByte()
        )
        os.write(rgb)
        os.flush()
        os.close()
        sc.close()
        return 0
    }
}