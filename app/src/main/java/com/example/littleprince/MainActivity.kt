package com.example.littleprince

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var notificationChannel: NotificationChannel
    private lateinit var notificationManager: NotificationManager
    private lateinit var builder: Notification.Builder
    private val channelId = "12345"
    private val description = "Уведомления приложения LittlePrince"
    private val notificationId = 12345

    private var canClick = true

    private lateinit var btnMorning: Button
    private lateinit var btnDay: Button
    private lateinit var btnEvening: Button
    private lateinit var btnNight: Button
    private lateinit var imgView: ImageView

    private var currentTime = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        btnMorning = findViewById(R.id.btnMorning)
        btnDay = findViewById(R.id.btnDay)
        btnEvening = findViewById(R.id.btnEvening)
        btnNight = findViewById(R.id.btnNight)
        imgView = findViewById(R.id.imageView)

        val animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        imgView.startAnimation(animationFadeIn)
        findViewById<LinearLayout>(R.id.linearLayout).startAnimation(animationFadeIn)
    }

    private fun changeImageAnim(view: View, imgRes: Int){
        val animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        view.startAnimation(animationFadeOut)
        Handler().postDelayed({
            imgView.setImageResource(imgRes)
            view.startAnimation(animationFadeIn)
            Handler().postDelayed({ canClick = true }, 1000)
        }, 1000)
    }

    private fun notifyUser(title: String, text: String)
    {
        val intent = Intent(this, LauncherActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, description, NotificationManager .IMPORTANCE_HIGH)
//            notificationChannel.lightColor = Color.BLUE notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
            builder = Notification.Builder(this, channelId)
                                   .setContentTitle(title)
                                   .setContentText(text)
                                   .setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pendingIntent)
        }
        notificationManager.cancel(notificationId)
        notificationManager.notify(notificationId, builder.build())
    }

    fun timeClick(view: View)
    {
        if (canClick)
        {
            canClick = false
            when(view)
            {
                btnMorning -> {
                    currentTime = 0
                    changeImageAnim(imgView, R.drawable.morning)
                    notifyUser("Время суток изменилось","Наступило утро. Вы можете выпить кофе")
                }
                btnDay -> {
                    currentTime = 1
                    changeImageAnim(imgView, R.drawable.day)
                    notifyUser("Время суток изменилось","Наступил день. Вам пора работать")
                }
                btnEvening -> {
                    currentTime = 2
                    changeImageAnim(imgView, R.drawable.evening)
                    notifyUser("Время суток изменилось","Наступил вечер. Вам пора ехать домой")
                }
                btnNight -> {
                    currentTime = 3
                    changeImageAnim(imgView, R.drawable.night)
                    notifyUser("Время суток изменилось","Наступила ночь. Вы можете пойти спать")
                }
                imgView -> {
                    canClick = true
                    when(currentTime)
                    {
                        0 -> Toast.makeText(applicationContext, "Вы сделали глоток из чашки с кофе", Toast.LENGTH_LONG).show()
                        1 -> Toast.makeText(applicationContext, "Вы работаете над новым Android приложением", Toast.LENGTH_LONG).show()
                        2 -> Toast.makeText(applicationContext, "Вы сели в вагон метро и едете домой", Toast.LENGTH_LONG).show()
                        3 -> Toast.makeText(applicationContext, "Вы легли в кровать и стараетесь уснуть", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}