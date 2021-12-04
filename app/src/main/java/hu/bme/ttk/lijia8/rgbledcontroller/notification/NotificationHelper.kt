package hu.bme.ttk.lijia8.rgbledcontroller.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import hu.bme.ttk.lijia8.rgbledcontroller.MainActivity
import hu.bme.ttk.lijia8.rgbledcontroller.R
import kotlin.random.Random

class NotificationHelper {
    companion object {
        const val ACTION_SHOW_LEDS_ACTIVE = "hu.bme.ttk.lijia8.rgbledcontroller.showledsactive"
        fun createNotificationChannels(ctx: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = ctx.getString(R.string.active_leds)
                val descriptionText = ctx.getString(R.string.leds_on_desc)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel("hu.bme.ttk.lijia8.rgbledcontroller.activeleds", name, importance).apply {
                    description = descriptionText
                }
                with(NotificationManagerCompat.from(ctx)) {
                    createNotificationChannel(channel)
                }
            }
        }

        fun createLedsActiveNotification(ctx: Context) {
            val intent = Intent(ctx, MainActivity::class.java).apply {
                action = ACTION_SHOW_LEDS_ACTIVE
            }
            val pendingIntent: PendingIntent =
                PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val builder =
                NotificationCompat.Builder(ctx, "hu.bme.ttk.lijia8.rgbledcontroller.activeleds")
                    .setSmallIcon(R.drawable.ic_baseline_highlight_24)
                    .setContentTitle(ctx.getString(R.string.active_leds))
                    .setContentText(ctx.getString(R.string.leds_on_desc))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)

            with(NotificationManagerCompat.from(ctx)) {
                notify(Random.Default.nextInt(10000, 100000), builder.build())
            }
        }
    }
}