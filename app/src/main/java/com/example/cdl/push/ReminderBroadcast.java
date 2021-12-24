package com.example.cdl.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.cdl.push.NotificationUtils;

public class ReminderBroadcast extends BroadcastReceiver
{
    String [] comment = {"점호 시작까지 15분 남았습니다.","점호가 시작되었습니다.", "점호 마감 5분전 입니다.", "점호가 마감되었습니다."};
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle value = intent.getExtras(); // putExtra()로 intent에 넣은 값을 받아오는 방법.
        // https://stackoverflow.com/questions/29846244/intent-putextra-in-pending-intent-not-working
        int number = value.getInt("id");

        Log.i("ReminderBroadCast","CONTENT : " + String.valueOf(number) + " " + comment[number]);
        Log.i("ReminderBroadCast","NOW TIME IS : " + String.valueOf(System.currentTimeMillis()));
        NotificationUtils _notificationUtils = new NotificationUtils(context);
        NotificationCompat.Builder _builder = _notificationUtils.setNotification("[점호]", comment[number]);
        _notificationUtils.getManager().notify(101, _builder.build());
        Log.i("test","notification Online");
    }
}
