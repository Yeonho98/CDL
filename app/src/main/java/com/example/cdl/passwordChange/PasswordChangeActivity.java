package com.example.cdl.passwordChange;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cdl.R;

public class PasswordChangeActivity extends AppCompatActivity {

    Button changeButton;
    EditText pw, pwCp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwordchange);

        changeButton = (Button)findViewById(R.id.passChangeButton);

        pw = (EditText) findViewById(R.id.changePassword);
        pwCp = (EditText) findViewById(R.id.changePasswordCopy);

        String stuCode = CookieManager.getInstance().getCookie("http://146.56.165.103:8080/");
        changeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                try{
                    String result = "비밀번호가 서로 다릅니다.";                              // 계정 정보 넣기 (학번)
                    String pwd = pw.getText().toString();       // (비밀번호)
                    String pwdCopy = pwCp.getText().toString(); // (다시 입력)

                    if(pwd.trim().isEmpty() || pwdCopy.trim().isEmpty()) {    // 둘 중 하나가 빈칸
                        Log.i("PasswordChange", "내용없음");
                        Toast.makeText(PasswordChangeActivity.this, "내용을 입력하세요", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (pwd.equals(pwdCopy)){      // 두 내용이 일치할 때
                            Log.i("PasswordChange", "일치");
                            RegisterPasswordActivity task = new RegisterPasswordActivity();
                            result = task.execute(stuCode, pwd).get(); // jsp에서 out.print() 사용시 값 들어감. select에 맞게 하면 될듯
                        }
                        Toast.makeText(PasswordChangeActivity.this, result, Toast.LENGTH_SHORT).show();
                        Log.i("PasswordChange", pwd + " / " + pwdCopy);
                    }
                } catch(Exception e){
                    Log.i("PasswordChange","Catch Exception");
                }
            }

        });


    }


}