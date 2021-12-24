package com.example.cdl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cdl.login.LoginActivity;
import com.example.cdl.board.BoardActivity;
import com.example.cdl.check.CheckActivity;
import com.example.cdl.person.PersonActivity;
import com.example.cdl.push.NotificationUtils;
import com.example.cdl.question.QuestionActivity;
import com.example.cdl.sleep.SleepActivity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private long _triggerReminder;
    private String[] info = null;
    CookieManager cookieManager = CookieManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!cookieManager.hasCookies()){ // 쿠키 여부 확인 - 없으면 로그인 화면으로 넘김.
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            Log.i("Main", "0");
            startActivity(loginIntent);
        }
//        Log.i("Main", "1");

        reminderNotification(); // 푸시 알람

        String data = cookieManager.getCookie("http://146.56.165.103:8080/");

        TextView stuCode = (TextView)findViewById(R.id.mainCode);
        TextView stuName = (TextView)findViewById(R.id.mainName);

        ImageView person_btn = (ImageView) findViewById(R.id.person_btn);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);
        Button button5 = (Button) findViewById(R.id.button5);

        if (data != null) {

            UserInfoActivity userInfo = new UserInfoActivity();
            try {
                info = userInfo.execute(data).get().split("#");
                stuCode.setText(info[0]);
                stuName.setText(info[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        person_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent personIntent = new Intent(MainActivity.this, PersonActivity.class); // Main 에서 person 화면 전환.
                startActivity(personIntent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent CheckIntent = new Intent(MainActivity.this, CheckActivity.class);
                MainActivity.this.startActivity(CheckIntent);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent BoardIntent = new Intent(MainActivity.this, BoardActivity.class);
                MainActivity.this.startActivity(BoardIntent);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent QuestionIntent = new Intent(MainActivity.this, QuestionActivity.class);
                MainActivity.this.startActivity(QuestionIntent);
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SleepIntent = new Intent(MainActivity.this, SleepActivity.class);
                MainActivity.this.startActivity(SleepIntent);
            }
        });
    }
    public void reminderNotification()
    {

        NotificationUtils _notificationUtils = new NotificationUtils(this);

        LocalDate dtNow = LocalDate.now();
        int dayOfWeek = dtNow.getDayOfWeek().getValue();
        Log.i("DayOfWeek",String.valueOf(dayOfWeek));

        if (!(dayOfWeek == 1 || dayOfWeek == 3)){ //월요일과 수요일만 푸시가 나타나도록 변경

            return;
        }

        // long _currentDate = dtNow.atStartOfDay(ZoneOffset.of("+9")).toInstant().toEpochMilli(); // 시스템 날짜 0시 0분 0초를 ms로 나타냄.
        // long _resultTime = _currentDate + 72000000 ;


        // 20시간을 ms로 나타냄 - 20시 점호 기준 72000000L
        // http://extraconversion.com/time/hours/hours-to-milliseconds.html 오후 8시를 기준으로 시간을 나타냄.
        // 다른 시간이 필요한 경우 사용할 것. Hours 기준

        LocalDateTime toDayRc = LocalDateTime.of(LocalDate.now(), LocalTime.of(21,00)); // 현재 날짜의 21:00 LocalDateTime 객체를 얻음
        Long toDayRcMillis = toDayRc.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        Log.i("selectDate",String.valueOf(toDayRcMillis));


        long _currentTime = System.currentTimeMillis(); // 현재 시간
        Log.i("NowTime",String.valueOf(_currentTime));
        //long tenSeconds = 1000 * 5; // 10초

        long _triggerReminder = toDayRcMillis ; //10초후 실행되도록 설정
        _notificationUtils.setReminder(toDayRcMillis);


    }


    @Override
    public void onBackPressed () {
        // 뒤로가기 처리 함수 <-> finish() : 액티비티 종료를 위한 메서드 (오버라이딩 안됨)
        // 또한 백스택 무시하고 논리적으로 날려버림.
        // https://stackoverflow.com/questions/11807554/go-to-home-screen-instead-of-previous-activity
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

}