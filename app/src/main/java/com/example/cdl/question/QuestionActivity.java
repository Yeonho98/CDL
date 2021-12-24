package com.example.cdl.question;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cdl.R;

public class QuestionActivity extends AppCompatActivity {

    private ArrayAdapter adapter;
    private Button sendButton;
    private Spinner spinner;
    private EditText editText;
    private TextView managerNumber1, managerNumber2, managerNumber3;
    private TextView managerName1, managerName2, managerName3;
    private String result, data, stuCode, questionText;

    String [] managerInfo = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        managerName1 = (TextView)findViewById(R.id.ManagerName1);
        managerName2 = (TextView)findViewById(R.id.ManagerName2);

        managerNumber1 = (TextView)findViewById(R.id.ManagerNumber1);
        managerNumber2 = (TextView)findViewById(R.id.ManagerNumber2);



        RegisterManagerCellphoneActivity task = new RegisterManagerCellphoneActivity();
        try {
           managerInfo = task.execute().get().split("/");
        } catch (Exception e){
            e.printStackTrace();
        }

        managerName1.setText(managerInfo[0]); // 이름
        managerNumber1.setText(managerInfo[1]); // 번호

        managerName2.setText(managerInfo[2]);
        managerNumber2.setText(managerInfo[3]);

        spinner = (Spinner) findViewById(R.id.tag);
        adapter = ArrayAdapter.createFromResource(this, R.array.tag, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        sendButton = (Button) findViewById(R.id.button4);
        editText = (EditText) findViewById(R.id.question);

        CookieManager cookieManager = CookieManager.getInstance();
        stuCode = cookieManager.getCookie("http://146.56.165.103:8080/");

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override   // position 으로 몇번째 것이 선택됬는지 값을 넘겨준다
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    data = String.valueOf(spinner.getItemIdAtPosition(position) + 1);
                }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionText = editText.getText().toString();
                RegisterQuestionActivity task = new RegisterQuestionActivity();
                try {
                    result = task.execute(stuCode,data,questionText).get();
                    Toast.makeText(QuestionActivity.this,result,Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        }


    public void onClicked1(View v){
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:"+managerInfo[1]));
        startActivity(intent);
    }
    public void onClicked2(View v){
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:"+managerInfo[3]));
        startActivity(intent);
    }
    public void onClicked3(View v){
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:010-1111-1111"));
        startActivity(intent);
    }

}