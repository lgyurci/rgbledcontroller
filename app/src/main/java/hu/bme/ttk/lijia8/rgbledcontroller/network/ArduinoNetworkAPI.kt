package hu.bme.ttk.lijia8.rgbledcontroller.network

import android.content.Context
import androidx.preference.PreferenceManager
import java.io.OutputStream
import java.net.Socket

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
        val sc = Socket(ip,port)
        sc.soTimeout = 1000
        return sc
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