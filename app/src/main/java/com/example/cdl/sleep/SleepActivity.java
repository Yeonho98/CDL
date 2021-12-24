package com.example.cdl.sleep;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.cdl.R;
import com.example.cdl.UserInfoActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.regex.Pattern;

public class SleepActivity extends AppCompatActivity {
    public TextView datetextStart;
    public TextView datetextEnd;
    String dateMessageStart, dateMessageEnd,content,result, stuCode;
    EditText editText;
    TextView stuSleepCode,stuSleepRoom,stuSleepName;
    Button button;
    private LocalDate start, end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);
        datetextStart= findViewById(R.id.datetext);
        datetextEnd= findViewById(R.id.datetext1);

        editText = (EditText) findViewById(R.id.sleepContent);

        stuSleepCode = (TextView)findViewById(R.id.stuSleepCode);
        stuSleepName = (TextView)findViewById(R.id.stuSleepName);
        stuSleepRoom = (TextView)findViewById(R.id.stuSleepRoom);

        CookieManager cookieManager = CookieManager.getInstance();
        stuCode = cookieManager.getCookie("http://146.56.165.103:8080/");

        String [] info = null;
        UserInfoActivity userInfo = new UserInfoActivity();
        try {
            info = userInfo.execute(stuCode).get().split("#");
        } catch (Exception e) {
            e.printStackTrace();
        }
        stuSleepCode.setText(info[0]);
        stuSleepName.setText(info[1]);
        stuSleepRoom.setText(info[2]);

        button = (Button) findViewById(R.id.requestButton);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = editText.getText().toString();
                Log.i("content"," : " + content);
                if(!content.replaceAll(" ", "").equals("")) {
                    if (dateMessageStart != null && dateMessageEnd != null) {
                        start = LocalDate.parse(dateMessageStart, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                        end = LocalDate.parse(dateMessageEnd, DateTimeFormatter.ofPattern("yyyy/MM/dd"));

                        if (end.isAfter(start)) {
                            //https://docs.oracle.com/javase/8/docs/api/java/time/LocalDate.html#isAfter-java.time.chrono.ChronoLocalDate-
                            RegisterSleepActivity task = new RegisterSleepActivity();

                            try {
                                Log.i("content", stuCode + " " + dateMessageStart + " " + dateMessageEnd + " " + content);
                                result = task.execute(stuCode, dateMessageStart, dateMessageEnd, content).get();
                                Toast.makeText(SleepActivity.this, result, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(SleepActivity.this, "날짜가 잘못 설정되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SleepActivity.this, "시작/도착 날자가 불분명합니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SleepActivity.this, "내용을 입력하세요", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void showDatePickerStart(View view) {
        DialogFragment newFragment = new DatePickerFragmentStart();
        newFragment.show(getSupportFragmentManager(),"datePicker");
    }

    public void processDatePickerResultStart(int year, int month, int day){
        String month_string = Integer.toString(month+1);
        String day_string = Integer.toString(day);
        if(day < 10){
            day_string = "0" + day_string;
        }
        String year_string = Integer.toString(year);
        dateMessageStart = (year_string + "/" + month_string + "/" + day_string);
        datetextStart.setText(String.format("%d / %d / %d",year,month+1,day));

        Toast.makeText(this,"Date: "+dateMessageStart,Toast.LENGTH_SHORT).show();
    }

    public void showDatePickerEnd(View view) {
        DialogFragment newFragment = new DatePickerFragmentEnd();
        newFragment.show(getSupportFragmentManager(),"datePicker1");
    }

    public void processDatePickerResultEnd(int year, int month, int day){
        String month_string = Integer.toString(month+1);
        String day_string = Integer.toString(day);
        if(day < 10){
            day_string = "0" + day_string;
        }
        String year_string = Integer.toString(year);
        dateMessageEnd = (year_string + "/" + month_string + "/" + day_string);
        datetextEnd.setText(String.format("%d / %d / %d",year,month+1,day));


        Toast.makeText(this,"Date: "+dateMessageEnd,Toast.LENGTH_SHORT).show();
    }


}