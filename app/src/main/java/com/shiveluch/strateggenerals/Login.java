package com.shiveluch.strateggenerals;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.telephony.mbms.StreamingServiceInfo;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.View.GONE;


public class Login extends AppCompatActivity implements OnMapReadyCallback {
    String sbname;
    String domain = "http://gamestrateg.ru/generals/";
    String codenames = "http://gamestrateg.ru/generals/get_codes.php";
    String getcodes = "http://gamestrateg.ru/generals/get_gps_points.php";
    String addtolog = "http://gamestrateg.ru/generals/add_tolog.php";
    String add_taken = "http://gamestrateg.ru/generals/add_takens.php";
    String getTaken = "http://gamestrateg.ru/generals/get_taken.php";
    String getPersonal="";
    String matter = "";
    public String takenSide="";
    String getLastfromLog = "";
    String isMarker;
    String getTime = "";
    String getControl = "http://gamestrateg.ru/generals/get_control.php";
    float mapzoom = 14.535042f;
    String updateOwner = "http://gamestrateg.ru/generals/update_owner.php";
    String removeOld = "http://gamestrateg.ru/generals/remove_old_takens.php";
    String getEvents = "http://gamestrateg.ru/generals/get_events.php";
    String getLast = "";
    String getCount = "";
    String publicate = "";
    String getSize = "http://gamestrateg.ru/generals/get_points_size.php";
    String takePass = "";
    String killStatus = "";
    String updateStatus = "";
    String lastDate;
    String getCom = "";
    String getTimings="http://gamestrateg.ru/generals/get_timings.php";
    boolean showlist = false;
    //LatLng position =  new LatLng(54.885710, 38.35);//Энергетик
//LatLng position =  new LatLng(54.854968, 38.121925);//ступино
    LatLng position = new LatLng(55.54195, 36.93696);//Алабино
    //  LatLng position=new LatLng(55.086091,38.180888);//дача
    //  LatLng position=new LatLng(53.128827,158.359331);//Совхоз
    String summary = "";
    String get_pass = "";
    String alpha = "ABCDEFHIKLMNPQRSTUVWXYZ123456789";
    String command = "";
    String currentpoint;
    TextView genname, mess, coordtext, yellowtime, bluetime, greytime;
    RelativeLayout RL2;
    boolean isTaken = false;
    private GoogleMap mMap;
    private LocationManager locationManager;
    Context context;
    SupportMapFragment mapFragment;
    SharedPreferences mSettings;
    boolean showComonList = false;
    boolean isStart=false;
    public static final String APP_PREFERENCES = "mysettings";//filename
    public static final String APP_PREFERENCES_INFO = "INFO";//player's INFO
    public static final String APP_PREFERENCES_PASSWORD = "Password";//player's system password
    public static final String APP_PREFERENCES_CODES = "Codes";//player's point codes
    public static final String APP_PREFERENCES_ID = "Taken";//player's taken points
    public static final String APP_PREFERENCES_TAKEN = "CodesOfTakenPoints";//player's taken points
    public static final String APP_PREFERENCES_CONTROL="ControlPoints";
    public static final String APP_PREFERENCES_GENERAL = "General";
    public static final String APP_PREFERENCES_SIDE = "Side";
    public static final String APP_ORGANIZATOR = "Organizator";
    public static final String APP_PREFERENCES_POINTS = "Points";
    public static final String APP_PREFERENCES_TIME = "Time";
    public static final String APP_POINTS_SIZE = "PointSize";
    public static final String APP_IS_LOGIN = "isLogin";
    public static final String APP_TO_BASA = "toBasa";
    public static final String APP_STATUS = "status";
    public static final String APP_PUBLIC = "public";
    public static final String APP_NICKNAME = "Nickname";
    public static final String APP_IS_ONLINE = "isOnline";
    public static final String APP_IS_CONTROL = "Control";
    public static final String APP_IS_UNIQKEY = "Uniqkey";


    Marker[] markers = new Marker[50];
    String genName;
    Button approve, closeLog, reset, cancelReset, showLog, showMap, showMap2;
    ListView genlist;
    Marker imhere;
    Timer mTimer;
    private MyTimerTask myTimerTask;


    //TextView first_login, first_password;
    TextView generalname;
    EditText first_login, first_password, event_password;
    RelativeLayout RL1, secRL, mapview, center, startRL, RLList, RLComList;
    Button new_login, QRScan, send, sendlog;
    boolean check = false;
    public String have_pass;
    ImageView cenmap, backs, finger, QR, selflog, commonlog, upload, exit, restart;
    LinearLayout toppanel;
    ListView list;
    // ExpandableListView listView;
    ArrayList<String> get = new ArrayList<>();
    ArrayList<String> toBasa = new ArrayList<>();
    ArrayList<String> toCommonLog = new ArrayList<>();

    // Used in checking for runtime permissions.
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    // The BroadcastReceiver used to listen from broadcasts from the service.
    //   private MyReceiver myReceiver;

    // A reference to the service used to get location updates.
    // private LocationUpdatesService mService = null;

    // Tracks the bound state of the service.
    private boolean mBound = false;

    // UI elements.

    // Monitors the state of the connection to the service.
//    private final ServiceConnection mServiceConnection = new ServiceConnection() {
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
//            mService = binder.getService();
//            mService.requestLocationUpdates(); // also request it here
//            mBound = true;
//
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            mService = null;
//            mBound = false;
//        }
//    };

    @Override
    protected void onStop() {
        super.onStop();
//        if (mBound) {
//            // Unbind from the service. This signals to the service that this activity is no longer
//            // in the foreground, and the service can respond by promoting itself to a foreground
//            // service.
//            unbindService(mServiceConnection);
//            mBound = false;
        //  }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if (!checkPermissions()) {
//            requestPermissions();
//        } else if (mService != null) { // add null checker
//            mService.removeLocationUpdates();
//            mService.requestLocationUpdates();
//        }
//        bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
//                Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
       // LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);

        super.onPause();
    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            checkEnabled();
        }

        @Override
        public void onProviderEnabled(String provider) {
            checkEnabled();
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            showLocation(locationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (provider.equals(LocationManager.GPS_PROVIDER)) {
            //    Log.d("ShowLoc", "GPS");
            } else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
         //       Log.d("ShowLoc", "Network");

            }
        }
    };

    private void showLocation(Location location) {
        if (location == null)
            return;
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
      //      Log.d("ShowLoc", (formatLocation(location)));
          //  coordtext.setText(formatLocation(location));

        } else if (location.getProvider().equals(
                LocationManager.NETWORK_PROVIDER)) {
      //      Log.d("ShowLoc", (formatLocation(location)));
         //   coordtext.setText(formatLocation(location));
        }
        String[] locations=formatLocation(location).split("#");
        commonLat= Double.parseDouble(locations[0].replace(",","."));
        commonLon= Double.parseDouble(locations[1].replace(",","."));
        if (imhere!=null) imhere.remove();
        imhere=(mMap.addMarker(new MarkerOptions()
                .position(new LatLng(commonLat, commonLon))
                .title("Я здесь")
                .icon(getBitmapHighDescriptor(R.drawable.imhere))));

    }


    private String formatLocation(Location location) {
        if (location == null)
            return "";
        return String.format(
                "%1$.5f#%2$.5f",
                location.getLatitude(), location.getLongitude());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000 * 3, 10, locationListener);
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1000 * 3, 10,
                locationListener);
        checkEnabled();
        getJSON(getControl);

        getJSON(getSize);
        isMarker=(mSettings.getString(APP_TO_BASA,"")).replace('#','.');
      //  uploadInfo();
        idCheck();
//        String idcheck=mSettings.getString(APP_PREFERENCES_ID,"");
//        String gencheck=mSettings.getString(APP_PREFERENCES_GENERAL,"");
//        String nick=mSettings.getString(APP_NICKNAME,"");
//        if (idcheck!="") {
//            startRL.setVisibility(GONE);
//            //genName="Генерал "+mSettings.getString(APP_PREFERENCES_ID,"")+": ";
//
//            //  new_login.setVisibility(View.GONE);
//            // showMap.setVisibility(GONE);
//            //
//            genname.setText(nick.toUpperCase() + ", " + gencheck);
//            //        //secRL.setVisibility(View.VISIBLE);}
//            mapview.setVisibility(View.VISIBLE);
//            toppanel.setVisibility(View.VISIBLE);
//            QR.setVisibility(View.VISIBLE);
//            backs.setVisibility(GONE);
//        }
      //  getMarkers();
        //markers
//        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
//                new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //myReceiver = new MyReceiver();
        setContentView(R.layout.activity_login);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 123);
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        context = getApplicationContext();
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        if (Utils.requestingLocationUpdates(this)) {
            if (!checkPermissions()) {
                requestPermissions();
            }
        }
       // getJSON(getControl);

        first_login = findViewById(R.id.first_login);
        String thiskey=getString(R.string.settings);
        String coordLog=getCoordinates(getString(R.string.coordkey),thiskey);
        genname=findViewById(R.id.generalname);
        RL2=findViewById(R.id.RL2);
        coordtext=findViewById(R.id.coordtext);
        genlist=findViewById(R.id.genlist);
        toppanel=findViewById(R.id.toppanel);
        showLog=findViewById(R.id.showlog);
        RLList=findViewById(R.id.RLList);
        closeLog=findViewById(R.id.closeLog);
        cenmap=findViewById(R.id.cenmap);
        backs=findViewById(R.id.backs);
        selflog=findViewById(R.id.selflog);
        commonlog=findViewById(R.id.mainlog);
        upload=findViewById(R.id.upload);
        exit=findViewById(R.id.exit);
        RLComList=findViewById(R.id.RLComList);
        yellowtime=findViewById(R.id.yellowtime);
        bluetime=findViewById(R.id.bluetime);
        greytime=findViewById(R.id.greytime);

        first_password = findViewById(R.id.first_password);
        new_login = findViewById(R.id.new_login);
        finger=findViewById(R.id.finger);
        showMap=findViewById(R.id.showMap);
        showMap2=findViewById(R.id.showMap2);
        RL1=findViewById(R.id.center_layout);
        startRL=findViewById(R.id.startRL);
        center=findViewById(R.id.center_layout);
        mapview=findViewById(R.id.maplayout);
        secRL=findViewById(R.id.secRL);
        secRL.setVisibility(GONE);
        QRScan=findViewById(R.id.QRScan);
        send=findViewById(R.id.send);
        sendlog=findViewById(R.id.sendlog);
        list=findViewById(R.id.list);
        QR=findViewById(R.id.qr);
        restart = findViewById(R.id.restart);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        String org=mSettings.getString(APP_PREFERENCES_ID,"0");
     //   Log.d("OrgFlagID","Is org: "+org);

        int controls=Integer.parseInt(mSettings.getString(APP_IS_CONTROL,"0"));

        //if (controls==0) {getJSON(getControl);}
        getJSON(getControl);
        removeMarkers();
        idCheck();
        have_pass = mSettings.getString(APP_PREFERENCES_PASSWORD, "");
        String info = mSettings.getString(APP_PREFERENCES_INFO, "");
        String points=mSettings.getString(APP_PREFERENCES_CODES,"");
        String idcheck=mSettings.getString(APP_PREFERENCES_ID,"");
      //  Log.d ("check","Check is "+idcheck);
        String isCheck=mSettings.getString(APP_TO_BASA,"");
        String isLogin=mSettings.getString(APP_IS_LOGIN,"0");
        String gencheck=mSettings.getString(APP_PREFERENCES_GENERAL,"");
        String nick=mSettings.getString(APP_NICKNAME,"");
        String uniqKey=mSettings.getString(APP_IS_UNIQKEY,"");

        SharedPreferences.Editor createEdit=mSettings.edit();
        createEdit.putString(APP_IS_ONLINE,"0");
        createEdit.putString(APP_TO_BASA,coordLog);
        createEdit.apply();
        if (uniqKey.length()<4) {
            String isKey="";
            for (int i = 0; i < 6; i++) {
                Random rnd = new Random();
                int pos = rnd.nextInt(32);
                isKey=isKey+alpha.charAt(pos);
            }
            createEdit.putString(APP_IS_UNIQKEY,isKey);
            createEdit.apply();

        }

        toppanel.setVisibility(GONE);

        mTimer = new Timer();
        myTimerTask = new MyTimerTask();
        mTimer.schedule(myTimerTask, 1000,4000);
        //getJSON(getControl);
        selflog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invLog();
                showlist=!showlist;
                if (showlist) RLList.setVisibility(View.VISIBLE);
                if (!showlist) RLList.setVisibility(View.GONE);
                getPersonal=domain+"get_personal_events.php/get.php?nom="+mSettings.getString(APP_PREFERENCES_SIDE,"");
                getJSON(getPersonal);

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline(getApplicationContext())) {
                //    uploadInfo();

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(),
                            "Выгрузка произведена. Проверьте общий игровой лог",Toast.LENGTH_LONG).show();
                }
                if (!isOnline(getApplicationContext())) Toast.makeText(getApplicationContext(),
                        "Вне зоны дейстия сети",Toast.LENGTH_LONG).show();
            }
        });

        showMap2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QR.setVisibility(View.VISIBLE);
                getJSON(getcodes);
                try {   mapview.setVisibility(View.VISIBLE);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mapview.setVisibility(View.VISIBLE);
            }
        });

finger.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        invLog();
//        matter="http://gamestrateg.ru/generals/test_update.php";
//        String cur=mSettings.getString(APP_PREFERENCES_PASSWORD,"");
//        new testAsyncTask().execute("matter", cur);



        float getZoom=mMap.getCameraPosition().zoom;


        LatLng position=new LatLng(commonLat,commonLon);
        if (position.latitude==0 && position.longitude==0)
        {
            Toast.makeText(getApplicationContext(), "Подождите, идет определение текущих координат",Toast.LENGTH_LONG).show();
            return;
        }
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position)
                .zoom(getZoom)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.animateCamera(cameraUpdate);
        if (imhere!=null) imhere.remove();
        imhere=(mMap.addMarker(new MarkerOptions()
                .position(new LatLng(commonLat, commonLon))
                .title("Я здесь")
                .icon(getBitmapHighDescriptor(R.drawable.imhere))));
    }
});
        cenmap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                invLog();
                float getZoom=mMap.getCameraPosition().zoom;

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(position)
                        .zoom(getZoom)
                        .build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                mMap.animateCamera(cameraUpdate);
            }
        });

        showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getJSON(getControl);

                if (!checkPermissions()) {
                    requestPermissions();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ;
                }
                getJSON(getcodes);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startRL.setVisibility(GONE);
                getMarkers();
                mapview.setVisibility(View.VISIBLE);
            }
        });
        backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invLog();
                mapview.setVisibility(View.GONE);
                startRL.setVisibility(View.VISIBLE);
            }
        });

        getStatus();

        if (idcheck!="" || Integer.parseInt(mSettings.getString(APP_IS_LOGIN,"0"))>0 ) {
            startRL.setVisibility(GONE);
            //genName="Генерал "+mSettings.getString(APP_PREFERENCES_ID,"")+": ";

            //  new_login.setVisibility(View.GONE);
            // showMap.setVisibility(GONE);
            //
            genname.setText(nick.toUpperCase() + ", " + gencheck);
    //        //secRL.setVisibility(View.VISIBLE);}
            mapview.setVisibility(View.VISIBLE);
            toppanel.setVisibility(View.VISIBLE);
            int side=Integer.parseInt(mSettings.getString(APP_PREFERENCES_SIDE,"0"));
            if (side<9)QR.setVisibility(View.VISIBLE);
            if (side==9)restart.setVisibility(View.VISIBLE);
            backs.setVisibility(GONE);
        }
        String taken=mSettings.getString(APP_PREFERENCES_TAKEN,"");

        String []splitinfo=taken.split("#");
        if (splitinfo.length>=1) {
            {
                for (int i = 0; i < splitinfo.length; i++) {
                    String[] finalString = splitinfo[i].split(",");
                    if (finalString.length>1){
                        String toArray =
                                finalString[1] + " взята\n" + finalString[3] + " в " + finalString[4];
                       // get.add(0, toArray);
                    }
//                    ArrayAdapter adapter = new ArrayAdapter(this,
//                            R.layout.list_item, get);
////
//                    list.setAdapter(adapter);

//
//                        }
                }

            }

//            ArrayAdapter adapter = new ArrayAdapter(this,
//                    R.layout.list_item, get);
//
//            list.setAdapter(adapter);
        }
        String [] pointsarray=points.split("#");

        for (int i=0;i<pointsarray.length;i++)
        {
            String [] pointarray=pointsarray[i].split(",");

        }





        commonlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invLog();
                showComonList=!showComonList;
                if (showComonList) {RLComList.setVisibility(View.VISIBLE);
                    getJSON(getEvents);}
                if (!showComonList) RLComList.setVisibility(View.GONE);

            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invLog();
                //new ClearAsyncTask().execute(mSettings.getString(APP_PREFERENCES_ID,""));
               showResetDialog();

            }
        });

        new_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getJSON(getControl);

                if (!checkPermissions()) {
                    requestPermissions();
                }

                String curLog=first_login.getText().toString();
                String curPass=first_password.getText().toString();
                getMarkers();
                if (curPass.length()>0 && curLog.length()>0)
                {
                    System.out.println(""+curPass.length());
                    if (curPass.length()<6){
                    sbname="http://gamestrateg.ru/generals/get_generals.php/get.php?nom="+curPass;
                SharedPreferences.Editor editor=mSettings.edit();
                    editor.putString(APP_NICKNAME,curLog);
                    editor.putString(APP_IS_LOGIN,"0");
                    editor.apply();
                    getJSON(sbname);}

                    if (curPass.length()==9)
                    {
                        SharedPreferences.Editor editor=mSettings.edit();
                        editor.putString(APP_NICKNAME,curLog);
                        editor.putString(APP_IS_LOGIN,"0");
                        editor.apply();
                        getCom=domain+"get_commander.php/get.php?nom="+curPass;
                        getJSON(getCom);
                    }

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Не введено имя пользователя или пароль",Toast.LENGTH_SHORT).show();
                }

            }
        });

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
showRestartDialog();
            }
        });

        QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String owner=mSettings.getString(APP_PREFERENCES_SIDE,"");
                String gen=mSettings.getString(APP_PREFERENCES_GENERAL,"");
                String nick=mSettings.getString(APP_NICKNAME,"");
                Log.d("DATA", gen+", "+nick);
                if (isOnline()) {
                    invLog();

                    getJSON(getTaken);

                    IntentIntegrator integrator = new IntentIntegrator(activity);
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);

                    integrator.setPrompt(" ");
                    integrator.setCameraId(0);
                    integrator.setOrientationLocked(false);
//                integrator.autoWide();
                    //         integrator.setBeepEnabled(false);
                    //     integrator.setBarcodeImageEnabled(false);
                    try {
                        integrator.initiateScan();
                    } catch (Exception e) {

                    }
                }
                else Toast.makeText(getApplicationContext(), "Нет подключения к интернету",Toast.LENGTH_LONG).show();
            }


        });

        QRScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt(" ");
                integrator.setCameraId(0);
            //    integrator.autoWide();
                //         integrator.setBeepEnabled(false);
                //     integrator.setBarcodeImageEnabled(false);
                try {
                    integrator.initiateScan();
                } catch (Exception e) {

                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (isOnline()) {
            getJSON(getTaken);
            String controlPoint=mSettings.getString(APP_PREFERENCES_CONTROL,"");
            if (controlPoint.length()<1)
            {
                Toast.makeText(getApplicationContext(),"Не загружены стартовые данные", Toast.LENGTH_LONG).show();
                return;
            }

            String [] splitcontrol=controlPoint.split("#");
            String isSide=mSettings.getString(APP_PREFERENCES_SIDE,"0");


            String taken = mSettings.getString(APP_PREFERENCES_TAKEN, "");
            String toBasa = mSettings.getString(APP_TO_BASA, "");

            String basa = mSettings.getString(APP_PREFERENCES_POINTS, "");
            String[] allcodes = basa.split("#");
            boolean isCode = false;
            SharedPreferences.Editor newedit = mSettings.edit();
            newedit.putString(APP_IS_ONLINE, "0");
            newedit.apply();
            int takeDistance = 50;

            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

            if (result != null) {
                if (result.getContents() == null) {

                } else {
                    String new_codename = result.getContents();//получили код
                    Log.d("Taken", mSettings.getString(APP_PREFERENCES_CONTROL,""));

                    for (int i=0;i<splitcontrol.length;i++)
                    {
                        if (splitcontrol[i].contains(new_codename))
                        {
                            String[] getInfo = splitcontrol[i].split(",");
                            if (Integer.parseInt(getInfo[1])==Integer.parseInt(isSide))
                            {
                                showDialog("Точка уже под контролем вашей стороны");
                                return;

                            }

                            if (getInfo[2].equals("NULL")||getInfo[2].equals("null"))
                            {
                                getInfo[2]="60";
                            }
                            if (Integer.parseInt(getInfo[1])==3 && Integer.parseInt(getInfo[2])<60)
                            {
                                int diff=60-(Integer.parseInt(getInfo[2]));
                                showDialog("Точка под контролем стороны Союз еще\n" + diff + " мин.");
                                return;

                            }
                        }

                    }


                    for (int i = 0; i < allcodes.length; i++) {
                        String[] currentcode = allcodes[i].split(",");
                        if (currentcode[1].equals(new_codename)) {
                            double takenLat = Double.parseDouble(currentcode[2]);
                            double takenLon = Double.parseDouble(currentcode[3]);
                            double distance = Utils.distanceInKmBetweenEarthCoordinates(commonLat, commonLon, takenLat, takenLon);
                            String convert = ("" + distance * 1000);

                            String[] split = convert.split("\\.");
                            int finalDist = Integer.parseInt(split[0]);
                            if (finalDist > takeDistance)
                                Toast.makeText(getApplicationContext(), "" + currentcode[0] + " не может быть взята, дистанция: " + finalDist + "метров", Toast.LENGTH_LONG).show();
                            if (finalDist <= takeDistance) {
                                // Текущее время
                                Date currentDate = new Date();

                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
                                String dateText = dateFormat.format(currentDate);
                                DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.UK);
                                String timeText = timeFormat.format(currentDate);
                                String datetime = dateText + "," + timeText;
                                String cGen = mSettings.getString(APP_PREFERENCES_GENERAL, "");
                                String cNick = mSettings.getString(APP_NICKNAME, "");
                                String cPoint = currentcode[0];
                                String idPoint = currentcode[5];
                                String cSide = mSettings.getString(APP_PREFERENCES_SIDE, "");
                                String cId = mSettings.getString(APP_PREFERENCES_ID, "");
                                String cTime = mSettings.getString(APP_PREFERENCES_TIME, "");

                                String[] timesplit = cTime.split(",");
                                double isBegin = 0;
                                double isEnd = 0;
                                Date date1 = null, date2 = null;
                                SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);

                                try {

                                    date1 = formater.parse(timesplit[0]);
                                    date2 = formater.parse(timesplit[1]);

                                    isBegin = date1.getTime();
                                    isEnd = date2.getTime();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                double isDate = currentDate.getTime();
                                // if (isBegin<isDate && isDate<isEnd)
                                {
                                    //Toast.makeText(getApplicationContext(), cPoint + " взята", Toast.LENGTH_LONG).show();
                                    showDialog(cPoint + " взята");
                                    taken = taken + cGen + "," + cPoint + "," + idPoint + "," + datetime + "," + cSide + "," + cId + "#";
                                    updateGameLog(cGen, cPoint, idPoint,cSide,cId,cNick);
                                    String inf = cPoint + " взята\n" + dateText + " в " + timeText;
                                    get.add(0, inf);
//                                    ArrayAdapter adapter = new ArrayAdapter(this,
//                                            R.layout.list_item, get);
////
//                                    list.setAdapter(adapter);
                                    SharedPreferences.Editor editor = mSettings.edit();
                                    editor.putString(APP_PREFERENCES_INFO, (inf + "#"));
                                    editor.putString(APP_PREFERENCES_TAKEN, (taken));
                                    editor.apply();
                                    String owner = mSettings.getString(APP_PREFERENCES_SIDE, "0");
                                    int newid = i + 1;

                                    //  uploadInfo();
                                    //       Log.d("Side", "Check side: " + owner+", "+new_codename);
                                    new uploadAsyncTask().execute("update_owner", owner, new_codename, "" + newid, "");
                                }
//                             else
//                             {
//                                 Toast.makeText(getApplicationContext(),"Время взятия истекло либо не наступило",Toast.LENGTH_LONG).show();
//
//                             }
                            }
                            isCode = true;
                        }

                    }
                    if (!isCode) {
                        Toast.makeText(getApplicationContext(), "Информация о точке не найдена", Toast.LENGTH_LONG).show();

                    }


                }
            } else {
                // This is important, otherwise the result will not be passed to the fragment
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
        else
        { Toast.makeText(getApplicationContext(), "Нет подключения к интернету",Toast.LENGTH_LONG).show();}

    }

    final Activity activity = this;

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        if (nInfo != null && nInfo.isConnected()) {
            Log.v("status", "ONLINE");
            return true;
        }
        else {
            Log.v("status", "OFFLINE");
            return false;
        }
    }

    private void getJSON(final String urlWebService) {
        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (s != null) {
                    try {

                        if (urlWebService==getcodes)
                            loadGpsCodes(s);

                        if (urlWebService==getCount)
                            loadcount(s);

                        if (urlWebService==getLast)
                            loadLast(s);

                        if (urlWebService==getControl)
                            loadcontrol(s);

                        if (urlWebService==updateStatus)
                            updateGenStatus(s);
                        if (urlWebService==getTimings)
                            loadTimings(s);


                        if (urlWebService==getSize)
                            loadsize(s);

                        if (urlWebService==takePass)
                            loadstatus(s);

//                        if (urlWebService==getcodesFormarkers)
//                            loadGpsCodesForMarkers(s);

                        if (urlWebService==killStatus)
                            updateGenStatus(s);

                        if (urlWebService==killStatus) updateGenStatus(s);

                        if (urlWebService==getCom) loadCom(s);

                        if (urlWebService==sbname)

                        {loadpassinfo(s);



                        }

                        if (urlWebService==codenames)
                        {
                            loadcodesinso(s);
                        }
                        if (urlWebService==getEvents)
                        {
                            loadevents(s);
                        }

                        if (urlWebService==getPersonal)
                        {
                            loadPersonalEvents(s);
                        }


                        if (urlWebService==getTime)
                        {
                            loadTime(s);
                        }

                        if (urlWebService==getTaken)
                        {
                            loadTakenPoints(s);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            protected String doInBackground(Void... voids) {


                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }

            }
        }


        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void loadCom(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);

        if (jsonArray.length() == 0) {
            Toast.makeText(getApplicationContext(),"Пароль не опознан",Toast.LENGTH_LONG).show();
            return;
        }

        String[] id_s = new String[jsonArray.length()];



        if (jsonArray.length() > 0) {
     //       for (int i = 0; i < jsonArray.length(); i++) {
//
              //  JSONObject obj = jsonArray.getJSONObject(i);
//                id_s[i]=obj.getString("id");

                    check=true;
                    String nick=mSettings.getString(APP_NICKNAME,"");
                    System.out.println(nick);
                    SharedPreferences.Editor editor=mSettings.edit();
                    editor.putString(APP_PREFERENCES_ID,"100");
                    editor.putString(APP_PREFERENCES_GENERAL,"Организатор");
                    editor.putString(APP_PREFERENCES_PASSWORD,first_password.getText().toString());
                    editor.putString(APP_IS_LOGIN,"1");
                    editor.putString(APP_ORGANIZATOR,"9");
                    editor.putString(APP_PREFERENCES_SIDE,"9");
                    editor.apply();
                    genname.setText(nick.toUpperCase()+", " + mSettings.getString(APP_PREFERENCES_GENERAL,""));
                    have_pass=mSettings.getString(APP_PREFERENCES_PASSWORD,"");
                    getJSON(getcodes);
                    String info=nick.toUpperCase()+" вошел в игру как организатор";
                    toLog(info);//запись в лог при входе
            startRL.setVisibility(GONE);
            mapview.setVisibility(View.VISIBLE);
            toppanel.setVisibility(View.VISIBLE);
            QR.setVisibility(View.GONE);
            restart.setVisibility(View.VISIBLE);
            backs.setVisibility(GONE);
            //}

        }
       // getJSON(getControl);
    }



    private void loadpassinfo(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);

        if (jsonArray.length() == 0) {
Toast.makeText(getApplicationContext(),"Пароль не опознан",Toast.LENGTH_LONG).show();
return;
        }

        String[] id_s = new String[jsonArray.length()];
        String[] gname_s = new String[jsonArray.length()];
        String[] gpass_s = new String[jsonArray.length()];
        String[] status_s = new String[jsonArray.length()];


        if (jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
//
                JSONObject obj = jsonArray.getJSONObject(i);
                id_s[i]=obj.getString("id");
                gname_s[i] = obj.getString("gname");
                gpass_s[i] = obj.getString("gpass");
                status_s[i] = obj.getString("status");
                int curStatus=Integer.parseInt(status_s[i]);
//                if (curStatus==1)
//                {
//                    Toast.makeText(getApplicationContext(),"Генерал уже в игре",Toast.LENGTH_LONG).show();
//                    return;
//                }
                if (gpass_s[i].equalsIgnoreCase(first_password.getText().toString())) {
                  check=true;
                    if (check)
                    {
                        startRL.setVisibility(GONE);
                        mapview.setVisibility(View.VISIBLE);
                        toppanel.setVisibility(View.VISIBLE);
                        int side=Integer.parseInt(mSettings.getString(APP_PREFERENCES_SIDE,"0"));
                        if (side<9){QR.setVisibility(View.VISIBLE);restart.setVisibility(View.GONE);}
                        if (side==9){restart.setVisibility(View.VISIBLE); QR.setVisibility(View.GONE);}

                        backs.setVisibility(GONE);
                        // new_login.setVisibility(View.GONE);

                    }
                    if (!check) {
                    }

                    String nick=mSettings.getString(APP_NICKNAME,"");
                    SharedPreferences.Editor editor=mSettings.edit();
                    editor.putString(APP_PREFERENCES_ID,id_s[i]);
                    editor.putString(APP_PREFERENCES_GENERAL,gname_s[i]);
                    editor.putString(APP_PREFERENCES_PASSWORD,gpass_s[i]);
                    editor.putString(APP_IS_LOGIN,"1");
                    if (gname_s[i].contains("Жел"))  editor.putString(APP_PREFERENCES_SIDE,"1");
                    if (gname_s[i].contains("Син"))  editor.putString(APP_PREFERENCES_SIDE,"2");
                    if (gname_s[i].contains("Сер"))  editor.putString(APP_PREFERENCES_SIDE,"3");
                    editor.apply();
                    genname.setText(nick.toUpperCase()+", " + mSettings.getString(APP_PREFERENCES_GENERAL,""));
                    have_pass=mSettings.getString(APP_PREFERENCES_PASSWORD,"");
                  //  getJSON(codenames);
                    getJSON(getcodes);
                    updateStatus="http://gamestrateg.ru/generals/update_points.php/get.php?nom="+gpass_s[i];
                    getJSON(updateStatus);
                    new updateNickAsyncTask().execute(gpass_s[i],nick);

                    String info=nick.toUpperCase()+" вошел в игру как "+gname_s[i];
                    toLog(info);//запись в лог при входе


                }

            }

        }
       // getJSON(getControl);
    }

    private void toLog(String info) {
        Date curdate = new Date();
        DateFormat formater = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss", Locale.UK);
        String isDate = formater.format(curdate);
        String add=info + " в "+isDate;
        new addInfoAsyncTask().execute(add);
    }

    private void updateGenStatus(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        if (jsonArray.length() == 0) {

        }

    }


    private void loadTimings(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        if (jsonArray.length() == 0) {

        }
        String[] status_s = new String[jsonArray.length()];
        String[] yellow_s = new String[jsonArray.length()];
        String[] blue_s = new String[jsonArray.length()];
        String[] grey_s = new String[jsonArray.length()];
        String[] control_s = new String[jsonArray.length()];
        int yellow=0,blue=0,grey=0;


        if (jsonArray.length() > 0) {
            for (int i = 0; i < (jsonArray.length()-7); i++) {
//
                JSONObject obj = jsonArray.getJSONObject(i);
                status_s[i] = obj.getString("status");
                yellow_s[i] = obj.getString("yellow");
                blue_s[i] = obj.getString("blue");
                grey_s[i] = obj.getString("grey");
                control_s[i] = obj.getString("control");
                if (yellow_s[i].equals("null")||yellow_s[i].equals("NULL")) yellow_s[i]="0";
                if (blue_s[i].equals("null")||blue_s[i].equals("NULL")) blue_s[i]="0";
                if (grey_s[i].equals("null")||grey_s[i].equals("NULL")) grey_s[i]="0";

                yellow+=Integer.parseInt(yellow_s[i]);
                blue+=Integer.parseInt(blue_s[i]);
                grey+=Integer.parseInt(grey_s[i]);
                int status=Integer.parseInt(status_s[i]);
                if (control_s[i].equals("NULL")||control_s[i].equals("null")||control_s[i]==null||control_s[i]=="") control_s[i]="0";
                if (status==1) yellow+=Integer.parseInt(control_s[i]);
                if (status==2) blue+=Integer.parseInt(control_s[i]);
                if (status==3) grey+=Integer.parseInt(control_s[i]);

            }
       //     Log.d("Timings", "Yellow: "+yellow+", Blue: "+blue+", Grey: "+grey);
            yellowtime.setText("Общее время контроля Единства: "+yellow+" мин.");
            bluetime.setText("Общее время контроля Коалиции: "+blue+" мин.");
            greytime.setText("Общее время контроля Союза: "+grey+" мин.");


        }
    }

    private void loadsize(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        if (jsonArray.length() == 0) {

        }
        String[] id_s = new String[jsonArray.length()];
        if (jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
//
                JSONObject obj = jsonArray.getJSONObject(i);
                id_s[i] = obj.getString("id");

            }
            SharedPreferences.Editor editor =mSettings.edit();
            editor.putString(APP_POINTS_SIZE,id_s[0]);
            editor.apply();
        }
    }

    private void loadLast(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        Date date1, date2;
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
        double isBegin, isEnd;
        lastDate = "00-00-00 00:00:00";
        if (jsonArray.length() == 0) {
            String[] cleartake = mSettings.getString(APP_PREFERENCES_TAKEN, "").split("#");

        }
        String[] date_s = new String[jsonArray.length()];
        String[] time_s = new String[jsonArray.length()];

        String codelist = "";
//        int sidecheck=Integer.parseInt(mSettings.getString(APP_PREFERENCES_ID,""));


        if (jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
//
                JSONObject obj = jsonArray.getJSONObject(i);
                date_s[i] = obj.getString("date");
                time_s[i] = obj.getString("time");

            }
            lastDate = date_s[0] + " " + time_s[0];


        String[] upload = mSettings.getString(APP_PREFERENCES_TAKEN, "").split("#");
        //doCancel("SHA");
        String[] splitting = upload[upload.length - 1].split(",");
        String newDate = splitting[3] + " " + splitting[4];
        String point_id = splitting[2];
        String curpass=mSettings.getString(APP_PREFERENCES_PASSWORD,"");
        String side = mSettings.getString(APP_PREFERENCES_SIDE, "0");
        String genId = "";
        try {

            date1 = formater.parse(lastDate);
            date2 = formater.parse(newDate);

            isBegin = date1.getTime();
            isEnd = date2.getTime();

            //Toast.makeText(getApplicationContext(), "Текущая датаЖ " + date1 + ", " + date2, Toast.LENGTH_LONG).show();
            if (isEnd >= isBegin)
            // Toast.makeText (getApplicationContext(), ""+"Должно быть нормально "+point_id+","+side,Toast.LENGTH_LONG).show();
            {
                //new RemoveAsyncTask().execute(genId);
                new UpdateOwnerAsyncTask().execute(side, point_id);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        SharedPreferences.Editor createEdit=mSettings.edit();
        createEdit.putString(APP_IS_ONLINE,"1");
        createEdit.apply();



    }

    private void loadGpsCodes(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        if (jsonArray.length() == 0) {}
        String[] id_s = new String[jsonArray.length()];
        String[] code_s = new String[jsonArray.length()];
        String[] name_s = new String[jsonArray.length()];
        String[] lat_s = new String[jsonArray.length()];
        String[] lon_s = new String[jsonArray.length()];
        String[] status_s = new String[jsonArray.length()];
        String[] time_s = new String[jsonArray.length()];
        String[] control_s = new String[jsonArray.length()];
        String codelist="";
        String times = null;
//        int sidecheck=Integer.parseInt(mSettings.getString(APP_PREFERENCES_ID,""));


        if (jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
//
                JSONObject obj = jsonArray.getJSONObject(i);
                id_s[i] = obj.getString("id");
                name_s[i] = obj.getString("name");
                code_s[i] = obj.getString("code");
                lat_s[i] = obj.getString("lat");
                lon_s[i] = obj.getString("lon");
                status_s[i] = obj.getString("status");
                time_s[i] = obj.getString("timeup");
                control_s[i]=obj.getString("control");


                String chars="";
//                chars+=code_s[i].charAt(0);
//                if ((chars.equals("A") && sidecheck>=1 && sidecheck<=10) || (chars.equals("B") && sidecheck>10 && sidecheck<=11) || chars.equals("C"))
//                {
                    codelist=codelist+name_s[i]+","+code_s[i]+","+lat_s[i]+","+lon_s[i]+","+status_s[i]+","+id_s[i]+"#";
                  times=time_s[i]+","+control_s[i];

                    }
            if (codelist.length()>10) 
            {
                SharedPreferences.Editor editor=mSettings.edit();
            editor.putString(APP_PREFERENCES_POINTS,codelist);
            editor.putString(APP_PREFERENCES_TIME,times);
            editor.apply();
            }

            String basa=mSettings.getString(APP_PREFERENCES_POINTS,"");
            String [] allcodes=basa.split("#");
            for (int i=0;i<allcodes.length;i++)
            {
            }
            //getMarkers();
            String base = mSettings.getString(APP_PREFERENCES_POINTS, "0,0,0,0,0,0");
            String[] splitting = removeLastChar(base).split("#");

            for (int j = 0; j < splitting.length; j++){}
            if (splitting != null) {
                for (int j = 0; j < splitting.length; j++) {
                    String[] currentmarker = splitting[j].split(",");
                    int sideMarker = Integer.parseInt(currentmarker[4]);



                    if (markers[j]!=null) {
                        int owner=Integer.parseInt(mSettings.getString(APP_ORGANIZATOR,"0"));

//                       if (owner>0) {markers[j].setSnippet(currentmarker[1]);
//                         //  System.out.println(""+owner+","+currentmarker[1]+","+markers[j].getSnippet());
//                       }

                        if (j<23) {
                            if (sideMarker == 0) {
                                markers[j].setIcon(getBitmapHighDescriptor(R.drawable.blackicon));
                                markers[j].setTag("black");
                            }
                            if (sideMarker == 1)
                            {markers[j].setIcon(getBitmapHighDescriptor(R.drawable.yellowicon));
                            markers[j].setTag("yellow");}
                            if (sideMarker == 2)
                            {markers[j].setIcon(getBitmapHighDescriptor(R.drawable.blueicon));
                            markers[j].setTag("blue");}
                            if (sideMarker == 3)
                            {  markers[j].setIcon(getBitmapHighDescriptor(R.drawable.greyicon));
                            markers[j].setTag("grey");}
                        }
                        else
                        {
                            if (sideMarker == 0) {
                                markers[j].setIcon(getBitmapStarsDescriptor(R.drawable.blackstar));
                                markers[j].setTag("black");
                            }
                            if (sideMarker == 1)
                            {markers[j].setIcon(getBitmapStarsDescriptor(R.drawable.yellowstar));
                            markers[j].setTag("yellow");}
                            if (sideMarker == 2)
                            {markers[j].setIcon(getBitmapStarsDescriptor(R.drawable.bluestar));
                            markers[j].setTag("blue");}
                            if (sideMarker == 3)
                            {   markers[j].setIcon(getBitmapStarsDescriptor(R.drawable.greystar));
                            markers[j].setTag("grey");}
                        }
                    }
//                if (markers[j]!=null)
//                {
//                    markers[j].setIcon();
//                }
                }
            }



        }
    }
    public static String removeFirstChar(String s) {
        return (s == null || s.length() == 0) ? "" : (s.substring(1));
    }
ArrayList <String> markerlist=new ArrayList<String>();
//    private void loadGpsCodesForMarkers(String json) throws JSONException {
//        initOverlay();
//        String basa=mSettings.getString(APP_PREFERENCES_POINTS,"");
//        String [] allcodes=basa.split("#");
//        for (int i=0;i<allcodes.length;i++)
//        {
//            markerlist.add(allcodes[i]);
//        }
//
//        for (int i=0;i<markers.length;i++)
//        {
//            if (markers[i]!=null) markers[i].remove();
//        }
//        JSONArray jsonArray = new JSONArray(json);
//        String[] id_s = new String[jsonArray.length()];
//        String[] code_s = new String[jsonArray.length()];
//        String[] name_s = new String[jsonArray.length()];
//        String[] lat_s = new String[jsonArray.length()];
//        String[] lon_s = new String[jsonArray.length()];
//        String[] status_s = new String[jsonArray.length()];
//        String[] time_s = new String[jsonArray.length()];
//        String[] control_s = new String[jsonArray.length()];
//        String codelist="";
////        int sidecheck=Integer.parseInt(mSettings.getString(APP_PREFERENCES_ID,""));
//
//
//        if (jsonArray.length() > 0) {
////mMap.clear();
//            for (int i = 0; i < jsonArray.length(); i++) {
//
//                JSONObject obj = jsonArray.getJSONObject(i);
//                id_s[i]=obj.getString("id");
//                name_s[i] = obj.getString("name");
//                code_s[i] = obj.getString("code");
//                lat_s[i] = obj.getString("lat");
//                lon_s[i] = obj.getString("lon");
//                status_s[i] = obj.getString("status");
//                time_s[i] = obj.getString("timeup");
//                control_s[i]=obj.getString("control");
//
//
//                String chars="";
////                chars+=code_s[i].charAt(0);
////                if ((chars.equals("A") && sidecheck>=1 && sidecheck<=10) || (chars.equals("B") && sidecheck>10 && sidecheck<=11) || chars.equals("C"))
////                {
//                codelist=codelist+name_s[i]+","+code_s[i]+","+lat_s[i]+","+lon_s[i]+","+status_s[i]+","+id_s[i]+"#";
//                SharedPreferences.Editor editor=mSettings.edit();
//                editor.putString(APP_PREFERENCES_POINTS,codelist);
//                //editor.putString(APP_PREFERENCES_TIME,time_s[i]);
//                editor.apply();}
//
////                if (gpass_s[i].equals(first_password.getText().toString())) {
////                    check = true;
////                }
//
//            //}
//
//            for (int i=0;i<markerlist.size();i++)
//            {
//
//                String[] currentmarker=markerlist.get(i).split(",");
//
//int sideMarker=Integer.parseInt(currentmarker[4]);
//if (sideMarker==0)
//{ markers[i]=(mMap.addMarker(new MarkerOptions()
//                        .position(new LatLng(Double.parseDouble(currentmarker[2]), Double.parseDouble(currentmarker[3])))
//                        .title("Точка "+(i+1))
//                        .icon(getBitmapHighDescriptor(R.drawable.blackicon))));}
//
//                if (sideMarker==1)
//                { markers[i]=(mMap.addMarker(new MarkerOptions()
//                        .position(new LatLng(Double.parseDouble(currentmarker[2]), Double.parseDouble(currentmarker[3])))
//                        .title("Точка "+(i+1))
//                        .icon(getBitmapHighDescriptor(R.drawable.yellowicon))));}
//                if (sideMarker==2)
//                { markers[i]=(mMap.addMarker(new MarkerOptions()
//                        .position(new LatLng(Double.parseDouble(currentmarker[2]), Double.parseDouble(currentmarker[3])))
//                        .title("Точка "+(i+1))
//                        .icon(getBitmapHighDescriptor(R.drawable.blueicon))));}
//                if (sideMarker==3)
//                { markers[i]=(mMap.addMarker(new MarkerOptions()
//                        .position(new LatLng(Double.parseDouble(currentmarker[2]), Double.parseDouble(currentmarker[3])))
//                        .title("Точка "+(i+1))
//                        .icon(getBitmapHighDescriptor(R.drawable.greyicon))));}
//
//            }
//        }
//    }


    private void loadcontrol(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        if (jsonArray.length() == 0) {

        }

        SharedPreferences.Editor editor=mSettings.edit();
        String[] control_s = new String[jsonArray.length()];

        if (jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
//
                JSONObject obj = jsonArray.getJSONObject(i);
                control_s[i] = obj.getString("control");
              if (!control_s[i].equals(getString(R.string.control)))
              {
                  Log.d("isControl", "Контроль не пройден");
              if (!isStart)
                  showResetAllDialog("Используется устаревшая версия, скачайте и установите новую версию");
              isStart=true;
                  //resetAll();
              }
              else
              {Log.d("isControl", "Контроль пройден");}
                }

        }
    }

    private void loadcount(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String genId = mSettings.getString(APP_PREFERENCES_ID, "0");
        String[] upload = removeLastChar(mSettings.getString(APP_PREFERENCES_TAKEN, "")).split("#");

        if (jsonArray.length() == 0) {

        }


        String[] count_s = new String[jsonArray.length()];
        if (jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
//
                JSONObject obj = jsonArray.getJSONObject(i);
               count_s[i] = obj.getString("count");
               String [] isTaken=mSettings.getString(APP_PREFERENCES_TAKEN,"").split("#");
               int isBase=Integer.parseInt(count_s[i]);
               int isSoft=isTaken.length;
               if (isSoft>isBase)
               {


                   try {
                       Thread.sleep(200);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   String uniq=mSettings.getString(APP_IS_UNIQKEY,"");
                   if (uniq.length()>4) new RemoveAsyncTask().execute(genId,uniq);
                   try {
                       Thread.sleep(200);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   String value="";
                   for (int t = 0; t < upload.length; t++) {

                       String[] splits = upload[t].split(",");
                       String thisnickname=mSettings.getString(APP_NICKNAME,"");
                       String uniqkey=mSettings.getString(APP_IS_UNIQKEY,"");
                       if (uniqkey.length()>4) {
                           value = value + "('" + splits[0] + "','" + splits[1] + "','" + splits[2] + "','" + splits[3] + "','" + splits[4]
                                   + "','" + splits[5] + "','" + splits[6] + "','" + thisnickname + "','"+ uniqkey+"'),";
                       }


                       // new UpdateAsyncTask().execute(splits[0], splits[1], splits[2], splits[3], splits[4], splits[5], splits[6]);


                   }
                   new UpdateAsyncTask().execute(removeLastChar(value));

               }
            }

        }
    }

    private void loadcodesinso(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        if (jsonArray.length() == 0) {

        }


        String[] code_s = new String[jsonArray.length()];
        String[] name_s = new String[jsonArray.length()];
        String codelist="";
        int sidecheck=Integer.parseInt(mSettings.getString(APP_PREFERENCES_ID,""));


        if (jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
//
                JSONObject obj = jsonArray.getJSONObject(i);
                code_s[i] = obj.getString("code");
                name_s[i] = obj.getString("name");
                String chars="";
                chars+=code_s[i].charAt(0);
                if ((chars.equals("A") && sidecheck>=1 && sidecheck<=10) || (chars.equals("B") && sidecheck>10 && sidecheck<=11) || chars.equals("C"))
                {
                    codelist=codelist+code_s[i]+","+name_s[i]+"#";
                    SharedPreferences.Editor editor=mSettings.edit();
                    editor.putString(APP_PREFERENCES_CODES,codelist);
                    editor.apply();}

//                if (gpass_s[i].equals(first_password.getText().toString())) {
//                    check = true;
//                }

            }

        }
    }


    private void loadTime(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        if (jsonArray.length() == 0) {

                showDialog("Считан ошибочный код");
            Toast.makeText(getApplicationContext(),"Считан ошибочный код",Toast.LENGTH_SHORT).show();
        }

        String[] time_s = new String[jsonArray.length()];
        String[] diff_s = new String[jsonArray.length()];
        String[] code_s = new String[jsonArray.length()];
        String[] name_s = new String[jsonArray.length()];
        if (jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
//
                JSONObject obj = jsonArray.getJSONObject(i);
                code_s[i] = obj.getString("pointcode");
                name_s[i] = obj.getString("pointname");
                time_s[i] = obj.getString("timed");
                diff_s[i] = obj.getString("diff");
            }
            int diff=Integer.parseInt(diff_s[0])*60;
            String [] splitting=time_s[0].split(":");
            int timehour=Integer.parseInt(splitting[0]);
            int timemin=Integer.parseInt(splitting[1]);
            int timesec=Integer.parseInt(splitting[2]);
            int commontime=timehour*3600+timemin*60+timesec;
            int lefttime=commontime-diff;
            timemin=Math.abs(lefttime/60);
            timesec=Math.abs(lefttime)-timemin*60;
            if (lefttime<0) {Toast.makeText(getApplicationContext(),"До возможности взятия осталось "
                            +Math.abs(timemin)+ " мин. "+timesec+ " сек.",Toast.LENGTH_LONG).show();}
            else {
                String  getInfo=mSettings.getString(APP_PREFERENCES_INFO,"");
               // Toast.makeText(getApplicationContext(), "можно брать", Toast.LENGTH_LONG).show();
                Date currentDate = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                String dateText = dateFormat.format(currentDate);
                DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                String timeText = timeFormat.format(currentDate);

                String summary=name_s[0]+"#"+dateText+"#"+timeText+",";
                String toList = name_s[0] +" взята\n"+dateText + " в " + timeText;
                String toArray=mSettings.getString(APP_PREFERENCES_GENERAL,"")+": " +name_s[0]+" взята "+dateText + " в " + timeText;
                new AddAsyncTask().execute(toArray,code_s[0]);
           //     Toast.makeText(getApplicationContext(),toArray+", "+code_s[0],Toast.LENGTH_LONG).show();
//                get.add(0,toList);
//                ArrayAdapter adapter = new ArrayAdapter(this,
//                        R.layout.list_item, get);
//
//                list.setAdapter(adapter);
                SharedPreferences.Editor editor=mSettings.edit();
                editor.putString(APP_PREFERENCES_INFO,getInfo+summary);
                editor.apply();

                    showDialog("Точка захвачена");
            }
        }
    }

    private void loadstatus(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        if (jsonArray.length() == 0) {
            if (!mSettings.getString(APP_PREFERENCES_SIDE,"0").equals("9"))
            mSettings.edit().clear().apply();
            first_password.setText("");
            secRL.setVisibility(GONE);
            //mapview.setVisibility(GONE);
            RL2.setVisibility(GONE);
            startRL.setVisibility(View.VISIBLE);
        }
        String[] status_s = new String[jsonArray.length()];
        if (jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
//
                JSONObject obj = jsonArray.getJSONObject(i);
                status_s[i] = obj.getString("status");
                SharedPreferences.Editor editor=mSettings.edit();
                editor.putString(APP_STATUS,status_s[i]);
                editor.apply();
                int status=Integer.parseInt(status_s[i]);
                if (status==0)
                {
                    check=false;
                    if (!mSettings.getString(APP_PREFERENCES_SIDE,"0").equals("9"))
                    mSettings.edit().clear().apply();
                    first_password.setText("");
                    mapview.setVisibility(GONE);
                    toppanel.setVisibility(GONE);
                    QR.setVisibility(GONE);
                    backs.setVisibility(View.VISIBLE);
                    secRL.setVisibility(GONE);
                    //mapview.setVisibility(GONE);
                    RL2.setVisibility(GONE);
                    startRL.setVisibility(View.VISIBLE);
                    SharedPreferences.Editor editir = mSettings.edit();
                    editir.putString(APP_PREFERENCES_ID,"");
                    editir.putString(APP_IS_LOGIN,"0");
                    editir.apply();
                }
                if (status==1)
                {
                    startRL.setVisibility(GONE);
                    mapview.setVisibility(View.VISIBLE);

                }
            }

        }


    }

    private void loadTakenPoints(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        if (jsonArray.length() == 0){}
        String take="";
        String version=getString(R.string.control);

        String[] codename_s = new String[jsonArray.length()];
        String[] status_s = new String[jsonArray.length()];
        String[] control_s = new String[jsonArray.length()];
        String[] diff_s = new String[jsonArray.length()];


        if (jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
//
                JSONObject obj = jsonArray.getJSONObject(i);
                codename_s[i] = obj.getString("codename");
                status_s[i] = obj.getString("status");
                control_s[i] = obj.getString("control");
                diff_s[i] = obj.getString("diff");
                take=take+codename_s[i]+","+status_s[i]+","+diff_s[i]+"#";



            }
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString(APP_PREFERENCES_CONTROL,take);
            editor.apply();


        }


    }





    private class UpdateAsyncTask extends AsyncTask<String, Integer, Double> {
        @Override
        protected Double doInBackground(String... params) {
            postData(params[0]);
            return null;
        }

        protected void onPostExecute(Double result) {

        }

        protected void onProgressUpdate(Integer... progress) {
        }

        public void postData(String value) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(addtolog);
            System.out.println(addtolog+", "+value);

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("value",value ));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                HttpResponse response = httpclient.execute(httppost);
            } catch (ClientProtocolException e) {

            } catch (IOException e) {
            }

        }
    }

    private class UpdateOwnerAsyncTask extends AsyncTask<String, Integer, Double> {
        @Override
        protected Double doInBackground(String... params) {
            postData(params[0],params[1]);
            return null;
        }

        protected void onPostExecute(Double result) {

        }

        protected void onProgressUpdate(Integer... progress) {
        }

        public void postData(String status, String point_id ) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(updateOwner);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("status",status));
                nameValuePairs.add(new BasicNameValuePair("point_id",point_id ));
              //  nameValuePairs.add(new BasicNameValuePair("cancellable",cancellable));



                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                HttpResponse response = httpclient.execute(httppost);
           //     Toast.makeText(getApplicationContext(),"скрипт выполнен, "+point_id+", "+status,Toast.LENGTH_LONG).show();
            } catch (ClientProtocolException e) {

            } catch (IOException e) {
            }

        }
    }

    private class RemoveAsyncTask extends AsyncTask<String, Integer, Double> {
        @Override
        protected Double doInBackground(String... params) {
            postData(params[0],params[1]);
            return null;
        }

        protected void onPostExecute(Double result) {

        }

        protected void onProgressUpdate(Integer... progress) {
        }

        public void postData(String id, String uniq) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(removeOld);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("id",id ));
                nameValuePairs.add(new BasicNameValuePair("uniq",uniq ));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                HttpResponse response = httpclient.execute(httppost);
            } catch (ClientProtocolException e) {

            } catch (IOException e) {
            }

        }
    }


    private class testAsyncTask extends AsyncTask<String, Integer, Double> {
        @Override
        protected Double doInBackground(String... params) {
            postData(params[0], params[1]);
            return null;
        }

        protected void onPostExecute(Double result) {

        }

        protected void onProgressUpdate(Integer... progress) {
        }

        public void postData(String id, String cur) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(matter);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("status",id ));
                nameValuePairs.add(new BasicNameValuePair("nom",cur ));


                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                HttpResponse response = httpclient.execute(httppost);
            } catch (ClientProtocolException e) {

            } catch (IOException e) {
            }

        }
    }


    private void loadevents(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        ArrayList <String> get=new ArrayList<>();
        if (jsonArray.length() == 0) {
            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item, get);
            genlist.setAdapter(adapter);
        }


        String[] general_s = new String[jsonArray.length()];
        String[] point_s = new String[jsonArray.length()];
        String[] date_s = new String[jsonArray.length()];
        String[] time_s = new String[jsonArray.length()];
        String[] side_s = new String[jsonArray.length()];
        String[] generals_id_s = new String[jsonArray.length()];
        String[] nickname_s = new String[jsonArray.length()];

        if (jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
//
                JSONObject obj = jsonArray.getJSONObject(i);

                general_s[i] = obj.getString("general");
                point_s[i] = obj.getString("point");
                date_s[i] = obj.getString("date");
                time_s[i] = obj.getString("time");
                side_s[i] = obj.getString("side");
                generals_id_s[i] = obj.getString("generals_id");
                nickname_s[i] = obj.getString("nickname");
                int isSide=Integer.parseInt(side_s[i]);
                if (isSide<9)
                {String toAdd=date_s[i]+ " " +time_s[i]+"\n"+
                        "Позывной: "+nickname_s[i].toUpperCase()+",\nРоль: "+general_s[i]+":\n"+point_s[i]+" захвачена\n";
                    get.add(0,toAdd);
                }
                if (isSide==9)
                {
                    int color=Integer.parseInt(generals_id_s[i]);
                    String logColor="";
                    switch (color) {
                        case 100:
                            logColor="переведена под контроль Единства";
                            break;
                        case 200:
                            logColor="переведена под контроль  Коалиции";
                            break;
                        case 300:
                            logColor="переведена под контроль Союза";
                            break;
                        case 400:
                            logColor="нейтральная ";
                            break;
                    }
                    String toAdd=date_s[i]+" "+time_s[i]+"\n"+ "Позывной: "+nickname_s[i].toUpperCase()+",\nРоль: "+general_s[i]+"\n"+point_s[i]+" "+logColor;

               //   String toAdd="Позывной: "+nickname_s[i].toUpperCase()+",\nРоль: "+general_s[i]+":\n"+point_s[i]+" "+logColor+" "+date_s[i]+ " в " +time_s[i];
                    get.add(0,toAdd);
                }

            }

            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item, get);
            genlist.setAdapter(adapter);

        }
    }


    private void loadPersonalEvents(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        ArrayList <String> get=new ArrayList<>();
        if (jsonArray.length() == 0) {
            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item, get);
            list.setAdapter(adapter);
        }


        String[] general_s = new String[jsonArray.length()];
        String[] point_s = new String[jsonArray.length()];
        String[] date_s = new String[jsonArray.length()];
        String[] time_s = new String[jsonArray.length()];
        String[] side_s = new String[jsonArray.length()];
        String[] generals_id_s = new String[jsonArray.length()];
        String[] nickname_s = new String[jsonArray.length()];

        if (jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
//
                JSONObject obj = jsonArray.getJSONObject(i);

                general_s[i] = obj.getString("general");
                point_s[i] = obj.getString("point");
                date_s[i] = obj.getString("date");
                time_s[i] = obj.getString("time");
                side_s[i] = obj.getString("side");
                generals_id_s[i] = obj.getString("generals_id");
                nickname_s[i] = obj.getString("nickname");
                int isSide=Integer.parseInt(side_s[i]);
                if (isSide<9 && generals_id_s[i].equals(mSettings.getString(APP_PREFERENCES_ID,"")))
                {String toAdd=date_s[i]+ " " +time_s[i]+":\n"+
                        " захвачена "+point_s[i];
                    get.add(0,toAdd);

                }
                if (isSide==9)
                {
                    int color=Integer.parseInt(generals_id_s[i]);
                    String logColor="";
                    switch (color) {
                        case 100:
                            logColor="переведена под контроль Единства";
                            break;
                        case 200:
                            logColor="переведена под контроль  Коалиции";
                            break;
                        case 300:
                            logColor="переведена под контроль Союза";
                            break;
                        case 400:
                            logColor="нейтральная ";
                            break;
                    }
                    String toAdd=date_s[i]+" "+time_s[i]+"\n"
                            + "Позывной: "+nickname_s[i].toUpperCase()+"\n"+point_s[i]+" "+logColor;

                    //   String toAdd="Позывной: "+nickname_s[i].toUpperCase()+",\nРоль: "+general_s[i]+":\n"+point_s[i]+" "+logColor+" "+date_s[i]+ " в " +time_s[i];
                    get.add(0,toAdd);
                }

            }

            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item, get);
            list.setAdapter(adapter);

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap=googleMap;

      //  updateLocationUI();
       // LatLng anomaly1 = new LatLng(53, 158);//Алабино
//        LatLng position=new LatLng(55.086091,38.180888);//Дача
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.getUiSettings().setRotateGesturesEnabled(false);

        initOverlay();

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position)
                .zoom(mapzoom)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.animateCamera(cameraUpdate);
        float getZoom=mMap.getCameraPosition().zoom;
        getMarkers();




        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                coordtext.setText(""+latLng.latitude+", "+latLng.longitude);
                RLComList.setVisibility(GONE);
                RLList.setVisibility(GONE);
                showComonList=false;
                showlist=false;


               // Toast.makeText(context,""+latLng.latitude+", "+latLng.longitude,Toast.LENGTH_LONG).show();
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String basa=mSettings.getString(APP_PREFERENCES_POINTS,"");
                int point_id=0;
                String [] allcodes=basa.split("#");
                String snippet=marker.getSnippet();
                String title = marker.getTitle();
                double distance=0;
                if (imhere!=null) {
                    for (int i=0;i<allcodes.length;i++)
                    {
                        if (allcodes[i].contains(title))
                        {
                            String [] splitting=allcodes[i].split(",");
                            Log.d("PointData", allcodes[i]);
                            double lat=Double.parseDouble(splitting[2]);
                            double lon=Double.parseDouble(splitting[3]);
                            distance = Utils.distanceInKmBetweenEarthCoordinates(commonLat, commonLon, lat, lon);
                            marker.setSnippet("Дистанция: "+String.format("%.0f", (distance*1000))+ " м.");

                        }


                    }
                }
                for (int i=0;i<allcodes.length;i++)
                {
                    if (allcodes[i].contains(title)) point_id=i+1;
               //     Log.d("Point", title+", "+point_id);

                }
               // System.out.println("Get snippet: "+snippet);
                int  owner=Integer.parseInt(mSettings.getString(APP_ORGANIZATOR,"0"));

                if (owner>0) showChangeSideDialog(marker, title, point_id);
                return false;
            }
        });

        double [] lats=
                {
                        55.5441,
                        55.5355,
                        55.5457,
                        55.5365,
                        55.5465,
                        55.5386,
                        55.5372,
                        55.5352,
                        55.5347,
                        55.5338,
                        55.5429,
                        55.5477,
                        55.5378,
                        55.5401,
                        55.5444,

                };

        double [] lons=
                {
                        36.9444,
                        36.9366,
                        36.9486,
                        36.9299,
                        36.9353,
                        36.9273,
                        36.9458,
                        36.9423,
                        36.9256,
                        36.9372,
                        36.9258,
                        36.9373,
                        36.9479,
                        36.931,
                        36.932,


                };

//        Marker [] markers=new Marker[lats.length];
//        for (int i=0;i<markers.length;i++)
//        {
//            markers[i]=(mMap.addMarker(new MarkerOptions()
//                    .position(new LatLng(lats[i], lons[i]))
//                    .title("Точка "+(i+1))
//                    .icon(getBitmapHighDescriptor(R.drawable.blackicon))));
//
//
//        }



    }

    private BitmapDescriptor getBitmapHighDescriptor(int id) {
        Drawable vectorDrawable = ContextCompat.getDrawable(getApplicationContext(), id);
        vectorDrawable.setBounds(0, 0, 30, 40);
        Bitmap bm = Bitmap.createBitmap(30, 40, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bm);
    }

    private BitmapDescriptor getBitmapStarsDescriptor(int id) {
        Drawable vectorDrawable = ContextCompat.getDrawable(getApplicationContext(), id);
        vectorDrawable.setBounds(0, 0, 50, 70);
        Bitmap bm = Bitmap.createBitmap(50, 70, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bm);
    }

    private void initOverlay() {
        //Для Алабино
//        double north = 55.5506105133793;
//        double south = 55.52989324454769;
//        double east = 36.94961723644818;
//        double west = 36.92383041021363;
//        //Для Ступино
//        double north = 54.898229576129;
//        double south = 54.87007215863002;
//        double east =38.36802721194703;
//        double west = 38.33335746839403;

        //Для СНБ Осень

        double north = 55.55064457517451;
        double south = 55.52981273693565;
        double east =36.94959543453138;
        double west = 36.92376200151153;


        LatLng swMapCoord=new LatLng(south,west);
        LatLng neMapCoord=new LatLng(north,east);
        LatLngBounds bounds=new LatLngBounds(swMapCoord, neMapCoord);

        GroundOverlayOptions poligon = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.snbmap)))
               // .bearing(1.386194109916687f)
                .positionFromBounds(bounds);
        if (poligon!=null)
            mMap.addGroundOverlay(poligon);
            isMarker=(mSettings.getString(APP_TO_BASA,"")).replace('#','.');

    }
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            mMap.setMyLocationEnabled(true);
         //   mMap.getUiSettings().setMyLocationButtonEnabled(true);

        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    private class AddAsyncTask extends AsyncTask<String, Integer, Double> {
        @Override
        protected Double doInBackground(String... params) {
            postData(params[0],params[1]);
            return null;
        }
        protected void onPostExecute(Double result) {

        }
        protected void onProgressUpdate(Integer... progress) {
        }
        public void postData(String info, String code) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(add_taken);
            //Toast.makeText(getApplicationContext(),""+info+", "+code,Toast.LENGTH_LONG).show();
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("gen_info",info ));
                nameValuePairs.add(new BasicNameValuePair("code",code ));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                HttpResponse response = httpclient.execute(httppost);
            } catch (ClientProtocolException e) {

            } catch (IOException e) {
            }

        }
    }


    private class updateNickAsyncTask extends AsyncTask<String, Integer, Double> {
        @Override
        protected Double doInBackground(String... params) {
            postData(params[0],params[1]);
            return null;
        }

        protected void onPostExecute(Double result) {

        }

        protected void onProgressUpdate(Integer... progress) {
        }

        public void postData(String info, String code) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://gamestrateg.ru/generals/update_nick.php");
            //Toast.makeText(getApplicationContext(),""+info+", "+code,Toast.LENGTH_LONG).show();
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("genpass",info ));
                nameValuePairs.add(new BasicNameValuePair("gennick",code ));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                HttpResponse response = httpclient.execute(httppost);
            } catch (ClientProtocolException e) {

            } catch (IOException e) {
            }

        }
    }


    private class addInfoAsyncTask extends AsyncTask<String, Integer, Double> {
        @Override
        protected Double doInBackground(String... params) {
            postData(params[0]);
            return null;
        }

        protected void onPostExecute(Double result) {

        }

        protected void onProgressUpdate(Integer... progress) {
        }

        public void postData(String info) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://gamestrateg.ru/generals/add_game_log.php");
            //Toast.makeText(getApplicationContext(),""+info+", "+code,Toast.LENGTH_LONG).show();
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("info",info ));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                HttpResponse response = httpclient.execute(httppost);
            } catch (ClientProtocolException e) {

            } catch (IOException e) {
            }

        }
    }

    private class RemoveGenAsyncTask extends AsyncTask<String, Integer, Double> {
        @Override
        protected Double doInBackground(String... params) {
            postData(params[0]);
            return null;
        }

        protected void onPostExecute(Double result) {

        }

        protected void onProgressUpdate(Integer... progress) {
        }

        public void postData(String info) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://gamestrateg.ru/generals/remove_general.php");
            //Toast.makeText(getApplicationContext(),""+info+", "+code,Toast.LENGTH_LONG).show();
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("genpass",info ));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                HttpResponse response = httpclient.execute(httppost);
            } catch (ClientProtocolException e) {

            } catch (IOException e) {
            }

        }
    }


    private void deleteAppData() {
        mSettings.edit().clear().apply();
        startRL.setVisibility(View.VISIBLE);
        genname.setText("");
        mapview.setVisibility(GONE);
        toppanel.setVisibility(View.GONE);
        QR.setVisibility(View.GONE);
        backs.setVisibility(View.VISIBLE);
        dialog.dismiss();


    }

    private void resetAll()
    {
        Toast.makeText(getApplicationContext(),"Несовпадение версий", Toast.LENGTH_LONG).show();
                try {
            // clearing app data
            String packageName = getApplicationContext().getPackageName();
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("pm clear "+packageName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDialog(String message)
    {

        AlertDialog.Builder alert;
        AlertDialog dialog;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        {
            alert=new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        }
        else
        {
            alert=new AlertDialog.Builder(this);
        }
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogue,null);
        approve=view.findViewById(R.id.approve);
        mess=view.findViewById(R.id.mess);
        alert.setView(view);
        alert.setCancelable(false);
        mess.setText(message);
        dialog = alert.create();

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {dialog.dismiss();}
        });

        dialog.show();
    }

    private void showResetAllDialog(String message)
    {

        AlertDialog.Builder alert;
        AlertDialog dialog;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        {
            alert=new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        }
        else
        {
            alert=new AlertDialog.Builder(this);
        }
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogue,null);
        approve=view.findViewById(R.id.approve);
        mess=view.findViewById(R.id.mess);
        alert.setView(view);
        alert.setCancelable(false);
        mess.setText(message);
        dialog = alert.create();

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAll();
                dialog.dismiss();}
        });

        dialog.show();
    }

    AlertDialog dialog;
    AlertDialog restartdialog;

    private void showRestartDialog()
    {

        AlertDialog.Builder alert;
        Button restart, cancel;


        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        {
            alert=new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        }
        else
        {
            alert=new AlertDialog.Builder(this);
        }
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.orgresetdialog,null);
        restart=view.findViewById(R.id.approve);
        cancel=view.findViewById(R.id.cancel);
        alert.setView(view);
        alert.setCancelable(false);
        restartdialog = alert.create();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {restartdialog.dismiss();}
        });

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new uploadAsyncTask().execute("reset_option",mSettings.getString(APP_PREFERENCES_PASSWORD,"0"),"","","");
                restartdialog.dismiss();
            }
        });

        restartdialog.show();
    }


    private void showResetDialog()
    {

        AlertDialog.Builder alert;

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        {
            alert=new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        }
        else
        {
            alert=new AlertDialog.Builder(this);
        }
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.reset_dalogue,null);
        reset=view.findViewById(R.id.reset);
        cancelReset=view.findViewById(R.id.cancelReset);
        alert.setView(view);
        alert.setCancelable(false);
        dialog = alert.create();

        cancelReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {dialog.dismiss();}
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nick=mSettings.getString(APP_NICKNAME,"");
                String info=nick.toUpperCase()+" покинул игру";
                toLog(info);//запись в лог при выходе
                String gpass=mSettings.getString(APP_PREFERENCES_PASSWORD,"");
                killStatus="http://gamestrateg.ru/generals/update_status.php/get.php?nom="+gpass;
                //   new UpdateAsyncTask().execute(pwd);
                getJSON(killStatus);
                new RemoveGenAsyncTask().execute(gpass);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }



                deleteAppData();
            }
        });

        dialog.show();
    }
double commonLat,commonLon;
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            float getZoom=mMap.getCameraPosition().zoom;

            if (imhere!=null) imhere.remove();
            LatLng position=new LatLng(commonLat,commonLon);

            imhere=(mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(commonLat, commonLon))
                    .title("Я здесь")
                    .icon(getBitmapHighDescriptor(R.drawable.imhere))));

//getMarkers();
  //          removeMarkers();
            if (isOnline(getApplicationContext())) {
                int getOnline = Integer.parseInt(mSettings.getString(APP_IS_ONLINE, "0"));

                if (getOnline == 0) {
                   // uploadInfo();
                   // Toast.makeText(getApplicationContext(), "Производится выгрузка в базу после восстановления сети", Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor editor =mSettings.edit();
                    editor.putString(APP_IS_ONLINE,"1");
                    editor.apply();
                }

            }//Toast.makeText (getApplicationContext(), "isOnline",Toast.LENGTH_SHORT).show();
            if (!isOnline(getApplicationContext())) {}//Toast.makeText (getApplicationContext(), "isOffline",Toast.LENGTH_SHORT).show();
           //
            int size=Integer.parseInt(mSettings.getString(APP_POINTS_SIZE,"0"));
            Location location = intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION);
            if (location != null) {
double lat=Double.parseDouble(Utils.getLat(location));
                getStatus();
                double lon=Double.parseDouble(Utils.getLon(location));
              //  commonLat=lat;
            //    commonLon=lon;
                double distance=Utils.distanceInKmBetweenEarthCoordinates(commonLat,commonLon,37.421,-122.08);
                String convert=(""+distance*1000);
                //int finalDist=Integer.parseInt(convert[0]);
                String [] split=convert.split("\\.");

                String parsing=mSettings.getString(APP_PREFERENCES_INFO,"");
                if (parsing.length()>0)
                {
                    String [] splits=parsing.split("#");
                  //  for (int)

                }
              //  getJSON(getcodesFormarkers);
            }
        }
    }
    private static byte[] doMarkers (byte[] a, byte[] key) {
        byte[] out = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            out[i] = (byte) (a[i] ^ key[i%key.length]);
        }
        return out;
    }


    private boolean checkPermissions() {
        return  PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }


    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {

            Snackbar.make(
                    findViewById(R.id.maplayout),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(Login.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    })
                    .show();
        } else {

            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(Login.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.

            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
                //mService.requestLocationUpdates();
            } else {
                // Permission denied.
              //  setButtonsState(false);
                Snackbar.make(
                        findViewById(R.id.maplayout),
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",getPackageName(),null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        }
    }

    private void uploadsummary()
    {


        //new AddAsyncTask().execute(mSettings.getString(APP_PREFERENCES_ID,""),toArray,new_codename);

    }

    private void getStatus()

    {

        String org=mSettings.getString(APP_ORGANIZATOR,"0");
      //  Log.d("OrgFlag",org);
        if (org=="9") return;
        takePass="http://gamestrateg.ru/generals/get_gen_status.php/get.php?nom="+have_pass;
        getJSON(takePass);

    }

    private void uploadInfo() {


        String take=mSettings.getString(APP_PREFERENCES_TAKEN,"");
        if (take.length()<10) return;
        String genId = mSettings.getString(APP_PREFERENCES_ID, "0");
        String[] upload = removeLastChar(mSettings.getString(APP_PREFERENCES_TAKEN, "")).split("#");
        if (upload[0].length()>0) {
            String[] splitting = upload[upload.length-1].split(",");
            String point_id = splitting[2];
            getLast = "http://gamestrateg.ru/generals/get_last_by_point.php/get.php?nom=" + point_id;
            getJSON(getLast);
            String thisKey=mSettings.getString(APP_IS_UNIQKEY,"");
            getCount="http://gamestrateg.ru/generals/get_count.php/get.php?nom="+thisKey;
            getJSON(getCount);


        }
    }

    public static String removeLastChar(String s) {
        return (s == null || s.length() == 0) ? null : (s.substring(0, s.length() - 1));
    }

    public static String getCoordinates(String s, String key) {
        return new String(doMarkers(Base64.decode(s, 0), key.getBytes()));
    }


    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }

    private void checkEnabled() {
        Log. d("Status GPS:", "Enabled: "
                + locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER));
        Log.d("Status network:","Enabled: "
                + locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }

    private void removeMarkers()
    {
        String snippet="";
        getJSON(getControl);
        getJSON(getcodes);
        getJSON(getTimings);
    }


    private void getMarkers()
    {
        String base = mSettings.getString(APP_PREFERENCES_POINTS, "0,0,0,0,0");

        String[] splitting = base.split("#");

                    for (int j = 0; j < splitting.length; j++){}


        if (splitting != null) {
                        for (int j = 0; j < splitting.length; j++) {
                            String[] currentmarker = splitting[j].split(",");
                            int sideMarker = Integer.parseInt(currentmarker[4]);

                            String owner=mSettings.getString(APP_PREFERENCES_SIDE,"0");
                            System.out.println("Is marker: "+splitting[j]+", "+owner);
                            if (owner=="9") {

                            }
                            if (j<23) {
                                if (sideMarker == 0) {
                                    markers[j] = (mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(Double.parseDouble(currentmarker[2]), Double.parseDouble(currentmarker[3])))
                                            .title(currentmarker[0])
                                            //.snippet("")
                                            .icon(getBitmapHighDescriptor(R.drawable.blackicon))));

                                }
                                if (sideMarker == 1) {
                                    markers[j] = (mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(Double.parseDouble(currentmarker[2]), Double.parseDouble(currentmarker[3])))
                                            .title(currentmarker[0])
                                            .icon(getBitmapHighDescriptor(R.drawable.yellowicon))));
                                }
                                if (sideMarker == 2) {
                                    markers[j] = (mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(Double.parseDouble(currentmarker[2]), Double.parseDouble(currentmarker[3])))
                                            .title(currentmarker[0])
                                            .icon(getBitmapHighDescriptor(R.drawable.blueicon))));

                                }
                                if (sideMarker == 3) {
                                    markers[j] = (mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(Double.parseDouble(currentmarker[2]), Double.parseDouble(currentmarker[3])))
                                            .title(currentmarker[0])
                                            .icon(getBitmapHighDescriptor(R.drawable.greyicon))));
                                }
                            }
                            else
                            {
                                if (sideMarker == 0) {
                                    markers[j] = (mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(Double.parseDouble(currentmarker[2]), Double.parseDouble(currentmarker[3])))
                                            .title(currentmarker[0])
                                            //.snippet("")
                                            .icon(getBitmapStarsDescriptor(R.drawable.blackstar))));

                                }
                                if (sideMarker == 1) {
                                    markers[j] = (mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(Double.parseDouble(currentmarker[2]), Double.parseDouble(currentmarker[3])))
                                            .title(currentmarker[0])
                                            .icon(getBitmapStarsDescriptor(R.drawable.yellowstar))));
                                }
                                if (sideMarker == 2) {
                                    markers[j] = (mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(Double.parseDouble(currentmarker[2]), Double.parseDouble(currentmarker[3])))
                                            .title(currentmarker[0])
                                            .icon(getBitmapStarsDescriptor(R.drawable.bluestar))));
                                }
                                if (sideMarker == 3) {
                                    markers[j] = (mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(Double.parseDouble(currentmarker[2]), Double.parseDouble(currentmarker[3])))
                                            .title(currentmarker[0])
                                            .icon(getBitmapStarsDescriptor(R.drawable.greystar))));
                                }

                            }
                        }
                    }
                }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    "dd:MMMM:yyyy HH:mm:ss a", Locale.getDefault());
            final String strDate = simpleDateFormat.format(calendar.getTime());

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    String idcheck=mSettings.getString(APP_PREFERENCES_ID,"");
                    String gencheck=mSettings.getString(APP_PREFERENCES_GENERAL,"");
                    String nick=mSettings.getString(APP_NICKNAME,"");
                    int side=Integer.parseInt(mSettings.getString(APP_PREFERENCES_SIDE,"0"));
                    String owner=mSettings.getString(APP_ORGANIZATOR,"");
                  //  Log.d("isOwner",owner);
                    if (idcheck!="")  {
                        startRL.setVisibility(GONE);
                        //genName="Генерал "+mSettings.getString(APP_PREFERENCES_ID,"")+": ";

                        //  new_login.setVisibility(View.GONE);
                        // showMap.setVisibility(GONE);
                        //
                        genname.setText(nick.toUpperCase() + ", " + gencheck);
                        //        //secRL.setVisibility(View.VISIBLE);}
                        mapview.setVisibility(View.VISIBLE);
                        toppanel.setVisibility(View.VISIBLE);
                        if (side<9) QR.setVisibility(View.VISIBLE);
                        if (side==9) restart.setVisibility(View.VISIBLE);
                        backs.setVisibility(GONE);

                   //     uploadInfo();
                        getStatus();
                    }
                    removeMarkers();

                }
            });
        }
    }

    private void invLog()
    {
        RLComList.setVisibility(GONE);
        RLList.setVisibility(GONE);
    }

//    private void doCancel(String cancellable) {
//        PackageInfo info;
//        try {
//            info = getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md;
//                md = MessageDigest.getInstance(cancellable);
//                md.update(signature.toByteArray());
//                publicate = removeLastChar(removeLastChar(removeLastChar(new String(Base64.encode(md.digest(), 0)))));
//                Log.e("key",publicate);
//
//            }
//        } catch (PackageManager.NameNotFoundException e1) {
//            Log.e("name not found" , e1.toString());
//        } catch (NoSuchAlgorithmException e) {
//            Log.e("no such an algorithm" , e.toString());
//        } catch (Exception e) {
//            Log.e("exception" , e.toString());
//        }
//    }

    private class uploadAsyncTask extends AsyncTask<String, Integer, Double> {
        @Override
        protected Double doInBackground(String... params) {postData(params[0],params[1], params[2],params[3],params[4]);
            return null;
        }
        protected void onPostExecute(Double result) {}
        protected void onProgressUpdate(Integer... progress) {}
        public void postData( String isUri, String post1, String post2, String post3, String post4) {
            HttpClient httpclient = new DefaultHttpClient();
            String getUri= String.format(domain+"%s.php", isUri);
            System.out.println(getUri+", "+post1+", "+post2+", "+post3);
            HttpPost httppost = new HttpPost(getUri);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("post1",post1 ));
                nameValuePairs.add(new BasicNameValuePair("post2",post2 ));
                nameValuePairs.add(new BasicNameValuePair("post3",post3 ));
                nameValuePairs.add(new BasicNameValuePair("post4",post4 ));
//                nameValuePairs.add(new BasicNameValuePair("event",event ));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                HttpResponse response = httpclient.execute(httppost);



            } catch (ClientProtocolException e) {

            } catch (IOException e) {
            }

        }
    }
    AlertDialog changesidedialog;
    private void showChangeSideDialog( Marker marker, String title, int id)
    {
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);
        String thisnickname=mSettings.getString(APP_NICKNAME,"");
        AlertDialog.Builder alert;

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        {
            alert=new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        }
        else
        {
            alert=new AlertDialog.Builder(this);
        }
        Button yellow,blue,union,neutral, dismiss;
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.changesidedialog,null);
        yellow=view.findViewById(R.id.yellow);
        blue=view.findViewById(R.id.blue);
        union=view.findViewById(R.id.neutral);
        neutral=view.findViewById(R.id.doneutral);
        dismiss = view.findViewById(R.id.dismiss);
        mess=view.findViewById(R.id.mess);
        alert.setView(view);
        alert.setCancelable(false);
        mess.setText("Укажите принадлежность точки "+"\""+title+"\"");
        changesidedialog = alert.create();
        String color=""+marker.getTag();
        Log.d("TAG", ""+color);

        if (color.equals("black")) {neutral.setAlpha(0.5f);neutral.setEnabled(false);}
        if (color.equals("yellow")) {yellow.setAlpha(0.5f);yellow.setEnabled(false);}
        if (color.equals("blue")) {blue.setAlpha(0.5f);blue.setEnabled(false);}
        if (color.equals("grey")) {union.setAlpha(0.5f);union.setEnabled(false);}




        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changesidedialog.dismiss();
            }
        });

        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (title.contains("Точка"))
                marker.setIcon(getBitmapHighDescriptor(R.drawable.yellowicon));
                if (title.contains("База"))
                    marker.setIcon(getBitmapHighDescriptor(R.drawable.yellowstar));




                //  String toSend=timeText+", "+snippet+" point captured by YELLOW side";
//                try {
//                    sendMessage(toSend);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                new uploadAsyncTask().execute("update_owner","1", title,""+id,"");
                changesidedialog.dismiss();
showDialog(title+" перешла под контроль Единства");
                String value="";
                updateGameLog("Организатор",title,""+id,"9", "100",thisnickname);
//                value = value + "('" + "Организатор" + "','" + title + "','" + id + "','"
//                        + dateText + "','" + timeText
//                        + "','" + "9" + "','" + "100" + "','" + thisnickname + "','"+ "10"+"'),";
//                new UpdateAsyncTask().execute(removeLastChar(value));

            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.contains("Точка"))
                    marker.setIcon(getBitmapHighDescriptor(R.drawable.blueicon));
                if (title.contains("База"))
                    marker.setIcon(getBitmapHighDescriptor(R.drawable.bluestar));
//                String toSend=timeText+", "+snippet+" point captured by BLUE side";
//                try {
//                    sendMessage(toSend);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                new uploadAsyncTask().execute("update_owner","2", title,""+id,"");

//                String add=timeText+"#"+title+"#Коалицией"+"#Коалиция";
//                new addInfoAsyncTask().execute(add);
                changesidedialog.dismiss();
                showDialog(title+" перешла под контроль Коалиции");
                String value="";
                updateGameLog("Организатор",title,""+id,"9", "200",thisnickname);


            }
        });
        union.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.contains("Точка"))
                    marker.setIcon(getBitmapHighDescriptor(R.drawable.greyicon));
                if (title.contains("База"))
                    marker.setIcon(getBitmapHighDescriptor(R.drawable.greystar));
//                String toSend=timeText+", "+snippet+" point is NEUTRAL";
//                try {
//                    sendMessage(toSend);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                new uploadAsyncTask().execute("update_owner","3", title,""+id,"");
                changesidedialog.dismiss();
                showDialog(title+" перешла под контроль Союза");

                String value="";
                updateGameLog("Организатор",title,""+id,"9", "300",thisnickname);


            }
        });
        changesidedialog.show();

        neutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("NeutralID", ""+id);
                if (id<=23) {
                    marker.setIcon(getBitmapHighDescriptor(R.drawable.blackicon));
                    Log.d("Neutral", "icon");
                }
                if (id>23) {
                    marker.setIcon(getBitmapHighDescriptor(R.drawable.blackstar));
                    Log.d("Neutral", "star");
                }



                //  String toSend=timeText+", "+snippet+" point captured by YELLOW side";
//                try {
//                    sendMessage(toSend);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                new uploadAsyncTask().execute("update_owner","0", title,""+id,"");
                changesidedialog.dismiss();
                showDialog(title+" нейтральная");
                String value="";
                updateGameLog("Организатор",title,""+id,"9", "400",thisnickname);


            }
        });
    }


    public void idCheck()
    {
        String idcheck=mSettings.getString(APP_PREFERENCES_ID,"");
        String gencheck=mSettings.getString(APP_PREFERENCES_GENERAL,"");
        String nick=mSettings.getString(APP_NICKNAME,"");
        int side= Integer.parseInt(mSettings.getString(APP_PREFERENCES_SIDE,"0"));
        System.out.println("General is "+side +", "+nick+", "+gencheck+", "+idcheck);
        if (idcheck!="") {
            startRL.setVisibility(GONE);
            genname.setText(nick.toUpperCase() + ", " + gencheck);
            mapview.setVisibility(View.VISIBLE);
            toppanel.setVisibility(View.VISIBLE);
            if (side<9) QR.setVisibility(View.VISIBLE);
            if (side==9) restart.setVisibility(View.VISIBLE);
            backs.setVisibility(GONE);
        }

    }

    public void updateGameLog (String general,String title, String id, String side,
                               String genID,String thisnickname)
    {

        String value = "('" + general + "','" + title + "','" + id + "',"
                + "CURDATE()" + "," + "CURTIME()"
                + ",'" + side + "','" + genID + "','" + thisnickname + "','"+ "10"+"'),";
new UpdateAsyncTask().execute(removeLastChar(value));
    }



}