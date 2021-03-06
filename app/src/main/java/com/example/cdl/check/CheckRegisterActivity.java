package com.example.cdl.check;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class CheckRegisterActivity extends AsyncTask<String, Void, String>  {
   String sendMsg, receiveMsg;

   @Override
    protected String doInBackground(String... strings) {
       try {


           String str;

           URL url = new URL("http://146.56.165.103:8080/android/attendRollCallProc.jsp"); // 보내려는 주소

           HttpURLConnection conn = (HttpURLConnection) url.openConnection();
           conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
           conn.setRequestMethod("POST"); // HTTP 방식
           OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");

           sendMsg = "rc_stucode=" + strings[0] + "&latY=" + strings[1] + "&lngX=" + strings[2];

           osw.write(sendMsg);
           osw.flush();

           if (conn.getResponseCode() == conn.HTTP_OK) {
               InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
               BufferedReader reader = new BufferedReader(tmp);
               StringBuffer buffer = new StringBuffer();

               while ((str = reader.readLine()) != null) {
                   buffer.append(str);
               }
               receiveMsg = buffer.toString();
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
