package dog.shebang.timer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dog.shebang.timer.Constants.CHANNEL_ID
import dog.shebang.timer.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var customView: RemoteViews

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        customView = RemoteViews(packageName, R.layout.custom_notification).apply {
            setChronometer(R.id.chronometer_on_notification, SystemClock.elapsedRealtime(), null, true)
            setTextViewText(R.id.title, "Test Notification")
            setImageViewResource(R.id.image, R.mipmap.ic_launcher)
        }

        createNotificationChannel()

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setCustomContentView(customView)
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setPriority(NotificationCompat.PRIORITY_DEFAULT)
            setContentIntent(pendingIntent)
            setAutoCancel(true)
        }

        with(NotificationManagerCompat.from(this)) {
            val notificationId = 1

            notify(notificationId, builder.build())
        }

        binding.apply {

            startButton.setOnClickListener {
                customView.setChronometer(R.id.chronometer_on_notification, SystemClock.elapsedRealtime(), null, true)
            }

            stopButton.setOnClickListener {
                customView.setChronometer(R.id.chronometer_on_notification, SystemClock.elapsedRealtime(), null, false)
            }
        }
    }

    private fun createNotificationChannel() {
        val name = CHANNEL_ID
        val descriptionText = "for testing"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
