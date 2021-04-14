package com.shiveluch.strateggenerals;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MotionEventCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Random;



public class Login extends AppCompatActivity {
    String sbname = "http://gamestrateg.ru/generals/get_generals.php";
    String codenames="http://gamestrateg.ru/generals/get_codes.php";
    String clear_gen="http://gamestrateg.ru/generals/clear_generals.php";
    String add_taken="http://gamestrateg.ru/generals/add_takens.php";
    String getTaken="http://gamestrateg.ru/generals/get_taken.php";

    String summary="";
    String get_pass="";
    String alpha="ABCDEFHIKLMNPQRSTUVWXYZ123456789";
    String command="";
    String currentpoint;
    TextView genname;


    SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "mysettings";//filename
    public static final String APP_PREFERENCES_INFO = "INFO";//player's INFO
    public static final String APP_PREFERENCES_PASSWORD = "Password";//player's system password
    public static final String APP_PREFERENCES_CODES = "Codes";//player's point codes
    public static final String APP_PREFERENCES_ID = "Taken";//player's taken points
    public static final String APP_PREFERENCES_TAKEN = "CodesOfTakenPoints";//player's taken points
    String genName;



    //TextView first_login, first_password;
    EditText first_login,first_password,event_password;
    RelativeLayout RL1,secRL;
    Button  new_login, QRScan,send;
    boolean check=false;
    public String have_pass;
    ListView list;
    // ExpandableListView listView;
    ArrayList <String> get=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 123);
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        first_login = findViewById(R.id.first_login);
        genname=findViewById(R.id.genname);
        first_password = findViewById(R.id.first_password);
        new_login = findViewById(R.id.new_login);
        RL1=findViewById(R.id.center_layout);
        secRL=findViewById(R.id.secRL);
        secRL.setVisibility(View.GONE);
        QRScan=findViewById(R.id.QRScan);
        send=findViewById(R.id.send);
        list=findViewById(R.id.list);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        have_pass = mSettings.getString(APP_PREFERENCES_PASSWORD, "");
        String info = mSettings.getString(APP_PREFERENCES_INFO, "");

        Log.d("Start",info);
        String points=mSettings.getString(APP_PREFERENCES_CODES,"");
        String idcheck=mSettings.getString(APP_PREFERENCES_ID,"");
        if (idcheck!="")
        {RL1.setVisibility(View.GONE);
            genName="Генерал "+mSettings.getString(APP_PREFERENCES_ID,"")+": ";
            new_login.setVisibility(View.GONE);
            secRL.setVisibility(View.VISIBLE);}
        String []splitinfo=info.split(",");
        if (splitinfo.length>=1) {
            {
                for (int i = 0; i < splitinfo.length; i++) {
                    Log.d("split", splitinfo[i]);
                    String[] finalString = splitinfo[i].split("#");
                    if (finalString.length>1){
                        String toArray =genName+ finalString[2] + " взята " + finalString[3] + " в " + finalString[4];
                        get.add(toArray);}
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

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new ClearAsyncTask().execute(mSettings.getString(APP_PREFERENCES_ID,""));
                deleteAppData();

            }
        });

        new_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check=false;
                getJSON(sbname);

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
        String temp=mSettings.getString(APP_PREFERENCES_TAKEN,"");

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {

            } else {
                String new_codename = result.getContents();//получили код
                Log.d("QR", "" + new_codename);

                String  getInfo=mSettings.getString(APP_PREFERENCES_INFO,"");
                if (getInfo.contains(new_codename)||temp.contains((new_codename))) {
                    Log.d("Att","Code is exist");
                    Toast toast =Toast.makeText(getApplicationContext(),"Точка была захвачена ранее",Toast.LENGTH_LONG);
                    toast.show();

                    return;
                }
                String points=mSettings.getString(APP_PREFERENCES_CODES,"");
                Log.d("Points",points);
                if (!points.contains(new_codename))
                {
                    //Toast.makeText(this,"Точка была захвачена ранее",Toast.LENGTH_SHORT);
                    return;
                }
                String [] pointsarray=points.split("#");
                for (int i=0;i<pointsarray.length;i++)
                {
                    String [] pointarray=pointsarray[i].split(",");
                    //Log.d("Points","From array: "+ pointsarray[i]);
                    if (pointarray[0].equals(new_codename)) currentpoint=pointarray[1];
                }
                Log.d("Points","Now: "+currentpoint);
                Date currentDate = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                String dateText = dateFormat.format(currentDate);
                DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                String timeText = timeFormat.format(currentDate);

                Log.d("FinalString",first_password.getText().toString()+"#"+new_codename+"#"
                        +currentpoint+"#"+dateText+"#"+timeText);
                String summary=first_password.getText().toString()+"#"+new_codename+"#"
                        +currentpoint+"#"+dateText+"#"+timeText+",";
                String toArray="Генерал "+mSettings.getString(APP_PREFERENCES_ID,"")+": "+currentpoint+" взята " +dateText + " в " + timeText;
                new AddAsyncTask().execute(mSettings.getString(APP_PREFERENCES_ID,""),toArray,new_codename);
                Log.d ("LOG",mSettings.getString(APP_PREFERENCES_ID,"0")+", "+toArray+", "+new_codename);
                get.add(toArray);
                ArrayAdapter adapter = new ArrayAdapter(this,
                        R.layout.list_item, get);

                list.setAdapter(adapter);
                SharedPreferences.Editor editor=mSettings.edit();
                editor.putString(APP_PREFERENCES_INFO,getInfo+summary);
                editor.apply();






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
                        if (urlWebService==sbname)

                        {loadpassinfo(s);
                            Log.d("GetJSON", "loadplayers");
                            if (check==true)
                            {
                                Log.d("Check",""+check);
                                RL1.setVisibility(View.GONE);
                                secRL.setVisibility(View.VISIBLE);
                                new_login.setVisibility(View.GONE);

                            }
                            if (check==false) {
                                Log.d("Check",""+check);
                            }


                        }

                        if (urlWebService==codenames)
                        {
                            loadcodesinso(s);
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
        if (jsonArray.length() == 0) Log.d("GetJson", "Пароль не найден");
        Log.d("GetJSON", "Taking data...");

        String[] id_s = new String[jsonArray.length()];
        String[] gname_s = new String[jsonArray.length()];
        String[] gpass_s = new String[jsonArray.length()];


        if (jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
//
                JSONObject obj = jsonArray.getJSONObject(i);
                id_s[i]=obj.getString("id");
                gname_s[i] = obj.getString("gname");
                gpass_s[i] = obj.getString("gpass");
                //   Log.d("GetJson", gname_s[i] + ":" + gpass_s[i]+"Password: "+first_password.getText().toString());

                if (gpass_s[i].equalsIgnoreCase(first_password.getText().toString())) {
                    check = true;
                    Log.d("Check", "" + check);
                    genname.setText(gname_s[i]);
                    SharedPreferences.Editor editor=mSettings.edit();
                    editor.putString(APP_PREFERENCES_ID,id_s[i]);
                    editor.apply();
                    getJSON(codenames);
                    Log.d("ID",mSettings.getString(APP_PREFERENCES_ID,""));

                }

            }

        }
    }

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
            Log.d("GetCodes", mSettings.getString(APP_PREFERENCES_CODES,""));

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
//    private class UpdateAsyncTask extends AsyncTask<String, Integer, Double> {
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
//        public void postData(String info) {
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpPost httppost = new HttpPost(add_taken);
//            try {
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//                Log.d("CATCH",info);
//                nameValuePairs.add(new BasicNameValuePair("gen_info",info ));
//                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
//                HttpResponse response = httpclient.execute(httppost);
//            } catch (ClientProtocolException e) {
//
//            } catch (IOException e) {
//            }
//
//        }
//    }


    private class AddAsyncTask extends AsyncTask<String, Integer, Double> {
        @Override
        protected Double doInBackground(String... params) {
            postData(params[0],params[1],params[2]);
            return null;
        }

        protected void onPostExecute(Double result) {

        }

        protected void onProgressUpdate(Integer... progress) {
        }

        public void postData(String id, String info, String code) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(add_taken);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("gen_id", id));
                nameValuePairs.add(new BasicNameValuePair("gen_info",info ));
                nameValuePairs.add(new BasicNameValuePair("code",code ));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                HttpResponse response = httpclient.execute(httppost);
            } catch (ClientProtocolException e) {

            } catch (IOException e) {
            }

        }
    }

    private void takenPoints()
    {


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

}