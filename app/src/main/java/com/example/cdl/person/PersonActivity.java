package com.example.cdl.person;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.cdl.MainActivity;
import com.example.cdl.R;
import com.example.cdl.login.LoginActivity;
import com.example.cdl.passwordChange.PasswordChangeActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class PersonActivity extends AppCompatActivity {



    private TextView personCode, personName, personRoom, personAddress, personPhone, personPoint;
    private String [] studentInfo = new String[5];
    private TableLayout tableLayout;
    private Button changeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

         // 개인정보(학번, 이름, 호실, 주소, 번호), 받을 값의 개수
        ArrayList<String> result = null;

        String data = CookieManager.getInstance().getCookie("http://146.56.165.103:8080/");
        RegisterPersonActivity task = new RegisterPersonActivity();
       try {
            result = new ArrayList<String>(Arrays.asList(task.execute(data).get().split("\\*"))); // jsp에서 out.print() 사용시 값 들어감. select에 맞게 하면 될듯
        } catch (ExecutionException e) {
           e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        studentInfo = result.get(0).split("#");

        personCode = (TextView)findViewById(R.id.personCode);
        personCode.setText(studentInfo[0]);

        personName = (TextView)findViewById(R.id.personName);
        personName.setText(studentInfo[1]);

        personRoom = (TextView)findViewById(R.id.personRoom);
        personRoom.setText(studentInfo[2]);

        personAddress = (TextView)findViewById(R.id.personAddress);
        personAddress.setText(studentInfo[3]);

        personPoint = (TextView)findViewById(R.id.mainPoint);
        personPoint.setText(studentInfo[4]);

        personPhone = (TextView)findViewById(R.id.personPhone);
        personPhone.setText(studentInfo[5]);

        String [] rPointInfo = new String[Integer.parseInt(result.get(1).replace("<br>",""))];
        int pointer = 2; // get(3)부터 점호 내역임.

        for (int i = 0; i<rPointInfo.length; i++){
            rPointInfo[i] = result.get(i+pointer);

        }


        tableLayout = (TableLayout) findViewById(R.id.rPointTable);

        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tr.setBackground(getDrawable(R.drawable.box_border_left));

        String[] trHeader = {"순번", "일시", "내용", "점수"};
        for(int i = 0; i < 4; i++) {
            TextView textView = new TextView(this);
            textView.setText(trHeader[i]);
            textView.setGravity(Gravity.CENTER);
            tr.addView(textView);
        }
        tableLayout.addView(tr);

        for (int i = 0; i<rPointInfo.length; i++){
            String [] content = null; // 순번, 날짜, 사유 , 상벌점
            content = rPointInfo[i].split("#");

            content[0] = content[0].replace("<br>","");
            TableRow tableRow = new TableRow(this); // 테이블 row를 for 문 밖에 놔두면 초기화를 하지 못하여 content는 바뀌나 row에 들어가는 text는 동일함.
            tableRow.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tableRow.setBackground(getDrawable(R.drawable.box_border));

            for (int j = 0; j<content.length; j++){
                TextView textView = new TextView(this);
                textView.setText(content[j]);
                textView.setGravity(Gravity.CENTER);
                tableRow.addView(textView);
            }
            if (tableRow.getParent() != null){
                ((ViewGroup) tableRow.getParent()).removeView(tableRow);
            }
            tableLayout.addView(tableRow);

        }
        changeButton = (Button)findViewById(R.id.passChange);
        changeButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent loginIntent = new Intent(PersonActivity.this, PasswordChangeActivity.class); // Main 에서 Login 화면 전환.
                startActivity(loginIntent);
            }
        });

        Button logOutBtn = (Button) findViewById(R.id.log_Out);
        logOutBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                CookieManager cookieManager = CookieManager.getInstance();;
                cookieManager.removeAllCookies(null); // 쿠키 제거 ( 로그아웃 구현 )
                Intent loginIntent = new Intent(PersonActivity.this, LoginActivity.class); // Main 에서 Login 화면 전환.
                startActivity(loginIntent);
            }
        });
    }


}
