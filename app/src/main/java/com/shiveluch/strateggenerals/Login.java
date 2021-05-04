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
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.telephony.mbms.StreamingServiceInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.view.View.GONE;


public class Login extends AppCompatActivity implements OnMapReadyCallback {
    String sbname = "http://gamestrateg.ru/generals/get_generals.php";
    String codenames="http://gamestrateg.ru/generals/get_codes.php";
    String getcodes="http://gamestrateg.ru/generals/get_gps_points.php";
    String addtolog="http://gamestrateg.ru/generals/add_tolog.php";
    String getcodesFormarkers="http://gamestrateg.ru/generals/get_gps_points.php";
    String clear_gen="http://gamestrateg.ru/generals/clear_generals.php";
    String add_taken="http://gamestrateg.ru/generals/add_takens.php";
    String getTaken="http://gamestrateg.ru/generals/get_taken.php";
    String getLastfromLog="";
    String getTime="";
    String updateOwner="http://gamestrateg.ru/generals/update_owner.php";
    String removeOld="http://gamestrateg.ru/generals/remove_old_takens.php";
    String getEvents="http://gamestrateg.ru/generals/get_events.php";
    String getLast="";
    String getSize="http://gamestrateg.ru/generals/get_points_size.php";
String message="";
String takePass="";
String updateStatus="";
String lastDate;
    String summary="";
    String get_pass="";
    String alpha="ABCDEFHIKLMNPQRSTUVWXYZ123456789";
    String command="";
    String currentpoint;
    TextView genname,mess, coordtext;
    RelativeLayout RL2;
    private GoogleMap mMap;
    Context context;
    SupportMapFragment mapFragment;
    SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "mysettings";//filename
    public static final String APP_PREFERENCES_INFO = "INFO";//player's INFO
    public static final String APP_PREFERENCES_PASSWORD = "Password";//player's system password
    public static final String APP_PREFERENCES_CODES = "Codes";//player's point codes
    public static final String APP_PREFERENCES_ID = "Taken";//player's taken points
    public static final String APP_PREFERENCES_TAKEN = "CodesOfTakenPoints";//player's taken points
    public static final String APP_PREFERENCES_GENERAL = "General";
    public static final String APP_PREFERENCES_SIDE="Side";
    public static final String APP_PREFERENCES_POINTS="Points";
    public static final String APP_PREFERENCES_TIME="Time";
    public static final String APP_POINTS_SIZE="PointSize";
    public static final String APP_IS_LOGIN="isLogin";
    public static final String APP_TO_BASA="toBasa";
    public static final String APP_STATUS="status";
    public static final String APP_NICKNAME="Nickname";

    Marker [] markers=new Marker[50];
    String genName;
    Button approve, closeLog, reset, cancelReset, showLog, showMap, showMap2;
    ListView genlist;
    Marker imhere;



    //TextView first_login, first_password;
    EditText first_login,first_password,event_password;
    RelativeLayout RL1,secRL,mapview,center, startRL;
    Button  new_login, QRScan,send;
    boolean check=false;
    public String have_pass;
    ImageView cenmap,backs,finger;
    ListView list;
    // ExpandableListView listView;
    ArrayList <String> get=new ArrayList<>();
    ArrayList <String> toBasa=new ArrayList<>();
    ArrayList <String> toCommonLog = new ArrayList<>();

    // Used in checking for runtime permissions.
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    // The BroadcastReceiver used to listen from broadcasts from the service.
    private MyReceiver myReceiver;

    // A reference to the service used to get location updates.
    private LocationUpdatesService mService = null;

    // Tracks the bound state of the service.
    private boolean mBound = false;

    // UI elements.

    // Monitors the state of the connection to the service.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
            mService = binder.getService();
            mService.requestLocationUpdates(); // also request it here
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection);
            mBound = false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!checkPermissions()) {
            requestPermissions();
        } else if (mService != null) { // add null checker
            mService.removeLocationUpdates();
            mService.requestLocationUpdates();
        }
        bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getJSON(getSize);
      //  getMarkers();
        //markers
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myReceiver = new MyReceiver();
        setContentView(R.layout.activity_login);
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
        first_login = findViewById(R.id.first_login);
        genname=findViewById(R.id.genname);
        RL2=findViewById(R.id.RL2);
        coordtext=findViewById(R.id.coordtext);
        genlist=findViewById(R.id.genlist);
        showLog=findViewById(R.id.showlog);
        closeLog=findViewById(R.id.closeLog);
        cenmap=findViewById(R.id.cenmap);
        backs=findViewById(R.id.backs);

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
        list=findViewById(R.id.list);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        have_pass = mSettings.getString(APP_PREFERENCES_PASSWORD, "");
        String info = mSettings.getString(APP_PREFERENCES_INFO, "");

        Log.d("Start",info);
        String points=mSettings.getString(APP_PREFERENCES_CODES,"");
        String idcheck=mSettings.getString(APP_PREFERENCES_ID,"");
        String isLogin=mSettings.getString(APP_IS_LOGIN,"0");
        String gencheck=mSettings.getString(APP_PREFERENCES_GENERAL,"");
        String nick=mSettings.getString(APP_NICKNAME,"");

        showMap2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
       if (imhere!=null) imhere.remove();
        LatLng position=new LatLng(commonLat,commonLon);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position)
                .zoom(15)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.animateCamera(cameraUpdate);
        imhere=(mMap.addMarker(new MarkerOptions()
                .position(new LatLng(commonLat, commonLon))
                .title("Я здесь")
                .icon(getBitmapHighDescriptor(R.drawable.imhere))));
    }
});
        cenmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   LatLng position=new LatLng(55.54, 36.937);//Алабино
                LatLng position=new LatLng(55.086091,38.180888);//дача
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(position)
                        .zoom(17f)
                        .build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                mMap.animateCamera(cameraUpdate);
            }
        });

        showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                mapview.setVisibility(View.VISIBLE);
            }
        });
        backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("check", "Check is" + idcheck);
                mapview.setVisibility(View.INVISIBLE);
            }
        });

        getStatus();
        if (idcheck!="")
        {startRL.setVisibility(GONE);
            genName="Генерал "+mSettings.getString(APP_PREFERENCES_ID,"")+": ";
          //  new_login.setVisibility(View.GONE);
           // showMap.setVisibility(GONE);
            genname.setText(nick.toUpperCase()+"\n"+gencheck);
            secRL.setVisibility(View.VISIBLE);}
        String taken=mSettings.getString(APP_PREFERENCES_TAKEN,"");

        String []splitinfo=taken.split("#");
        if (splitinfo.length>=1) {
            {
                for (int i = 0; i < splitinfo.length; i++) {
                    Log.d("split", splitinfo[i]);
                    String[] finalString = splitinfo[i].split(",");
                    if (finalString.length>1){
                        String toArray =
                                finalString[1] + " взята " + finalString[3] + " в " + finalString[4];
                        get.add(toArray);}
                    ArrayAdapter adapter = new ArrayAdapter(this,
                            R.layout.list_item, get);
//
                    list.setAdapter(adapter);

//
//                        }
                }

            }

            ArrayAdapter adapter = new ArrayAdapter(this,
                    R.layout.list_item, get);

            list.setAdapter(adapter);
        }
        String [] pointsarray=points.split("#");

        for (int i=0;i<pointsarray.length;i++)
        {
            Log.d("Points",pointsarray[i]);
            String [] pointarray=pointsarray[i].split(",");

        }



        closeLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secRL.setVisibility(View.VISIBLE);
                RL2.setVisibility(GONE);
            }
        });

        showLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secRL.setVisibility(GONE);
                RL2.setVisibility(View.VISIBLE);
                getJSON(getEvents);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new ClearAsyncTask().execute(mSettings.getString(APP_PREFERENCES_ID,""));
               showResetDialog();

            }
        });

        new_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermissions()) {
                    requestPermissions();
                }
                String curLog=first_login.getText().toString();
                String curPass=first_password.getText().toString();
                if (curPass.length()>0)
                {sbname="http://gamestrateg.ru/generals/get_generals.php/get.php?nom="+curPass;
                SharedPreferences.Editor editor=mSettings.edit();
                    editor.putString(APP_NICKNAME,curLog);
                    editor.putString(APP_IS_LOGIN,"0");
                    editor.apply();
                    getJSON(sbname);

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Не введен пароль",Toast.LENGTH_SHORT);
                }

            }
        });

        QRScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getJSON(getTaken);

                Log.d("GetCodesbyButton", mSettings.getString(APP_PREFERENCES_CODES,""));
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt(" ");
                integrator.setCameraId(0);
                integrator.autoWide();
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
        String taken=mSettings.getString(APP_PREFERENCES_TAKEN,"");
        String toBasa=mSettings.getString(APP_TO_BASA,"");

        String basa=mSettings.getString(APP_PREFERENCES_POINTS,"");
        String [] allcodes=basa.split("#");
        boolean isCode=false;

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {

            } else {
                String new_codename = result.getContents();//получили код
                for (int i=0;i<allcodes.length;i++)
                {
                    String [] currentcode=allcodes[i].split(",");
                    if (currentcode[1].equals(new_codename))
                    {
                        double takenLat=Double.parseDouble(currentcode[2]);
                        double takenLon=Double.parseDouble(currentcode[3]);
                        double distance=Utils.distanceInKmBetweenEarthCoordinates(commonLat,commonLon,takenLat,takenLon);
                        String convert=(""+distance*1000);

                        String [] split=convert.split("\\.");
                        int finalDist=Integer.parseInt(split[0]);
                        Log.d("Distance","Dist: "+finalDist);
                        if (finalDist>50000000) Toast.makeText(getApplicationContext(),""+currentcode[0]+" не может быть взята, дистанция: "+finalDist + "метров",Toast.LENGTH_LONG).show();
                        if (finalDist<=50000000)
                        {
                            // Текущее время
                            Date currentDate = new Date();

                            Log.d("CurrentDate","Currentdate "+currentDate);
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
                            String dateText = dateFormat.format(currentDate);
                            DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.UK);
                            String timeText = timeFormat.format(currentDate);
                            String datetime=dateText+","+timeText;
                            String cGen=mSettings.getString(APP_PREFERENCES_GENERAL,"");
                            String cPoint=currentcode[0];
                            String idPoint=currentcode[5];
                            String cSide = mSettings.getString(APP_PREFERENCES_SIDE,"");
                            String cId=mSettings.getString(APP_PREFERENCES_ID,"");
                            String cTime=mSettings.getString(APP_PREFERENCES_TIME,"");
                            Log.d("newtime",cTime);

                            String [] timesplit=cTime.split(",");
                            Log.d("newtimeSplit",timesplit[0]+", "+timesplit[1]);
                            double isBegin = 0;
                            double isEnd=0 ;
                            Date date1 = null, date2=null;
                            SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.UK);

                            try {

                                date1 = formater.parse(timesplit[0]);
                                date2= formater.parse(timesplit[1]);

                                isBegin=date1.getTime();
                                isEnd =date2.getTime();

                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                             double isDate=currentDate.getTime();
                            Log.d("Getdate", ""+date1+", "+currentDate+", "+date2);
                             if (isBegin<isDate && isDate<isEnd)
                             {
                                 Toast.makeText(getApplicationContext(),"в пределах даты",Toast.LENGTH_LONG).show();
                                 taken=taken+cGen+","+cPoint+","+idPoint+","+datetime+","+cSide+","+cId+"#";
                                 String inf=cPoint+ " взята " + dateText+ " в " + timeText;
                                 get.add(inf);
                                                 ArrayAdapter adapter = new ArrayAdapter(this,
                        R.layout.list_item, get);
//
                        list.setAdapter(adapter);
                                SharedPreferences.Editor editor =mSettings.edit();
                                editor.putString(APP_PREFERENCES_INFO,(inf+"#"));
                                editor.putString(APP_PREFERENCES_TAKEN,(taken));
                                editor.apply();
                                uploadInfo();


                             }
                             else
                             {
                                 Toast.makeText(getApplicationContext(),"Время взятия истекло либо не наступило",Toast.LENGTH_LONG).show();

                             }


                     //      get.add(cPoint+" взята "+)

                        }
                        isCode=true;
                    }

                }
                if (!isCode)
                {
                    Toast.makeText(getApplicationContext(),"Информация о точке не найдена",Toast.LENGTH_LONG).show();

                }

//                Log.d("QR", "" + new_codename);
//                String control=""+new_codename.charAt(0);
//                String side=mSettings.getString(APP_PREFERENCES_SIDE,"");
//                if (side.equals("A") && control.equals("B"))
//                {
//                    message="Захват невозможен!Эта точка принадлежит стороне противника!";
//                    showDialog();
//                    return;
//                }
//
//                if (side.equals("B") && control.equals("A"))
//                {
//                    message="Захват невозможен! Эта точка принадлежит стороне противника!";
//                    showDialog();
//                    return;
//                }
//                getTime="http://gamestrateg.ru/generals/get_point_time_info.php/get.php?code="+new_codename;
//                getJSON(getTime);

//                String  getInfo=mSettings.getString(APP_PREFERENCES_INFO,"");
//                if (getInfo.contains(new_codename)||temp.contains((new_codename))) {
//                    Log.d("Att","Code is exist");
////                    Toast toast =Toast.makeText(getApplicationContext(),"Точка была захвачена ранее",Toast.LENGTH_LONG);
////                    toast.show();
////                    message=
//
//                    return;
//                }
//                String points=mSettings.getString(APP_PREFERENCES_CODES,"");
//                Log.d("Points",points);
//                if (!points.contains(new_codename))
//                {
//                    //Toast.makeText(this,"Точка была захвачена ранее",Toast.LENGTH_SHORT);
//                    return;
//                }
//                String [] pointsarray=points.split("#");
//                for (int i=0;i<pointsarray.length;i++)
//                {
//                    String [] pointarray=pointsarray[i].split(",");
//                    //Log.d("Points","From array: "+ pointsarray[i]);
//                    if (pointarray[0].equals(new_codename)) currentpoint=pointarray[1];
//                }
//                Log.d("Points","Now: "+currentpoint);
//                Date currentDate = new Date();
//                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
//                String dateText = dateFormat.format(currentDate);
//                DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
//                String timeText = timeFormat.format(currentDate);
//
//                Log.d("FinalString",first_password.getText().toString()+"#"+new_codename+"#"
//                        +currentpoint+"#"+dateText+"#"+timeText);
//                String summary=first_password.getText().toString()+"#"+new_codename+"#"
//                        +currentpoint+"#"+dateText+"#"+timeText+",";
//                String toList = currentpoint +"взята "+dateText + " в " + timeText;
//                String toArray=mSettings.getString(APP_PREFERENCES_GENERAL,"")+": " +currentpoint +" взята "+dateText + " в " + timeText;
//                new AddAsyncTask().execute(mSettings.getString(APP_PREFERENCES_ID,""),toArray,new_codename);
//                Log.d ("LOG",mSettings.getString(APP_PREFERENCES_ID,"0")+", "+toArray+", "+new_codename);
//                get.add(toList);
//                ArrayAdapter adapter = new ArrayAdapter(this,
//                        R.layout.list_item, get);
//
//                list.setAdapter(adapter);
//                SharedPreferences.Editor editor=mSettings.edit();
//                editor.putString(APP_PREFERENCES_INFO,getInfo+summary);
//                editor.apply();
//                message="Точка захвачена";
//                    showDialog();






//                String summary=getInfo+first_password.getText().toString()+":"+new_codename+",";
//                SharedPreferences.Editor editor=mSettings.edit();
//                editor.putString(APP_PREFERENCES_INFO,summary);
//                editor.apply();
//                String [] pointsarray=points.split(",");
//                for (int i=0;i<pointsarray.length;i++)
//                {
//                    Log.d("Points",pointsarray[i]);
//                    String [] pointarray=pointsarray[i].split(":");
//                    if (pointarray[0].equals(new_codename)) currentpoint=pointarray[1];
//                }
//                Log.d("Points",currentpoint);
//
//                String massiv=mSettings.getString(APP_PREFERENCES_INFO,"");
//                Log.d("shared",massiv);
//                String[] separated = massiv.split(",");
//                for (int i=0;i<separated.length;i++)
//                {
//                    Log.d("massiv",separated[i]);                }

            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    final Activity activity = this;

    private void getJSON(final String urlWebService) {
        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {

                Log.d("GetJSON", "PreExecute");
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //    Log.d("GetJSON all",s);

                if (s != null) {
                    try {

                        if (urlWebService==getcodes)
                            loadGpsCodes(s);

                        if (urlWebService==getLast)
                            loadLast(s);

                        if (urlWebService==getSize)
                            loadsize(s);

                        if (urlWebService==takePass)
                            loadstatus(s);

//                        if (urlWebService==getcodesFormarkers)
//                            loadGpsCodesForMarkers(s);

                        if (urlWebService==updateStatus)
                            updateGenStatus(s);

                        if (urlWebService==sbname)

                        {loadpassinfo(s);
                            Log.d("GetJSON", "loadplayers");
                            if (check==true)
                            {
                                Log.d("Check",""+check);
                                startRL.setVisibility(GONE);
                                secRL.setVisibility(View.VISIBLE);
                               // new_login.setVisibility(View.GONE);

                            }
                            if (check==false) {
                                Log.d("Check",""+check);
                            }


                        }

                        if (urlWebService==codenames)
                        {
                            loadcodesinso(s);
                        }
                        if (urlWebService==getEvents)
                        {
                            loadevents(s);
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





    private void loadpassinfo(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);

        if (jsonArray.length() == 0) {
            Log.d("GetPass", "Пароль не найден");
Toast.makeText(getApplicationContext(),"Пароль не опознан",Toast.LENGTH_LONG).show();
return;
        }
        Log.d("GetJSON", "Taking data...");

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
                //   Log.d("GetJson", gname_s[i] + ":" + gpass_s[i]+"Password: "+first_password.getText().toString());
                int curStatus=Integer.parseInt(status_s[i]);
                if (curStatus==1)
                {
                    Toast.makeText(getApplicationContext(),"Генерал уже в игре",Toast.LENGTH_LONG).show();
                    return;
                }
                if (gpass_s[i].equalsIgnoreCase(first_password.getText().toString())) {
                  check=true;
                    String nick=mSettings.getString(APP_NICKNAME,"");
                    Log.d("Check", "" + check);
                    genname.setText(nick.toUpperCase()+"\n"+gname_s[i]);
                    SharedPreferences.Editor editor=mSettings.edit();
                    editor.putString(APP_PREFERENCES_ID,id_s[i]);
                    editor.putString(APP_PREFERENCES_GENERAL,gname_s[i]);
                    editor.putString(APP_PREFERENCES_PASSWORD,gpass_s[i]);
                    editor.putString(APP_IS_LOGIN,"1");
                    if (gname_s[i].contains("Жел"))  editor.putString(APP_PREFERENCES_SIDE,"1");
                    if (gname_s[i].contains("Син"))  editor.putString(APP_PREFERENCES_SIDE,"2");
                    if (gname_s[i].contains("Сер"))  editor.putString(APP_PREFERENCES_SIDE,"3");
                    editor.apply();
                    have_pass=mSettings.getString(APP_PREFERENCES_PASSWORD,"");
                  //  getJSON(codenames);
                    getJSON(getcodes);
                    updateStatus="http://gamestrateg.ru/generals/update_points.php/get.php?nom="+gpass_s[i];
                    getJSON(updateStatus);
                    new updateNickAsyncTask().execute(gpass_s[i],nick);

                    String info=nick.toUpperCase()+" вошел в игру как "+gname_s[i];
                    toLog(info);//запись в лог при входе
                    Log.d("ID",mSettings.getString(APP_PREFERENCES_ID,"")+", "
                            +mSettings.getString(APP_PREFERENCES_SIDE,""));

                }

            }

        }
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
        if (jsonArray.length() == 0) Log.d("Update status", "Updated");

    }

    private void loadsize(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        if (jsonArray.length() == 0) Log.d("GetLast", "Коды не найдены");
        Log.d("GetJSON", "Taking data...");
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
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.UK);
        double isBegin, isEnd;
        lastDate="00-00-00 00:00:00";
        if (jsonArray.length() == 0) {
            Log.d("GetLast", "Коды не найдены");
            String[] cleartake = mSettings.getString(APP_PREFERENCES_TAKEN, "").split("#");
            Log.d("Last","Last is cleartake "+cleartake.length+", "+cleartake);

        }
        Log.d("GetJSON", "Taking data...");
        String[] date_s = new String[jsonArray.length()];
        String[] time_s = new String[jsonArray.length()];

        String codelist="";
//        int sidecheck=Integer.parseInt(mSettings.getString(APP_PREFERENCES_ID,""));


        if (jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
//
                JSONObject obj = jsonArray.getJSONObject(i);
                date_s[i] = obj.getString("date");
                time_s[i] = obj.getString("time");

            }
            lastDate=date_s[0]+" "+time_s[0];
        }

            String[] upload = mSettings.getString(APP_PREFERENCES_TAKEN, "").split("#");
            Log.d("Last","Last is "+upload.length);
            String[] splitting=upload[upload.length-1].split(",");
            String newDate=splitting[3]+" "+splitting[4];

            String point_id=splitting[2];
            String side=mSettings.getString(APP_PREFERENCES_SIDE,"0");

            try {

                date1 = formater.parse(lastDate);
                date2= formater.parse(newDate);

                isBegin=date1.getTime();
                isEnd =date2.getTime();
                Toast.makeText (getApplicationContext(), ""+date1+", "+date2,Toast.LENGTH_LONG).show();
                if (isEnd>isBegin)
                   // Toast.makeText (getApplicationContext(), ""+"Должно быть нормально "+point_id+","+side,Toast.LENGTH_LONG).show();

                    new UpdateOwnerAsyncTask().execute(side, point_id);

            }
            catch (Exception e) {
                e.printStackTrace();
            }




    }

    private void loadGpsCodes(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        if (jsonArray.length() == 0) Log.d("GetJson", "Коды не найдены");
        Log.d("GetJSON", "Taking data...");
        String[] id_s = new String[jsonArray.length()];
        String[] code_s = new String[jsonArray.length()];
        String[] name_s = new String[jsonArray.length()];
        String[] lat_s = new String[jsonArray.length()];
        String[] lon_s = new String[jsonArray.length()];
        String[] status_s = new String[jsonArray.length()];
        String[] time_s = new String[jsonArray.length()];
        String[] control_s = new String[jsonArray.length()];
        String codelist="";
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
//                Log.d("sidecheck",""+chars+", "+ sidecheck);
//                if ((chars.equals("A") && sidecheck>=1 && sidecheck<=10) || (chars.equals("B") && sidecheck>10 && sidecheck<=11) || chars.equals("C"))
//                {
                    codelist=codelist+name_s[i]+","+code_s[i]+","+lat_s[i]+","+lon_s[i]+","+status_s[i]+","+id_s[i]+"#";
                    String times=time_s[i]+","+control_s[i];
                    SharedPreferences.Editor editor=mSettings.edit();
                    editor.putString(APP_PREFERENCES_POINTS,codelist);
                    editor.putString(APP_PREFERENCES_TIME,times);
                    editor.apply();}
            Log.d("newtime",mSettings.getString(APP_PREFERENCES_TIME,""));

//                if (gpass_s[i].equals(first_password.getText().toString())) {
//                    check = true;
//                    Log.d("Check", "" + check);
//                }

            //}
            //Log.d("GetCodes", mSettings.getString(APP_PREFERENCES_POINTS,""));
            String basa=mSettings.getString(APP_PREFERENCES_POINTS,"");
            String [] allcodes=basa.split("#");
            for (int i=0;i<allcodes.length;i++)
            {
                Log.d("GPScodes",allcodes[i]);
            }
            getMarkers();

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
//        if (jsonArray.length() == 0) Log.d("GetCodes", "Коды не найдены");
//        Log.d("GetJSON", "Taking data...");
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
//Log.d("Clear","map.clear");
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
////                Log.d("sidecheck",""+chars+", "+ sidecheck);
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
////                    Log.d("Check", "" + check);
////                }
//
//            //}
//            Log.d("GetCodes", mSettings.getString(APP_PREFERENCES_POINTS,""));
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


    private void loadcodesinso(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        if (jsonArray.length() == 0) Log.d("GetJson", "Коды не найдены");
        Log.d("GetJSON", "Taking data...");

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
                Log.d("sidecheck",""+chars+", "+ sidecheck);
                if ((chars.equals("A") && sidecheck>=1 && sidecheck<=10) || (chars.equals("B") && sidecheck>10 && sidecheck<=11) || chars.equals("C"))
                {
                    codelist=codelist+code_s[i]+","+name_s[i]+"#";
                    SharedPreferences.Editor editor=mSettings.edit();
                    editor.putString(APP_PREFERENCES_CODES,codelist);
                    editor.apply();}

//                if (gpass_s[i].equals(first_password.getText().toString())) {
//                    check = true;
//                    Log.d("Check", "" + check);
//                }

            }
     //       Log.d("GetCodes", mSettings.getString(APP_PREFERENCES_CODES,""));

        }
    }


    private void loadTime(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        if (jsonArray.length() == 0) {
            Log.d("GetJson", "Коды не найдены");
            message="Считан ошибочный код";
                showDialog();
            Toast.makeText(getApplicationContext(),"Считан ошибочный код",Toast.LENGTH_SHORT).show();
        }
        Log.d("GetJSON", "Taking data...");

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
                String toList = name_s[0] +" взята "+dateText + " в " + timeText;
                String toArray=mSettings.getString(APP_PREFERENCES_GENERAL,"")+": " +name_s[0]+" взята "+dateText + " в " + timeText;
                new AddAsyncTask().execute(toArray,code_s[0]);
           //     Toast.makeText(getApplicationContext(),toArray+", "+code_s[0],Toast.LENGTH_LONG).show();
              //  Log.d ("LOG",mSettings.getString(APP_PREFERENCES_ID,"0")+", "+toArray+", "+new_codename);
                get.add(toList);
                ArrayAdapter adapter = new ArrayAdapter(this,
                        R.layout.list_item, get);

                list.setAdapter(adapter);
                SharedPreferences.Editor editor=mSettings.edit();
                editor.putString(APP_PREFERENCES_INFO,getInfo+summary);
                editor.apply();
                message="Точка захвачена";
                    showDialog();
            }
        }
    }

    private void loadstatus(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        if (jsonArray.length() == 0) {Log.d("GetStatus", "Status not found");
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
                    Log.d("Status", "Не зашел в игру");
                    check=false;
                    mSettings.edit().clear().apply();
                    Log.d("Pref", mSettings.getString(APP_PREFERENCES_GENERAL,"0"));
                    first_password.setText("");
                    secRL.setVisibility(GONE);
                    //mapview.setVisibility(GONE);
                    RL2.setVisibility(GONE);
                    startRL.setVisibility(View.VISIBLE);
                    SharedPreferences.Editor editir = mSettings.edit();
                    editir.putString(APP_PREFERENCES_ID,"");
                    editir.apply();
                }
                if (status==1)
                {
                    Log.d("Status", "В игре");
                    startRL.setVisibility(GONE);
                    secRL.setVisibility(View.VISIBLE);

                }
            }

            Log.d("points","Get takens "+mSettings.getString(APP_PREFERENCES_TAKEN,""));
        }


    }

    private void loadTakenPoints(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        if (jsonArray.length() == 0) Log.d("GetJsonpoint", "Коды не найдены");
        Log.d("GetJSONpoint", "Taking data...");
        int take;

        String[] code_s = new String[jsonArray.length()];
        String[] name_s = new String[jsonArray.length()];
        String[] taken_s = new String[jsonArray.length()];


        if (jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
//
                JSONObject obj = jsonArray.getJSONObject(i);
                code_s[i] = obj.getString("code");
                name_s[i] = obj.getString("name");
                taken_s[i] = obj.getString("taken");
                take=Integer.parseInt(taken_s[i]);
                // Log.d("points",code_s[i]+", "+name_s[i]+", "+take);
                if (take==1)
                { Log.d("points","Finded: "+code_s[i]);

                    String temp=mSettings.getString(APP_PREFERENCES_TAKEN,"");
                    if (!temp.contains(code_s[i])) {
                        SharedPreferences.Editor editor = mSettings.edit();
                        editor.putString(APP_PREFERENCES_TAKEN, temp + code_s[i]);
                        editor.apply();
                    }
                }


            }

            Log.d("points","Get takens "+mSettings.getString(APP_PREFERENCES_TAKEN,""));
        }


    }





//    private class CreateUserAsyncTask extends AsyncTask<String, Integer, Double> {
//        @Override
//        protected Double doInBackground(String... params) {
//            postData(params[0], params[1]);
//            return null;
//        }
//
//        protected void onPostExecute(Double result) {
//
//        }
//
//        protected void onProgressUpdate(Integer... progress) {
//        }
//
//        public void postData(String nickname, String password) {
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpPost httppost = new HttpPost("http://kamonline.r41.ru/Strateg/add_player.php");
//            try {
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//                nameValuePairs.add(new BasicNameValuePair("nickname", nickname));
//                nameValuePairs.add(new BasicNameValuePair("passcode", password));
//
//                Log.d("UpdateBase", "nick: " + nickname + " pass: " + password);
//                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
//                HttpResponse response = httpclient.execute(httppost);
//            } catch (ClientProtocolException e) {
//
//            } catch (IOException e) {
//            }
//
//        }
//    }

//    private class ClearAsyncTask extends AsyncTask<String, Integer, Double> {
//        @Override
//        protected Double doInBackground(String... params) {
//            postData(params[0]);
//            return null;
//        }
//
//        protected void onPostExecute(Double result) {
//
//        }
//
//        protected void onProgressUpdate(Integer... progress) {
//        }
//
//        public void postData(String code) {
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpPost httppost = new HttpPost(clear_gen);
//            try {
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//                nameValuePairs.add(new BasicNameValuePair("gen_id", code));
//                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
//                HttpResponse response = httpclient.execute(httppost);
//                for (int i=0;i<get.size();i++)
//                {
//                    new AddAsyncTask().execute(mSettings.getString(APP_PREFERENCES_ID,""),""+get.get(i));
//
//                }
//            } catch (ClientProtocolException e) {
//
//            } catch (IOException e) {
//            }
//
//        }
//    }
//
//
    private class UpdateAsyncTask extends AsyncTask<String, Integer, Double> {
        @Override
        protected Double doInBackground(String... params) {
            postData(params[0],params[1],params[2],params[3],params[4],params[5], params[6]);
            return null;
        }

        protected void onPostExecute(Double result) {

        }

        protected void onProgressUpdate(Integer... progress) {
        }

        public void postData(String general, String point, String idPoint, String date, String time, String side, String id) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(addtolog);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("general",general ));
                nameValuePairs.add(new BasicNameValuePair("point",point));
                nameValuePairs.add(new BasicNameValuePair("point_id",idPoint));
                nameValuePairs.add(new BasicNameValuePair("date",date));
                nameValuePairs.add(new BasicNameValuePair("time",time ));
                nameValuePairs.add(new BasicNameValuePair("side",side ));
                nameValuePairs.add(new BasicNameValuePair("id",id ));

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
            postData(params[0]);
            return null;
        }

        protected void onPostExecute(Double result) {

        }

        protected void onProgressUpdate(Integer... progress) {
        }

        public void postData(String id) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(removeOld);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("id",id ));

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
        if (jsonArray.length() == 0) Log.d("GetJson", "Пароль не найден");
        Log.d("GetJSON", "Taking data...");


        String[] general_s = new String[jsonArray.length()];
        String[] point_s = new String[jsonArray.length()];
        String[] date_s = new String[jsonArray.length()];
        String[] time_s = new String[jsonArray.length()];

        if (jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
//
                JSONObject obj = jsonArray.getJSONObject(i);

                general_s[i] = obj.getString("general");
                point_s[i] = obj.getString("point");
                date_s[i] = obj.getString("date");
                time_s[i] = obj.getString("time");

                String toAdd=general_s[i]+": "+point_s[i]+" взята "+date_s[i]+ " в " +time_s[i];

                get.add(toAdd);
            }

            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item, get);
            genlist.setAdapter(adapter);

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
       // updateLocationUI();
       // LatLng anomaly1 = new LatLng(53, 158);//Алабино
//        LatLng position=new LatLng(55.086091,38.180888);//Дача
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.getUiSettings().setRotateGesturesEnabled(false);

        initOverlay();
        //LatLng position=new LatLng(55.54, 36.937);//Алабино
        LatLng position=new LatLng(55.086091,38.180888);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position)
                .zoom(17f)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.animateCamera(cameraUpdate);
       // getJSON(getcodesFormarkers);



        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                coordtext.setText(""+latLng.latitude+", "+latLng.longitude);
               // Toast.makeText(context,""+latLng.latitude+", "+latLng.longitude,Toast.LENGTH_LONG).show();
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

    private void initOverlay() {
        double north = 55.5506105133793;
        double south = 55.52989324454769;
        double east = 36.94961723644818;
        double west = 36.92383041021363;


        LatLng swMapCoord=new LatLng(south,west);
        LatLng neMapCoord=new LatLng(north,east);
        LatLngBounds bounds=new LatLngBounds(swMapCoord, neMapCoord);


        GroundOverlayOptions poligon = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.snbmap))
                .positionFromBounds(bounds);
        if (poligon!=null)
            mMap.addGroundOverlay(poligon);
    }
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

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
        try {
            // clearing app data
            String packageName = getApplicationContext().getPackageName();
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("pm clear "+packageName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDialog()
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

    private void showResetDialog()
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

            removeMarkers();
            if (isOnline(getApplicationContext())) {}//Toast.makeText (getApplicationContext(), "isOnline",Toast.LENGTH_SHORT).show();
            if (!isOnline(getApplicationContext())) {}//Toast.makeText (getApplicationContext(), "isOffline",Toast.LENGTH_SHORT).show();
           //
            int size=Integer.parseInt(mSettings.getString(APP_POINTS_SIZE,"0"));
            Location location = intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION);
            if (location != null) {
double lat=Double.parseDouble(Utils.getLat(location));
                getStatus();
                double lon=Double.parseDouble(Utils.getLon(location));
                commonLat=lat;
                commonLon=lon;
                double distance=Utils.distanceInKmBetweenEarthCoordinates(commonLat,commonLon,37.421,-122.08);
                String convert=(""+distance*1000);
                //int finalDist=Integer.parseInt(convert[0]);
                String [] split=convert.split("\\.");
                Log.d("Coord", ""+lat+","+lon+","+split[0]);

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
                mService.requestLocationUpdates();
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
        takePass="http://gamestrateg.ru/generals/get_gen_status.php/get.php?nom="+have_pass;
        getJSON(takePass);

    }

    private void uploadInfo() {

        String genId = mSettings.getString(APP_PREFERENCES_ID, "0");
        String[] upload = mSettings.getString(APP_PREFERENCES_TAKEN, "").split("#");
String[] splitting=upload[upload.length-1].split(",");
String point_id=splitting[2];
        getLast="http://gamestrateg.ru/generals/get_last_by_point.php/get.php?nom="+point_id;
        getJSON(getLast);
        new RemoveAsyncTask().execute(genId);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//Toast.makeText(getApplicationContext(),"Запись"+upload.length, Toast.LENGTH_LONG).show();
        for (int t = 0; t < upload.length; t++) {
            // Toast.makeText(getApplicationContext(),"Запись "+upload[t], Toast.LENGTH_LONG).show();

            String[] splits = upload[t].split(",");
            new UpdateAsyncTask().execute(splits[0], splits[1], splits[2], splits[3], splits[4], splits[5], splits[6]);
        }
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

    private void removeMarkers()
    {
        getJSON(getcodes);
        String base = mSettings.getString(APP_PREFERENCES_POINTS, "0,0,0,0,0,0");
        String[] splitting = base.split("#");
        for (int j = 0; j < splitting.length; j++)
            Log.d("splitting", splitting[j]);
        if (splitting != null) {
            for (int j = 0; j < splitting.length; j++) {
                Log.d("ToRemove","Is "+splitting[j]);
                String[] currentmarker = splitting[j].split(",");
                int sideMarker = Integer.parseInt(currentmarker[4]);

                if (markers[j]!=null) {
                    if (sideMarker == 0)
                        markers[j].setIcon(getBitmapHighDescriptor(R.drawable.blackicon));
                    if (sideMarker == 1)
                        markers[j].setIcon(getBitmapHighDescriptor(R.drawable.yellowicon));
                    if (sideMarker == 2)
                        markers[j].setIcon(getBitmapHighDescriptor(R.drawable.blueicon));
                    if (sideMarker == 3)
                        markers[j].setIcon(getBitmapHighDescriptor(R.drawable.greyicon));
                }
//                if (markers[j]!=null)
//                {
//                    markers[j].setIcon();
//                }
            }
            }


    }


    private void getMarkers()
    {
        String base = mSettings.getString(APP_PREFERENCES_POINTS, "0,0,0,0,0");
                    String[] splitting = base.split("#");
                    for (int j = 0; j < splitting.length; j++)
                        Log.d("splitting", splitting[j]);
      //  Toast.makeText(getApplicationContext(),"Split: "+base+", "+splitting,Toast.LENGTH_SHORT).show();

        if (splitting != null) {
                        for (int j = 0; j < splitting.length; j++) {
                            Log.d("Array", "J " + j);
                            String[] currentmarker = splitting[j].split(",");
                            int sideMarker = Integer.parseInt(currentmarker[4]);
                   //         Toast.makeText(getApplicationContext(),""+sideMarker,Toast.LENGTH_SHORT).show();
                            if (sideMarker == 0) {
                                markers[j] = (mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(Double.parseDouble(currentmarker[2]), Double.parseDouble(currentmarker[3])))
                                        .title("Точка " + (j + 1))
                                        .icon(getBitmapHighDescriptor(R.drawable.blackicon))));
                   //             Toast.makeText(getApplicationContext(),""+sideMarker+", "+"black",Toast.LENGTH_SHORT).show();

                            }
                            if (sideMarker == 1) {
                                markers[j] = (mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(Double.parseDouble(currentmarker[2]), Double.parseDouble(currentmarker[3])))
                                        .title("Точка " + (j + 1))
                                        .icon(getBitmapHighDescriptor(R.drawable.yellowicon))));
                            }
                            if (sideMarker == 2) {
                                markers[j] = (mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(Double.parseDouble(currentmarker[2]), Double.parseDouble(currentmarker[3])))
                                        .title("Точка " + (j + 1))
                                        .icon(getBitmapHighDescriptor(R.drawable.blueicon))));
                            }
                            if (sideMarker == 3) {
                                markers[j] = (mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(Double.parseDouble(currentmarker[2]), Double.parseDouble(currentmarker[3])))
                                        .title("Точка " + (j + 1))
                                        .icon(getBitmapHighDescriptor(R.drawable.greyicon))));
                            }
                        }
                    }
                }



}