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

public class View_edit1 extends AppCompatActivity {

    Button time,date,update;
    TextView get_time,get_date,rec_id;
    EditText observer,t_ins1,t_ins2,t_out1,t_out2,h_ins,h_out;
    String id="";
    Spinner crop;
    public static String checker2="";

    private ListView mainListView ;
    ArrayAdapter<String> spinnerArrayAdapter;

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_edit1);


        time=(Button) findViewById(R.id.btn_time);
        date= (Button)findViewById(R.id.btn_date);
        update= (Button)findViewById(R.id.update);

        get_date = (TextView)findViewById(R.id.in_date);
        get_time = (TextView)findViewById(R.id.in_time);
        rec_id=(TextView)findViewById(R.id.rec_id);

        observer = (EditText)findViewById(R.id.observer);
        crop = (Spinner) findViewById(R.id.spinner);

        t_ins1 = (EditText)findViewById(R.id.t_ins1);
        t_ins2 = (EditText)findViewById(R.id.t_ins2);
        t_out1 = (EditText)findViewById(R.id.t_out1);
        t_out2 = (EditText)findViewById(R.id.t_out2);
        h_ins = (EditText)findViewById(R.id.h_ins);
        h_out = (EditText)findViewById(R.id.h_out);

        observer.setText(MainActivity.username);
        observer.setEnabled(false);


        Intent intent = getIntent();


        id= intent.getStringExtra("id");


        rec_id.setText("record no.:"+id);


        final ArrayList<String> planetList = new ArrayList<String>();

        spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,planetList);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        crop.setAdapter(spinnerArrayAdapter);


        new AsyncLogin().execute(id);
        new AsyncLogin1().execute(id);


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

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String time = get_time.getText().toString();
                final String date = get_date.getText().toString();
                final String observer1 = observer.getText().toString();

                final String t_ins11 = t_ins1.getText().toString();
                final String t_ins22 = t_ins2.getText().toString();
                final String t_out11 = t_out1.getText().toString();
                final String t_out22 = t_out2.getText().toString();
                final String h_ins1 = h_ins.getText().toString();
                final String h_out1 = h_out.getText().toString();

                String temp =crop.getSelectedItem().toString();

                if (time.isEmpty()||date.isEmpty()||observer1.isEmpty()||temp.equals("Please Select Crop")||t_ins11.isEmpty()||t_ins22.isEmpty()||t_out11.isEmpty()||t_out22.isEmpty()||h_ins1.isEmpty()||h_out1.isEmpty()) {
                    Message.message(getApplicationContext(),"Please complete form");
                }
                else
                {


                    String temp1=temp.replace(")","");
                    String temp2=temp1.replaceAll("Greenhouse_no","");
                    String temp3=temp2.replace("(","");

                    String[] in=temp3.split(":");
                    final String crop2 = in[0];
                    final String greenhouse2 = in[1];

                    RequestQueue queue = Volley.newRequestQueue(View_edit1.this);
                    StringRequest request = new StringRequest(Request.Method.POST, "http://"+MainActivity.network+"/CHAT_RKS/android_update_greenhouse.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Toast.makeText(View_edit1.this, "" + response, Toast.LENGTH_SHORT).show();
                            Log.i("My success", "" + response);


                            Intent intent = new Intent(View_edit1.this, Histo.class);
                            startActivity(intent);
                            View_edit1.this.finish();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {


                            Toast.makeText(View_edit1.this, "Failed to connect to Network", Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> map = new HashMap<String, String>();
                            map.put("id", id);
                            map.put("date", date);
                            map.put("time", time);
                            map.put("crop", crop2);
                            map.put("greenhouse", greenhouse2);

                            map.put("t_ins1", t_ins11);
                            map.put("t_ins2", t_ins22);

                            map.put("t_out1", t_out11);
                            map.put("t_out2", t_out22);

                            map.put("h_ins", h_ins1);
                            map.put("h_outs", h_out1);


                            return map;
                        }
                    };
                    queue.add(request);
                }
            }
        });

    }

    private class AsyncLogin extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(View_edit1.this);
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
                url = new URL("http://"+MainActivity.network+"/CHAT_RKS/android_show_greenhouse2.php");

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
                        .appendQueryParameter("id", params[0]);
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


            String[] data=temp.split("=");


            checker2=data[0];
            spinnerArrayAdapter.add(data[0]);

            get_date.setText(data[1]);
            get_time.setText(data[2]);
            t_ins1.setText(data[3]);
            t_ins2.setText(data[4]);
            t_out1.setText(data[5]);
            t_out2.setText(data[6]);
            h_ins.setText(data[7]);
            h_out.setText(data[8]);






        }

    }

    private class AsyncLogin1 extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(View_edit1.this);
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
            }
            else {

                String temp0 = temp.replace('"', '=');
                String temp1 = temp0.replace("[", "");
                String temp2 = temp1.replace("]", "");
                String temp3 = temp2.replace("{", "");


                String temp4 = temp3.replace("}", "");
                String temp5 = temp4.replaceAll("=", "");
                String temp6 = temp5.replaceAll("greenhouse_no:", "");
                String temp7 = temp6.replaceAll("name:", "");
                String temp8 = temp7.replaceAll("start_activity:", "");
                String temp9 = temp8.replaceAll("last_activity:", "");

                String[] out = temp9.split(",");

                int i = 0;
                for (i = 0; i < out.length; i++) {
                    String checker = out[i] + "(Greenhouse_no:" + out[i + 1] + ")" + ":(" + out[i + 2] + " - " + out[i + 3] + ")";
                    if (!checker.equals(checker2)) {
                        spinnerArrayAdapter.add(out[i] + "(Greenhouse_no:" + out[i + 1] + ")" + ":(" + out[i + 2] + " - " + out[i + 3] + ")");

                    }
                    i++;
                    i++;
                    i++;
                }

                spinnerArrayAdapter.notifyDataSetChanged();

            }




        }

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(View_edit1.this,Histo.class);
        startActivity(intent);
        View_edit1.this.finish();


    }
}
