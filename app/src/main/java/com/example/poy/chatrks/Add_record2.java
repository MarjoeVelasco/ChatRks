package com.example.poy.chatrks;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Add_record2 extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    Button time,date,send,clear;
    EditText observer;
    TextView get_time,get_date;
    EditText t_tank,t_drain,t_water_level;
    EditText s_tank,s_drain;
    EditText ppm_tank,ppm_drain;
    EditText tds_tank,tds_drain;
    EditText ec_tank,ec_drain;
    EditText ph_tank,ph_drain;

    Spinner crop;

    private ListView mainListView ;
    ArrayAdapter<String> spinnerArrayAdapter;

    String url="http://"+MainActivity.network+"/CHAT_RKS/android_add_hydroponic.php";
    public static String time3,date3,greenhouse3,crop3,t_tank3,t_drain3,t_water_level3,s_tank3,s_drain3,ppm_tank3,ppm_drain3,tds_tank3,tds_drain3,ec_tank3,ec_drain3,ph_tank3,ph_drain3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record2);

        time=(Button) findViewById(R.id.btn_time);
        date= (Button)findViewById(R.id.btn_date);
        send= (Button)findViewById(R.id.send);
        clear= (Button)findViewById(R.id.clear);

        observer = (EditText)findViewById(R.id.observer);
        crop = (Spinner) findViewById(R.id.spinner);


        get_date = (TextView)findViewById(R.id.in_date);
        get_time = (TextView)findViewById(R.id.in_time);

        t_tank=(EditText)findViewById(R.id.t_tank);
        t_drain=(EditText)findViewById(R.id.t_drain);
        t_water_level=(EditText)findViewById(R.id.t_water_level);

        s_tank=(EditText)findViewById(R.id.s_tank);
        s_drain=(EditText)findViewById(R.id.s_drain);

        ppm_tank=(EditText)findViewById(R.id.ppm_tank);
        ppm_drain=(EditText)findViewById(R.id.ppm_drain);

        tds_tank=(EditText)findViewById(R.id.tds_tank);
        tds_drain=(EditText)findViewById(R.id.tds_drain);

        ec_tank=(EditText)findViewById(R.id.ec_tank);
        ec_drain=(EditText)findViewById(R.id.ec_drain);

        ph_tank=(EditText)findViewById(R.id.ph_tank);
        ph_drain=(EditText)findViewById(R.id.ph_drain);



        observer.setText(MainActivity.username);
        observer.setEnabled(false);

        get_time.setText(time3);
        get_date.setText(date3);

        t_tank.setText(t_tank3);
        t_drain.setText(t_drain3);
        t_water_level.setText(t_water_level3);
        s_tank.setText(s_tank3);
        s_drain.setText(s_drain3);
        ppm_tank.setText(ppm_tank3);
        ppm_drain.setText(ppm_drain3);
        tds_tank.setText(tds_tank3);
        tds_drain.setText(tds_drain3);
        ec_tank.setText(ec_tank3);
        ec_drain.setText(ec_drain3);
        ph_tank.setText(ph_tank3);
        ph_drain.setText(ph_drain3);

        final ArrayList<String> planetList = new ArrayList<String>();

        spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,planetList);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        crop.setAdapter(spinnerArrayAdapter);

        String user=MainActivity.username;
        new AsyncLogin().execute(user);


        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(),"TimePicker");
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(),"Date Picker");
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_date.setText(null);
                get_time.setText(null);

                crop.post(new Runnable() {
                    @Override
                    public void run() {
                        crop.setSelection(0);
                    }
                });

                t_tank.setText(null);
                t_drain.setText(null);
                t_water_level.setText(null);

                s_tank.setText(null);
                s_drain.setText(null);

                ppm_tank.setText(null);
                ppm_drain.setText(null);

                tds_tank.setText(null);
                tds_drain.setText(null);

                ec_tank.setText(null);
                ec_drain.setText(null);

                ph_tank.setText(null);
                ph_drain.setText(null);

                time3 = null;
                date3 =  null;

                t_tank3 =  null;
                t_drain3 =  null;
                t_water_level3 =  null;

                s_tank3 =  null;
                s_drain3 =  null;

                ppm_tank3 =  null;
                ppm_drain3 =  null;

                tds_tank3 =  null;
                tds_drain3 =  null;

                ec_tank3 =  null;
                ec_drain3 =  null;

                ph_tank3 =  null;
                ph_drain3 =  null;

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String time = get_time.getText().toString();
                final String date = get_date.getText().toString();
                final String observer1 = observer.getText().toString();


                final String t_tank1 = t_tank.getText().toString();
                final String t_drain1 = t_drain.getText().toString();
                final String t_water_level1 = t_water_level.getText().toString();

                final String s_tank1 = s_tank.getText().toString();
                final String s_drain1 = s_drain.getText().toString();

                final String ppm_tank1 = ppm_tank.getText().toString();
                final String ppm_drain1 = ppm_drain.getText().toString();

                final String tds_tank1 = tds_tank.getText().toString();
                final String tds_drain1 = tds_drain.getText().toString();

                final String ec_tank1 = ec_tank.getText().toString();
                final String ec_drain1 = ec_drain.getText().toString();

                final String ph_tank1 = ph_tank.getText().toString();
                final String ph_drain1 = ph_drain.getText().toString();

                String temp =crop.getSelectedItem().toString();

                if (time.isEmpty()||date.isEmpty()||observer1.isEmpty()||temp.equals("Please Select Crop")||temp.equals("No crop data found")||t_tank1.isEmpty()||t_drain1.isEmpty()||t_water_level1.isEmpty()||s_tank1.isEmpty()||s_drain1.isEmpty()||ppm_tank1.isEmpty()||ppm_drain1.isEmpty()||tds_tank1.isEmpty()||tds_drain1.isEmpty()||ec_tank1.isEmpty()||ec_drain1.isEmpty()||ph_tank1.isEmpty()||ph_drain1.isEmpty()) {
                    Toast.makeText(Add_record2.this, "Please complete form",Toast.LENGTH_SHORT).show();
                }
                else {

                    String temp1=temp.replace(")","");
                    String temp2=temp1.replaceAll("Greenhouse_no","");
                    String temp3=temp2.replace("(","");

                    String[] in=temp3.split(":");
                    final String crop2 = in[0];
                    final String greenhouse2 = in[1];


                    RequestQueue queue = Volley.newRequestQueue(Add_record2.this);
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Toast.makeText(Add_record2.this, "" + response, Toast.LENGTH_SHORT).show();
                            Log.i("My success", "" + response);
                            get_date.setText(null);
                            get_time.setText(null);

                            t_tank.setText(null);
                            t_drain.setText(null);
                            t_drain.setText(null);

                            s_drain.setText(null);
                            s_drain.setText(null);

                            ppm_drain.setText(null);
                            ppm_drain.setText(null);

                            tds_drain.setText(null);
                            tds_drain.setText(null);

                            ec_drain.setText(null);
                            ec_drain.setText(null);

                            ph_drain.setText(null);
                            ph_drain.setText(null);

                            time3 = null;
                            date3 =  null;
                            greenhouse3 = null;
                            crop3 =  null;

                            t_tank3 =  null;
                            t_drain3 =  null;
                            t_water_level3 =  null;

                            s_tank3 =  null;
                            s_drain3 =  null;

                            ppm_tank3 =  null;
                            ppm_drain3 =  null;

                            tds_tank3 =  null;
                            tds_drain3 =  null;

                            ec_tank3 =  null;
                            ec_drain3 =  null;

                            ph_tank3 =  null;
                            ph_drain3 =  null;

                            Intent intent = new Intent(Add_record2.this, Menu.class);
                            startActivity(intent);
                            Add_record2.this.finish();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {


                            Toast.makeText(Add_record2.this, "Failed to connect to Network", Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> map = new HashMap<String, String>();
                            map.put("date", date);
                            map.put("time", time);
                            map.put("crop", crop2);
                            map.put("observer", observer1);
                            map.put("greenhouse", greenhouse2);

                            map.put("t_tank", t_tank1);
                            map.put("t_drain", t_drain1);
                            map.put("t_water_level", t_water_level1);

                            map.put("s_tank", s_tank1);
                            map.put("s_drain", s_drain1);

                            map.put("ppm_tank", ppm_tank1);
                            map.put("ppm_drain", ppm_drain1);

                            map.put("tds_tank", tds_tank1);
                            map.put("tds_drain", tds_drain1);

                            map.put("ec_tank", ec_tank1);
                            map.put("ec_drain", ec_drain1);

                            map.put("ph_tank", ph_tank1);
                            map.put("ph_drain", ph_drain1);


                            return map;
                        }
                    };
                    queue.add(request);
                }

            }
        });





    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Add_record2.this,Menu.class);
        startActivity(intent);
        Add_record2.this.finish();

        time3 = get_time.getText().toString();
        date3 = get_date.getText().toString();


        t_tank3 = t_tank.getText().toString();
        t_drain3 = t_drain.getText().toString();
        t_water_level3 = t_water_level.getText().toString();

        s_tank3 = s_tank.getText().toString();
        s_drain3 = s_drain.getText().toString();

        ppm_tank3 = ppm_tank.getText().toString();
        ppm_drain3 = ppm_drain.getText().toString();

        tds_tank3 = tds_tank.getText().toString();
        tds_drain3 = tds_drain.getText().toString();

        ec_tank3 = ec_tank.getText().toString();
        ec_drain3 = ec_drain.getText().toString();

        ph_tank3 = ph_tank.getText().toString();
        ph_drain3 = ph_drain.getText().toString();

    }

    private class AsyncLogin extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(Add_record2.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://"+MainActivity.network+"/CHAT_RKS/android_show_crop.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", params[0]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return(result.toString());

                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();

            String temp=result;

            if(result.equalsIgnoreCase("na"))
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                spinnerArrayAdapter.add("No crop data found");
                Message.message(getApplicationContext(), "No crop data found\nPlease Contact your System administrator");


            }
            else
            {

            String temp0=temp.replace('"','=');
            String temp1 = temp0.replace("[","");
            String temp2=temp1.replace("]","");
            String temp3=temp2.replace("{","");


            String temp4=temp3.replace("}","");
            String temp5=temp4.replaceAll("=","");
            String temp6=temp5.replaceAll("greenhouse_no:","");
            String temp7=temp6.replaceAll("name:","");
            String temp8=temp7.replaceAll("start_activity:","");
            String temp9=temp8.replaceAll("last_activity:","");

            String[] out = temp9.split(",");

            spinnerArrayAdapter.add("Please Select Crop");
            int i=0;
            for(i=0;i<out.length;i++) {
                spinnerArrayAdapter.add(out[i] + "(Greenhouse_no:" + out[i+1]+")"+ ":("+ out[i+2]+" - " + out[i+3]+")");
                i++;
                i++;
                i++;
            }



            spinnerArrayAdapter.notifyDataSetChanged();


            }




        }

    }
}
