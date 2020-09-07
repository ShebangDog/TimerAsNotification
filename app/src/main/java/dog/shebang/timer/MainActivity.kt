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
import dog.shebang.timer.databinding.CustomNotificationBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()

        val customView = RemoteViews(packageName, R.layout.custom_notification).apply {
            setTextViewText(R.id.title, "Test Notification")
            setImageViewResource(R.id.image, R.mipmap.ic_launcher)
            setChronometer(
                R.id.chronometer_on_notification,
                SystemClock.elapsedRealtime(),
                null,
                true
            )
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setPriority(NotificationCompat.PRIORITY_DEFAULT)
            setContentIntent(pendingIntent)
            setAutoCancel(true)
        }

        with(NotificationManagerCompat.from(this@MainActivity)) {
            val notificationId = 1

            val builderForStarting = builder.apply {
                setCustomContentView(customView)
            }

            notify(notificationId, builderForStarting.build())
        }

        binding.apply {

            startButton.setOnClickListener {
                with(NotificationManagerCompat.from(this@MainActivity)) {
                    val notificationId = 1

                    val customViewForStarting = customView.apply {
                        setChronometer(
                            R.id.chronometer_on_notification,
                            SystemClock.elapsedRealtime(),
                            null,
                            true
                        )
                    }

                    val builderForStarting = builder.apply {
                        setCustomContentView(customViewForStarting)
                    }

                    notify(notificationId, builderForStarting.build())
                }
            }

            stopButton.setOnClickListener {
                with(NotificationManagerCompat.from(this@MainActivity)) {
                    val notificationId = 1

                    val customViewForStopping = customView.apply {
                        setChronometer(
                            R.id.chronometer_on_notification,
                            SystemClock.elapsedRealtime(),
                            null,
                            false
                        )
                    }

                    val builderForStopping = builder.apply {
                        setCustomContentView(customViewForStopping)
                    }

                    notify(notificationId, builderForStopping.build())
                }
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
