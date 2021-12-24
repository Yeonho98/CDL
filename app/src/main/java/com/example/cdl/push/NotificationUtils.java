package com.example.cdl.push;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.cdl.R;
import com.example.cdl.check.CheckActivity;

public class NotificationUtils extends ContextWrapper
{

    private NotificationManager _notificationManager;
    private Context _context;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    public NotificationUtils(Context base)
    {
        super(base);
        _context = base;
        createChannel();
    }

    public NotificationCompat.Builder setNotification(String title, String body)
    {
        Intent notifyIntent = new Intent(this, CheckActivity.class);
        // Set the Activity to start in a new, empty task

        // Create the PendingIntent
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                this, 10, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        ); // 푸시 터치시 점호 화면으로 전환하기 위한 PendingIntent - 현재 사용하는 앱이 특정한 기능(ex-notification[푸시])에 접근할 수 있게 만듬.

        return new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentIntent(notifyPendingIntent) // 푸시알람 누르면 앱을 실행하는 기능
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT); // 푸시알림 설정
    }

    private void createChannel() // 안드로이드 오레오 버전 이상에서는 notification를 채널 단위로 관리한다.
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(PRIMARY_CHANNEL_ID // notification 사용을 위한 채널 이름
                    ,"Test Notification",NotificationManager.IMPORTANCE_HIGH); //  notification을 관리하기 위한 채널 설정.
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            getManager().createNotificationChannel(channel);
        }
    }

    public NotificationManager getManager()
    {
        if(_notificationManager == null)
        {
            _notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // 서비스명을 notification
        }

        return _notificationManager;
    }

    public void setReminder(long timeInMillis)
    {
        Intent _intent = new Intent(_context, ReminderBroadcast.class);

        AlarmManager _alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent [] _pendingIntents = new PendingIntent[4];

        int i = 0;
        for(PendingIntent value : _pendingIntents){
            _intent.putExtra("id",i);
            value = PendingIntent.getBroadcast(_context, i, _intent, PendingIntent.FLAG_ONE_SHOT);
            // https://stackoverflow.com/questions/18750755/android-notifications-multiple-times
            _pendingIntents[i] = value;
            i++;
        }
        long result = timeInMillis;
        long [] timeTable = {result - 900000, result, result + 600000, result + 900000}; // -15분 시작 +10분 +15분
        // NotificationCompat(다른 앱의 기능)을 사용하기 위해 권한을 부여한다.

        long currentTime = System.currentTimeMillis();

        for(int t = 0; t < timeTable.length; t++) {
            if(currentTime < timeTable[t]) {      // 설정할 알람 시간이 현재 시간보다 이후인 경우
                Log.i("NotificationUtils", "SETTING : " + t + "(" + String.valueOf(timeTable[t]) + ")");
                _alarmManager.set(AlarmManager.RTC_WAKEUP, timeTable[t], _pendingIntents[t]);
            }
        }
    }


}