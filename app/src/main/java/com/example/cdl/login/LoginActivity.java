package com.example.cdl.login;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.example.cdl.MainActivity;
import com.example.cdl.R;
import com.example.cdl.check.CheckActivity;
import com.example.cdl.push.NotificationUtils;

import java.time.LocalDate;
import java.time.ZoneOffset;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    EditText id2, pw2;

    CookieManager cookieManager = CookieManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button)findViewById(R.id.loginButton);
        id2 = (EditText) findViewById(R.id.id);
        pw2 = (EditText) findViewById(R.id.pw);

        loginButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                try{

                    String result;
                    String id = id2.getText().toString(); // 계정 정보 넣기 (학번)
                    String pwd = pw2.getText().toString(); // (비밀번호)

                    LoginRegisterActivity task = new LoginRegisterActivity();
                    result = task.execute(id,pwd).get(); // jsp에서 out.print() 사용시 값 들어감. select에 맞게 하면 될듯

                    if (result.equals("로그인 성공")){
                        cookieManager.removeAllCookies(null); // 남아있는 쿠키 제거
                        //쿠키를 사용한 이유. 직접 intent에 변수를 넣어 전달하는 과정은 로그인-메인-각각의 기능 순으로 전달해야함. 효율적이지 못함.
                        //학생을 위한 객체를 생성하기에는 전반적인 기능에 필요한건 학번에 불과함.
                        cookieManager.setCookie("http://146.56.165.103:8080/", id); // 전반적인 기능을 위한 학번을 쿠키에 저장함
                        String s = cookieManager.getCookie("http://146.56.165.103:8080/");
                        Log.i("stu_code : ",s);

                        Toast.makeText(LoginActivity.this,"로그인 성공",Toast.LENGTH_SHORT).show();

                        Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(loginIntent);
        
                    } else {
                        Toast.makeText(LoginActivity.this,"로그인에 실패하였습니다",Toast.LENGTH_SHORT).show();
                    }


                }catch(Exception e){
                    Log.i("DBtest","......ERROR...!");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // 뒤로가기 처리 함수 <-> finish() : 액티비티 종료를 위한 메서드 (오버라이딩 안됨)
        // 또한 백스택 무시하고 논리적으로 날려버림.
        // https://stackoverflow.com/questions/11807554/go-to-home-screen-instead-of-previous-activity
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
    @Override
    protected void onStart() { // 권한을 얻는 방법
        super.onStart();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //권한이 없을 경우 최초 권한 요청 또는 사용자에 의한 재요청 확인
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // 권한 재요청
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return;
            }
        }
    }

}