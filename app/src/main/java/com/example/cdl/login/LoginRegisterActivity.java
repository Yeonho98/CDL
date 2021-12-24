package com.example.cdl.login;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class LoginRegisterActivity extends AsyncTask<String, Void, String>  {
    String sendMsg, receiveMsg;

    @Override
    protected String doInBackground(String... strings) {
        try {


            String str;

            URL url = new URL("http://146.56.165.103:8080/android/loginProc.jsp"); // 보내려는 주소 http://192.168.2.6:8080/CloudTestExample/test3.jsp

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST"); // HTTP 방식
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");

            sendMsg = "stu_code=" + strings[0] + "&stu_pwd=" + strings[1]; // 특정 기능에 맞게 strings의 값이 유동적으로 변경.

            osw.write(sendMsg); // 사이트를 위한 request 등록
            osw.flush();

            if (conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8"); // out.print 받는 과정
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();

                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString(); // 결과값 - MainActivity get()으로 받음.
            } else {

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return receiveMsg;
    }
}

