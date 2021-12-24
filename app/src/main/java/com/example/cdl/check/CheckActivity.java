package com.example.cdl.check;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.cdl.R;
import com.example.cdl.login.LoginActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class CheckActivity extends AppCompatActivity implements OnMapReadyCallback,LocationListener {

    Button registerBtn;


    private LocationManager locationManager;
    TextView isRollCall;
    String parseLat, parseLng;
    double lng,lat;
    private String TAG = "LocationProvider";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rollcall);

        CookieManager cookieManager = CookieManager.getInstance();

        if(!cookieManager.hasCookies()){ // 쿠키 여부 확인 - 없으면 로그인 화면으로 넘김. 로그인을 위한 쿠키 등록까지 30초
            Intent loginIntent = new Intent(CheckActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        }
        registerBtn = (Button)findViewById(R.id.register_btn); // 점호 참여 버튼
        isRollCall = (TextView)findViewById(R.id.rollCall);
        isRollCall.setGravity(Gravity.CENTER);
        String[] isRc = null;
        RollCallRegisterActivity rc = new RollCallRegisterActivity();
        try {
            isRc = rc.execute().get().split("#");
        } catch(Exception e){
            Log.i("DBtest","......ERROR...!");
        }
        Log.i("content",isRc[2]);
        if (isRc[2].equals("O")){
            isRollCall.setText(isRc[0] + "\n점호 참여 가능");
        } else if(isRc[2].equals("X")){
            isRollCall.setText(isRc[0] + "\n점호 참여 불가능");
        } else {
            isRollCall.setText(isRc[0] + "\n오늘은 점호가 없습니다.");
        }


        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.Map);
        fragment.getMapAsync((OnMapReadyCallback) this); // 구글 맵 요소


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        } // locationManager 위치 권한 체크
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        // 위치값 받기

        if (lastKnownLocation != null) {
            lat = lastKnownLocation.getLatitude(); // 35.1656664 128.0982364 : 에뮬레이터용 테스트 위치(기숙사) - 에뮬레이터의 경우 위치 정보가 정확하지 않음.
            lng = lastKnownLocation.getLongitude(); // lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()
            // emulator 위치 값 오류로 인한 위치 값 특정.
            Log.d(TAG, "nlongtitude=" + lng + ", latitude=" + lat);
        }
        parseLat = Double.toString(lat);
        parseLng = Double.toString(lng);

        registerBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                try{
                    String result;
                    if (!cookieManager.getCookie("http://146.56.165.103:8080/").isEmpty()){ // 관리자 사이트 (key) - 학번 (value)
                        String id = cookieManager.getCookie("http://146.56.165.103:8080/");

                        Log.i("stu_code",id);
                        CheckRegisterActivity task = new CheckRegisterActivity();
                        result = task.execute(id,parseLat,parseLng).get().replace("]", "]\n"); // jsp에서 out.print() 사용시 값 들어감. select에 맞게 하면 될듯
                        // execute(String ... strings)
                        Log.i("DBtest",result);
                        Toast.makeText(CheckActivity.this,result,Toast.LENGTH_SHORT).show();
                    }

                }catch(Exception e){
                    Log.i("DBtest","......ERROR...!");
                }
            }

        });





    }
    @Override
    public void onProviderEnabled(String provider) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this); // 위치값 업데이트
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();// location.getLatitude() 35.1656664 - 기숙사 35.1638058 - 엠비씨네
        double longtitude = location.getLongitude();//location.getLongitude() 128.0982364 - 위치 128.1077792 - 위치

        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            Log.d(TAG + "-NETWORK : ", Double.toString(latitude) + '/' + Double.toString(longtitude));

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }



    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates((LocationListener) this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
    }

    GoogleMap mMap;



    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.getUiSettings().setAllGesturesEnabled(false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        double lat = lastKnownLocation.getLatitude(); // lastKnownLocation.getLatitude() 마커 위치에 맞게 조정함.
        double lng = lastKnownLocation.getLongitude(); // lastKnownLocation.getLongitude()

        LatLng sUser = new LatLng(lat, lng);
        LatLng sArea = new LatLng(35.1623664, 128.0982364);
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(sUser);
        markerOption.title("현재 위치");
        markerOption.snippet("설명");
        mMap.addMarker(markerOption);

        markerOption.position(sArea);
        markerOption.alpha(0.75f);
        markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.blackcircle));
        markerOption.title("기숙사 범위");
        markerOption.snippet("범위에 포함되어야 점호에 성공");
        mMap.addMarker(markerOption);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng), 15));

    }


}