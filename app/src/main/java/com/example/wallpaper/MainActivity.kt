package com.example.wallpaper

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import com.example.wallpaper.ui.theme.WallpaperTheme
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WallpaperTheme {
                WallpaperApp()
//                QuickSettingsScreen()
            }
        }

//        setAlarm()
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun setAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, MyJobService::class.java)
        val pendingIntent = PendingIntent.getService(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 알람은 매일 정각에 실행
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, 19)
        calendar.set(Calendar.MINUTE, 29)
        calendar.set(Calendar.SECOND, 0)

        // Android 8.0 (API 레벨 26) 이상에서는 setExact 대신에 setExactAndAllowWhileIdle을 사용
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }
}

class MyJobService : JobService() {
    private val NOTIFICATION_CHANNEL_ID = "channel_id"
    private val NOTIFICATION_ID = 1

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartJob(params: JobParameters): Boolean {
        startForeground(NOTIFICATION_ID, createNotification())
        checkAndPerformAction(params, this)
        stopForegroundService(true)
        jobFinished(params, false)
        return false
    }

    @SuppressLint("ServiceCast")
    private fun createNotification(): Notification {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Notification 채널 생성 (Android O 이상에서 필요)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(NOTIFICATION_CHANNEL_ID, "Channel", NotificationManager.IMPORTANCE_LOW)
            notificationManager.createNotificationChannel(channel)
        }

        // Notification 빌더 설정
        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText("Running...")
            .setSmallIcon(R.drawable.androidparty)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)

        return builder.build()
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        stopForegroundService(true)
        return false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkAndPerformAction(params: JobParameters, context: Context) {
        val dayOfWeek = LocalDateTime.now().dayOfWeek

        if (dayOfWeek in DayOfWeek.MONDAY..DayOfWeek.FRIDAY) {
            // 월요일부터 금요일까지만 실행되는 작업 수행
            Log.d("MyJobService", "Executing task on ${dayOfWeek.name}")
            // 여기에 실행할 작업 추가
            WallpaperActionList().setWallpaper(context, R.drawable._1f854bd38a2b)
        } else {
            Log.d("MyJobService", "No task on ${dayOfWeek.name}")
        }
    }

    private fun stopForegroundService(removeNotification: Boolean) {
        if (removeNotification) {
            stopForegroundService(true)
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(NOTIFICATION_ID)
    }
}

@Composable
fun WallpaperChangeButtonAndImage(context: Context, modifier: Modifier = Modifier) {
    var result by remember { mutableStateOf(1) }
    val imageResource = when (result) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(imageResource),
            contentDescription = result.toString()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            result = (1..6).random()
            WallpaperActionList().setWallpaper(context, R.drawable._1f854bd38a2b)
        }) {
            Text(stringResource(R.string.roll))
        }
        Button(onClick = {
            result = (1..6).random()
            WallpaperActionList().setWallpaper(context, R.drawable.i16001892294_2)
        }) {
            Text(stringResource(R.string.roll))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WallpaperApp() {
    val context = LocalContext.current
    WallpaperTheme {
        WallpaperChangeButtonAndImage(context, modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
        )
    }
}