package com.example.cdl.board;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cdl.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;

public class BoardActivity extends AppCompatActivity {
    TextView textView;
    String url = "http://snj.yc.ac.kr/_Gesipan/list.php?tb=tb_news01&page=1&cate_no=&sd=&sg=&st=&search_yes=";
    String msg;
    String msg2;
    String[] url2 = new String[14];

    final Bundle bundle = new Bundle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        textView = findViewById(R.id.textView);
        WebView webView = findViewById(R.id.web);


        Intent intent = new Intent(getApplicationContext(), WebActivity.class);

        Button get = findViewById(R.id.button);
        get.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {


                webView.setWebChromeClient(new WebChromeClient());
                webView.loadUrl(url2[0]);



                intent.putExtra("a", url2[0]);
                startActivity(intent);
            }
        });
        Button get2 = findViewById(R.id.button2);
        get2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                webView.setWebChromeClient(new WebChromeClient());
                webView.loadUrl(url2[1]);

                intent.putExtra("a", url2[1]);
                startActivity(intent);
            }
        });
        Button get3 = findViewById(R.id.button3);
        get3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                webView.setWebChromeClient(new WebChromeClient());
                webView.loadUrl(url2[2]);

                intent.putExtra("a", url2[2]);
                startActivity(intent);
            }
        });
        Button get4 = findViewById(R.id.button4);
        get4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                webView.setWebChromeClient(new WebChromeClient());
                webView.loadUrl(url2[3]);

                intent.putExtra("a", url2[3]);
                startActivity(intent);
            }
        });
        Button get5 = findViewById(R.id.button5);
        get5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                webView.setWebChromeClient(new WebChromeClient());
                webView.loadUrl(url2[4]);
                intent.putExtra("a", url2[4]);
                startActivity(intent);
            }
        });
        Button get6 = findViewById(R.id.button6);
        get6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                webView.setWebChromeClient(new WebChromeClient());
                webView.loadUrl(url2[5]);

                intent.putExtra("a", url2[5]);
                startActivity(intent);
            }
        });
        Button get7 = findViewById(R.id.button7);
        get7.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                webView.setWebChromeClient(new WebChromeClient());
                webView.loadUrl(url2[6]);

                intent.putExtra("a", url2[6]);
                startActivity(intent);
            }
        });
        Button get8 = findViewById(R.id.button8);
        get8.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                webView.setWebChromeClient(new WebChromeClient());
                webView.loadUrl(url2[7]);

                intent.putExtra("a", url2[7]);
                startActivity(intent);
            }
        });

        new Thread(){
            @Override
            public void run(){
                Document doc = null;
                try{
                    doc = (Document) Jsoup.connect(url).get();
                    Elements elements = doc.select("tr td a");


                    String result = "\n";
                    int count = 0;
                    for(Element e : elements.select("a")) {


                        msg2 = e.absUrl("href");
                        url2[count] = msg2;

                        System.out.println(url2[count]);
                        System.out.println(count);
                        count = count + 1;

                    }
                    int count2 = 0;
                    System.out.println(url2[0]);
                    for(Element e : elements.select("a")){

                        msg = e.text();

                        result = result + msg + '\n' + '\n' + '\n';
                        bundle.putString("message", result);
                        Message msg = handler.obtainMessage();
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                        count2 = count2 + 1;
                        if(count2 == 8) break;
                    }


                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }.start();
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            Bundle bundle = msg.getData();

            textView.setText(bundle.getString("message"));
        }
    };



}